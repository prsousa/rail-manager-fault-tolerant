package pt.uminho.sdc.railmanager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RailManagerTester {

    private static final String[] rails = {"Braga-Nine", "Nine-Braga", "Viana-Porto", "Porto-Viana"};
    private static final int[] rails_segments = {5, 5, 10, 10};
    private static final int number_compositions = 5;

    private static Logger logger = LoggerFactory.getLogger(RailManagerTester.class);

    private final RailManagerSupplier supplier;
    private final Random random;
    private Stage stage;
    private int nops;
    private long totalrtt;

    //@FunctionalInterface
    public interface RailManagerSupplier {

        RailManager get() throws Exception;
    }

    public RailManagerTester(RailManagerSupplier supplier, int nthr, long seconds) {
        this.supplier = supplier;
        logger.info("testing rail manager implementation: threads = {}, seconds = {}", nthr, seconds);
        this.random = new Random();
    }

    public RailManagerTester(RailManagerSupplier supplier, String[] args) {
        this.supplier = supplier;
        this.random = new Random();
    }

    public void test() throws InterruptedException, Exception {
        RailManager r = supplier.get();

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        long startTime, stopTime, execTime;

        String line, response;
        while (true) {
            System.out.print("> ");

            response = "";
            line = br.readLine();
            String[] tokens = line.split(" ");
            String method = tokens[0];

            startTime = System.currentTimeMillis();

            try {
                switch (method) {
                    case "access":
                        boolean access = r.access(tokens[1], Integer.valueOf(tokens[2]), tokens[3].charAt(0));
                        response = Boolean.toString(access);
                        break;
                    case "enter":
                        r.enter(tokens[1], Integer.valueOf(tokens[2]), tokens[3].charAt(0));
                        break;
                    case "leave":
                        r.leave(tokens[1], Integer.valueOf(tokens[2]), tokens[3].charAt(0));
                        break;
                    case "getPositions":
                        Map<Character, Integer> positions = r.getPositions(tokens[1]);
                        response = positions.toString();
                        break;
                    case "getAlarms":
                        List<String> alarms = r.getAlarms();
                        response = alarms.toString();
                        break;
                    case "exit":
                        return;
                    case "test":
                        int time = (tokens.length >= 2) ? Integer.valueOf(tokens[1]) : 10;
                        time *= 1000;
                        int numThreads = (tokens.length >= 3) ? Integer.valueOf(tokens[2]) : 1;
                        this.stage = Stage.Warmup;
                        this.nops = 0;
                        this.totalrtt = 0;

                        Worker worker[] = new Worker[numThreads];

                        for (int i = 0; i < worker.length; i++) {
                            worker[i] = new Worker();
                        }
                        for (int i = 0; i < worker.length; i++) {
                            worker[i].start();
                        }

                        if (!waitInStage(Stage.Warmup, time / 10)) {
                            logger.error("test aborted during warmup");
                        }

                        long before = System.nanoTime();

                        logger.info("warmup complete: running!");

                        setStage(Stage.Run);

                        if (!waitInStage(Stage.Run, time)) {
                            logger.error("test aborted during measurement");
                        }

                        setStage(Stage.Shutdown);

                        long after = System.nanoTime();

                        logger.info("complete: shutting down");

                        for (int i = 0; i < worker.length; i++) {
                            worker[i].join();
                        }

                        if (stage != Stage.Shutdown) {
                            logger.error("test aborted");
                            return;
                        }

                        logger.info("performance: {} ops, {} ops/s, {} s", nops, nops / ((after - before) / 1e9d), (totalrtt / 1e9d) / nops);

                        break;
                    case "help":
                    default:
                        StringBuilder sb = new StringBuilder();
                        sb.append("Usage").append("\n");
                        sb.append("\t").append("access <rail_name> <segment_number> <composition_char>").append("\n");
                        sb.append("\t").append("enter <rail_name> <segment_number> <composition_char>").append("\n");
                        sb.append("\t").append("leave <rail_name> <segment_number> <composition_char>").append("\n");
                        sb.append("\t").append("getPositions <rail_name>").append("\n");
                        sb.append("\t").append("getAlarms").append("\n");
                        sb.append("\t").append("exit").append("\n");
                        sb.append("\t").append("test [seconds] [thread_number]").append("\n");

                        System.out.println(sb.toString());
                        continue;
                }

                stopTime = System.currentTimeMillis();
                execTime = stopTime - startTime;

                System.out.print(response);
                System.out.println("\t(" + execTime + "ms)");
            } catch (Exception e) {
                logger.trace("client error {}", e);
            }
        }
    }

    private static enum Stage {
        Warmup, Run, Shutdown, Error
    };

    private synchronized void setStage(Stage stage) {
        if (stage.compareTo(this.stage) <= 0) {
            return;
        }
        this.stage = stage;
    }

    private synchronized boolean waitInStage(Stage stage, long time) throws InterruptedException {
        long now = System.currentTimeMillis();
        long target = now + time;
        while (this.stage == stage && now < target) {
            wait(target - now);
            now = System.currentTimeMillis();
        }
        return this.stage == stage;
    }

    private synchronized void log(long delta) {
        if (stage != Stage.Run) {
            return;
        }

        nops++;
        totalrtt += delta;
    }

    private synchronized boolean isRunning() {
        return stage.compareTo(Stage.Run) <= 0;
    }

    private class Worker extends Thread {

        @Override
        public void run() {
            try {
                RailManager r = supplier.get();
                logger.debug("worker connected to bank");

                while (isRunning()) {
                    int railId = random.nextInt(rails.length);
                    String rail = rails[railId];
                    int numberSegments = rails_segments[railId];
                    char composition = (char) ('A' + random.nextInt(number_compositions));
                    
                    long before = System.nanoTime();
                    boolean canAccess = r.access(rail, numberSegments, composition);
                    long after = System.nanoTime();
                    log(after - before);
                    
                    if( canAccess ) {
                        before = System.nanoTime();
                        r.enter(rail, numberSegments, composition);
                        after = System.nanoTime();
                        
                        before = System.nanoTime();
                        log(after - before);
                        r.leave(rail, numberSegments, composition);
                        after = System.nanoTime();
                        log(after - before);
                    }
                    
                }
            } catch (Exception e) {
                logger.error("worker stopping on exception", e);
                setStage(Stage.Error);
            }
        }
    }
}
