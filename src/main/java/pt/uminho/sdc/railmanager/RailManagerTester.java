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

    @FunctionalInterface
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

        String line;
        while (true) {
            line = br.readLine();
            String[] tokens = line.split(" ");

            String method = tokens[0];

            try {
                switch (method) {
                    case "access":
                        boolean access = r.access(tokens[1], Integer.valueOf(tokens[2]), tokens[3].charAt(0));
                        System.out.println(line + ": " + access);
                        break;
                    case "enter":
                        r.enter(tokens[1], Integer.valueOf(tokens[2]), tokens[3].charAt(0));
                        System.out.println(line);
                        break;
                    case "leave":
                        r.leave(tokens[1], Integer.valueOf(tokens[2]), tokens[3].charAt(0));
                        System.out.println(line);
                        break;
                    case "getPositions":
                        Map<Character, Integer> positions = r.getPositions(tokens[1]);
                        System.out.println(line + ": " + positions);
                        break;
                    case "getAlarms":
                        List<String> alarms = r.getAlarms();
                        System.out.println(line + ": " + alarms);
                }
            } catch (Exception e) {
                logger.trace("client error {}", e);
            }
        }
    }
}
