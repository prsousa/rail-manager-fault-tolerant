package pt.uminho.sdc.railmanager;

import pt.uminho.sdc.cs.SocketClient;

public class TestRemote {
    public static void main(String[] args) throws Exception {
        new RailManagerTester(
                () -> new RailManagerClient(
                    new SocketClient<>()
                ), args)
            .test();
    }
}
