package pt.uminho.sdc.railmanager;

import pt.uminho.sdc.cs.Request;
import pt.uminho.sdc.cs.RemoteInvocationException;

public class RailManagerLeaveRequest extends Request<RailManager, Void> {

    private final String line;
    private final int segment;
    private final char composition;

    public RailManagerLeaveRequest(String line, int segment, char composition) {
        this.line = line;
        this.segment = segment;
        this.composition = composition;
    }

    @Override
    public Void apply(RailManager state) throws RemoteInvocationException {
        state.leave(line, segment, composition);
        return null;
    }

    @Override
    public String toString() {
        return "Leave Request: line = " + line + ", segment = " + segment + ", composition = " + composition;
    }
}
