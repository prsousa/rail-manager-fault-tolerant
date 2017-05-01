package pt.uminho.sdc.railmanager;

import java.util.Map;
import pt.uminho.sdc.cs.Request;
import pt.uminho.sdc.cs.RemoteInvocationException;

public class RailManagerRailsRequest extends Request<RailManager, Map<String, Integer>> {

    @Override
    public Map<String, Integer> apply(RailManager state) throws RemoteInvocationException {
        return state.getRails();
    }

    @Override
    public String toString() {
        return "Rails Request";
    }
}
