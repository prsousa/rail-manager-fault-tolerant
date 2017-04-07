package pt.uminho.sdc.railmanager;

import java.util.List;
import pt.uminho.sdc.cs.Request;
import pt.uminho.sdc.cs.RemoteInvocationException;

public class RailManagerAlarmsRequest extends Request<RailManager, List<String>> {

    public RailManagerAlarmsRequest() {
        
    }

    @Override
    public List<String> apply(RailManager state) throws RemoteInvocationException {
        return state.getAlarms();
    }

    @Override
    public String toString() {
        return "Alarms Request";
    }
}
