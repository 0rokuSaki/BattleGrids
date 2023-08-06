package Client;

import Shared.Server;

public class ServerStubHolder {

    private static final ServerStubHolder instance = new ServerStubHolder();

    private Server serverStub;

    private ServerStubHolder() {}

    public static synchronized ServerStubHolder getInstance() {
        return instance;
    }

    public void setServerStub(Server serverStub) {
        this.serverStub = serverStub;
    }

    public Server getServerStub() {
        return serverStub;
    }
}
