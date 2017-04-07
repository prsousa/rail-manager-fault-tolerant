package pt.uminho.sdc.railmanager;

import pt.uminho.sdc.cs.Request;
import pt.uminho.sdc.cs.RemoteInvocationException;

public class RailManagerEnterRequest extends Request<RailManager, Void> {

    private final String line;
    private final int segment;
    private final char composition;

    public RailManagerEnterRequest(String line, int segment, char composition) {
        this.line = line;
        this.segment = segment;
        this.composition = composition;
    }

    @Override
    public Void apply(RailManager state) throws RemoteInvocationException {
        state.enter(line, segment, composition);
        return null;
    }

    @Override
    public String toString() {
        return "Enter Request: line = " + line + ", segment = " + segment + ", composition = " + composition;
    }
}
