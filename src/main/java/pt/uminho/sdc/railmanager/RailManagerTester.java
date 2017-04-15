package pt.uminho.sdc.railmanager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RailManagerTester {

    private static Logger logger = LoggerFactory.getLogger(RailManagerTester.class);

    private final RailManagerSupplier supplier;

    //@FunctionalInterface
    public interface RailManagerSupplier {

        RailManager get() throws Exception;
    }

    public RailManagerTester(RailManagerSupplier supplier, int nthr, long seconds) {
        this.supplier = supplier;
        logger.info("testing rail manager implementation: threads = {}, seconds = {}", nthr, seconds);
    }

    public RailManagerTester(RailManagerSupplier supplier, String[] args) {
        this.supplier = supplier;
    }

    public void test() throws InterruptedException, Exception {
        RailManager r = supplier.get();

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        long start;
        long stopTime;
        long execution;

        String line;
        while (true) {
            line = br.readLine();
            String[] tokens = line.split(" ");

            String method = tokens[0];

            try {
                switch (method) {
                    case "access":
                        start = System.currentTimeMillis();
                        boolean access = r.access(tokens[1], Integer.valueOf(tokens[2]), tokens[3].charAt(0));
                        stopTime = System.currentTimeMillis();
                        execution =  stopTime - start;
                        System.out.println("\n---------------------------------------------------------");
                        System.out.println(line + ": " + access);
                        System.out.println("'access' execution time  -> " + execution + " miliseconds");   
                        System.out.println("---------------------------------------------------------");
                        break;
                    case "enter":
                        start = System.currentTimeMillis();
                        r.enter(tokens[1], Integer.valueOf(tokens[2]), tokens[3].charAt(0));
                        stopTime = System.currentTimeMillis();
                        execution =  stopTime - start;
                        System.out.println("\n--------------------------------------------------------");
                        System.out.println(line);
                        System.out.println("'enter' execution time  -> " + execution + " miliseconds");
                        System.out.println("--------------------------------------------------------");
                        break;
                    case "leave":
                        start = System.currentTimeMillis();
                        r.leave(tokens[1], Integer.valueOf(tokens[2]), tokens[3].charAt(0));
                        stopTime = System.currentTimeMillis();
                        execution =  stopTime - start;
                        System.out.println("\n--------------------------------------------------------");
                        System.out.println(line);
                        System.out.println("'leave' execution time  -> " + execution + " miliseconds");
                        System.out.println("--------------------------------------------------------");
                        break;
                    case "getPositions":
                        start = System.currentTimeMillis();
                        Map<Character, Integer> positions = r.getPositions(tokens[1]);
                        stopTime = System.currentTimeMillis();
                        execution =  stopTime - start;
                        System.out.println("\n---------------------------------------------------------------");
                        System.out.println(line + ": " + positions);
                        System.out.println("'getPositions' execution time  -> " + execution + " miliseconds"); 
                        System.out.println("---------------------------------------------------------------");
                        break;
                    case "getAlarms":
                        start = System.currentTimeMillis();
                        List<String> alarms = r.getAlarms();
                        stopTime = System.currentTimeMillis();
                        execution =  stopTime - start;
                        System.out.println("\n------------------------------------------------------------");
                        System.out.println(line + ": " + alarms);
                        System.out.println("'getAlarms' execution time  -> " + execution + " miliseconds"); 
                        System.out.println("------------------------------------------------------------");
                }
            } catch (Exception e) {
                logger.trace("client error {}", e);
            }
        }
    }
}
