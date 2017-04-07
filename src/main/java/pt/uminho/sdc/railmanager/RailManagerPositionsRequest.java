package pt.uminho.sdc.railmanager;

import java.util.Map;
import pt.uminho.sdc.cs.Request;
import pt.uminho.sdc.cs.RemoteInvocationException;

public class RailManagerPositionsRequest extends Request<RailManager, Map<Character, Integer>> {

    private final String line;

    public RailManagerPositionsRequest(String line) {
        this.line = line;
    }

    @Override
    public Map<Character, Integer> apply(RailManager state) throws RemoteInvocationException {
        return state.getPositions(line);
    }

    @Override
    public String toString() {
        return "Positions Request: line = " + line;
    }
}
