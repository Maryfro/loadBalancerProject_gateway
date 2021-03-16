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
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(tcpPort);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);

        while (true) {
            Socket socket = null;
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Connected client");

            Runnable serverTask = new ServerTask(socket, serverSource);
            executor.execute(serverTask);
        }
    }
}
