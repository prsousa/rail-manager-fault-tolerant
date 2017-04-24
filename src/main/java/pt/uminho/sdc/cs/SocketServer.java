package pt.uminho.sdc.cs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.util.LinkedList;
import pt.uminho.sdc.railmanager.RailManagerImpl;
import spread.MembershipInfo;
import spread.SpreadConnection;
import spread.SpreadException;
import spread.SpreadGroup;
import spread.SpreadMessage;

public class SocketServer<T> {

    private static final Logger logger = LoggerFactory.getLogger(SocketServer.class);

    private final SpreadConnection connection;
    private final SpreadGroup spreadGroup;
    private T state;
    private boolean readyToServe;
    private final LinkedList<SpreadMessage> pendingRequests;

    public SocketServer(String name, boolean firstServer, T state) throws IOException, SpreadException {        
        connection = new SpreadConnection();
        connection.connect(InetAddress.getByName("localhost"), 4803, name, false, true);

        spreadGroup = new SpreadGroup();
        spreadGroup.join(connection, "servers");

        this.state = state;
        this.readyToServe = firstServer;
        this.pendingRequests = new LinkedList<>();
    }

    public void handleRequests() throws SpreadException {
        if (!this.readyToServe) {
            return;
        }

        while (!this.pendingRequests.isEmpty()) {
            SpreadMessage received = this.pendingRequests.pop();
            Request<T, ?> req = (Request<T, ?>) received.getObject();

            Reply rep;

            try {
                rep = new ValueReply<>(req.apply(state));
                logger.trace("current state: {}", state);
            } catch (RemoteInvocationException e) {
                rep = new ErrorReply(e);
            } catch (Exception e) {
                logger.warn("unexpected application exception", e);
                rep = new ErrorReply(new ServerSideException(e));
            }

            logger.debug("sending reply: {}", rep);
            rep.id = req.id;

            SpreadMessage send = new SpreadMessage();
            send.setSafe(); // TODO: is this correct/needed?
            send.addGroup(received.getSender());
            send.setObject(rep);
            connection.multicast(send);
        }
    }

    public void serve() throws IOException, SpreadException {
        logger.info("server starting");
        try {

            while (true) {
                SpreadMessage received = connection.receive();

                if (received.isMembership()) {
                    logger.debug("received membership message {}", received.getMembershipInfo().toString());
                    MembershipInfo membershipInfo = received.getMembershipInfo();

                    if (membershipInfo.isCausedByJoin()) {
                        SpreadGroup newMemeber = received.getMembershipInfo().getJoined();

                        if (!newMemeber.equals(connection.getPrivateGroup())) {
                            Reply rep = new ValueReply<>(state);
                            SpreadMessage send = new SpreadMessage();
                            send.setSafe(); // TODO: is this correct/needed?
                            send.addGroup(newMemeber);
                            send.setObject(rep);
                            connection.multicast(send);
                        }
                    }
                } else {
                    Object receivedObject = received.getObject();

                    if (receivedObject instanceof Request) {
                        logger.debug("received request");
                        this.pendingRequests.add(received);
                    } else if (receivedObject instanceof ValueReply && !this.readyToServe) {
                        logger.debug("received state");
                        ValueReply<T> valRep = (ValueReply<T>) receivedObject;
                        this.state = valRep.getValue();
                        this.readyToServe = true;
                    }

                    handleRequests();
                    
                    logger.debug("current state {}", (RailManagerImpl) this.state);
                }
            }

        } catch (IOException e) {
            logger.error("error reading request, closing connection", e);
        }

        try {
            connection.disconnect();
        } catch (SpreadException ex) {
            logger.trace("spread error {}", ex);
        }

        logger.info("server stopped");
    }
}
