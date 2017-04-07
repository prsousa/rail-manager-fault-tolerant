package pt.uminho.sdc.railmanager;

import pt.uminho.sdc.cs.Request;
import pt.uminho.sdc.cs.RemoteInvocationException;

public class RailManagerAccessRequest extends Request<RailManager, Boolean> {

    private final String line;
    private final int segment;
    private final char composition;

    public RailManagerAccessRequest(String line, int segment, char composition) {
        this.line = line;
        this.segment = segment;
        this.composition = composition;
    }

    @Override
    public Boolean apply(RailManager state) throws RemoteInvocationException {
        return state.access(line, segment, composition);
    }

    @Override
    public String toString() {
        return "Access Request: line = " + line + ", segment = " + segment + ", composition = " + composition;
    }
}
