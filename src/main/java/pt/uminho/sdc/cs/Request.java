package pt.uminho.sdc.cs;

public abstract class Request<T,V> extends Message {
    public abstract V apply(T state) throws RemoteInvocationException;
}
