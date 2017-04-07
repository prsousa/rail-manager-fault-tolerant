package pt.uminho.sdc.railmanager;

public class TestLocal {
    public static void main(String[] args) throws Exception {
        RailManager r = new RailManagerImpl();
        new RailManagerTester(() -> r, args).test();
    }
}
