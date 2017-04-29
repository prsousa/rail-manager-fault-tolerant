package pt.uminho.sdc.cs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.InetAddress;
import java.util.logging.Level;
import spread.SpreadConnection;
import spread.SpreadException;
import spread.SpreadGroup;
import spread.SpreadMessage;

public class SocketClient<T> implements Client<T> {

    private static final Logger logger = LoggerFactory.getLogger(SocketClient.class);

    SpreadConnection connection;
    SpreadGroup spreadGroup;

    private int lastSentId = 0;

    public SocketClient() throws IOException, SpreadException {
        String name = "cli" + Math.round(Math.random() * System.nanoTime());
        connection = new SpreadConnection();
        connection.connect(InetAddress.getByName("localhost"), 4803, name, false, false);
    }

    @Override
    public synchronized <V> V request(Request<T, V> req) throws RemoteInvocationException {
        Reply rep = null;
        req.id = ++this.lastSentId;

        try {
            SpreadMessage send = new SpreadMessage();
            send.setSafe();
            send.addGroup("servers");
            send.setObject(req);
            connection.multicast(send);
        } catch (SpreadException ex) {
            java.util.logging.Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            do {
                SpreadMessage received = connection.receive();
                rep = (Reply) received.getObject();
            } while (rep.id != req.id);

        } catch (SpreadException | InterruptedIOException ex) {
            java.util.logging.Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (rep instanceof ValueReply) {
            return ((ValueReply<V>) rep).getValue();
        } else {
            throw ((ErrorReply) rep).getException();
        }
    }

    public void close() throws IOException, SpreadException {
        spreadGroup.leave();
        connection.disconnect();
    }
}
