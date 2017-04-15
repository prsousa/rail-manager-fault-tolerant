package pt.uminho.sdc.railmanager;

import java.util.List;
import java.util.Map;
import pt.uminho.sdc.cs.Client;
import pt.uminho.sdc.cs.RemoteInvocationException;

public class RailManagerClient implements RailManager {

    private final Client<RailManager> client;

    public RailManagerClient(Client<RailManager> client) {
        this.client = client;
    }

    @Override
    public boolean access(String line, int segment, char composition) throws RemoteInvocationException {
        return client.request(new RailManagerAccessRequest(line, segment, composition));
    }

    @Override
    public void enter(String line, int segment, char composition) throws RemoteInvocationException {
        client.request(new RailManagerEnterRequest(line, segment, composition));
    }

    @Override
    public void leave(String line, int segment, char composition) throws RemoteInvocationException {
        client.request(new RailManagerLeaveRequest(line, segment, composition));
    }

    @Override
    public Map<Character, Integer> getPositions(String line) throws RemoteInvocationException {
        return client.request(new RailManagerPositionsRequest(line));
    }

    @Override
    public List<String> getAlarms() throws RemoteInvocationException {
        return client.request(new RailManagerAlarmsRequest());
    }
}