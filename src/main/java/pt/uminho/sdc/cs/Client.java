package pt.uminho.sdc.cs;

public interface Client<T> {
    <V> V request(Request<T,V> req) throws RemoteInvocationException;
}
