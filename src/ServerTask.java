import model.ServerInfo;
import model.ServerSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class ServerTask implements Runnable {

    private Socket socketFromClient;
    private ServerSource serverSource;

    public ServerTask(Socket socketFromClient, ServerSource serverSource) {
        this.socketFromClient = socketFromClient;
        this.serverSource = serverSource;
    }

    @Override
    public void run() {
        ServerInfo serverInfo = serverSource.getOptimalServer();
        String host = serverInfo.getHostname();
        int port = serverInfo.getPort();
        Socket socketToServer = null;
        try {
            socketToServer = new Socket(host, port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (PrintStream toClient = new PrintStream(socketFromClient.getOutputStream());
             BufferedReader fromClient = new BufferedReader(new InputStreamReader(socketFromClient.getInputStream()));
             PrintStream toServer = new PrintStream(socketToServer.getOutputStream());
             BufferedReader fromServer = new BufferedReader(new InputStreamReader(socketToServer.getInputStream()));) {
            String lineFromClient;
            while ((lineFromClient = fromClient.readLine()) != null) {
                toServer.println(lineFromClient);
                String lineFromServer = fromServer.readLine();
                toClient.println(lineFromServer);
            }
            socketToServer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
