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
}
