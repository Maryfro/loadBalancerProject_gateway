package model;

import model.ServerInfo;

public class ServerSource {

    ServerInfo optimalServer;

    public synchronized ServerInfo getOptimalServer() {
        return optimalServer;
    }

    public synchronized void updateOptimalServer(ServerInfo updates) {

        optimalServer = updates;
    }
}
