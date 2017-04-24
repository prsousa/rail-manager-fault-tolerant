package pt.uminho.sdc.railmanager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestLocal {

    private static final Logger logger = LoggerFactory.getLogger(RailManagerServer.class);

    public static void main(String[] args) throws Exception {
        RailManagerImpl r = new RailManagerImpl();
        logger.debug("Seeding RailManager");
        r.addRail("Nine-Braga", new Rail(5));
        r.addRail("Braga-Nine", new Rail(5));
        r.addRail("Viana-Porto", new Rail(10));
        r.addRail("Porto-Viana", new Rail(10));

        new RailManagerTester(() -> r, args).test();
    }
}
