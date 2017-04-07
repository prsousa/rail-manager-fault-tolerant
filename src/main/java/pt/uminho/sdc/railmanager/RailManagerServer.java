package pt.uminho.sdc.railmanager;

import pt.uminho.sdc.cs.SocketServer;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spread.SpreadException;

public class RailManagerServer {
    
    private static final Logger logger = LoggerFactory.getLogger(RailManagerServer.class);

    public static void main(String[] args) throws IOException, SpreadException {        
        boolean firstServer = false;
        if (args.length > 1) {
            firstServer = Boolean.valueOf(args[1]);
        }

        RailManagerImpl railManager = new RailManagerImpl();
        
        if (firstServer) {
            logger.debug("Seeding RailManager");
            railManager.addRail("Nine-Braga", new Rail(5));
            railManager.addRail("Braga-Nine", new Rail(5));
            railManager.addRail("Viana-Nine", new Rail(10));
            railManager.addRail("Nine-Viana", new Rail(10));
        }

        new SocketServer<>(args[0], firstServer, railManager).serve();
    }
}
