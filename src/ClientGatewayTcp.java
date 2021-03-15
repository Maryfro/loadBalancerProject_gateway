import model.ServerInfo;
import model.ServerSource;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientGatewayTcp implements Runnable {
    private ServerSource serverSource;
    private int tcpPort;
    private int threadPoolSize;


    public ClientGatewayTcp(ServerSource serverSource, int tcpPort, int threadPoolSize) throws IOException {
        this.serverSource = serverSource;
        this.tcpPort = tcpPort;
        this.threadPoolSize = threadPoolSize;
    }

    @Override
    public void run() {

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(tcpPort);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);
        while (true) {
            Socket socketToClient = null;
            try {
                socketToClient = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Connected");
            Runnable serverTask = new ServerTask(socketToClient, serverSource);
            executor.execute(serverTask);

        }
    }
}
