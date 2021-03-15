import model.ServerInfo;
import model.ServerSource;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class BalancerGatewayUdp implements Runnable {

    private ServerSource serverSource;

    private int udpPort;
    private final int PACKET_SIZE = 1024;

    public BalancerGatewayUdp(ServerSource serverSource, int udpPort) {
        this.serverSource = serverSource;
        this.udpPort = udpPort;
    }

    @Override
    public void run() {
        DatagramSocket serverUdpSocket = null;
        try {
            serverUdpSocket = new DatagramSocket(udpPort);
            byte[] dataIn = new byte[PACKET_SIZE];
            DatagramPacket packetIn = new DatagramPacket(dataIn, PACKET_SIZE);

            while (true) {
                try {
                    serverUdpSocket.receive(packetIn);
                    String line = new String(dataIn, 0, packetIn.getLength());
                    ServerInfo serverInfo = getPortServer(line);
                    if (serverInfo != null) {
                        serverSource.updateOptimalServer(serverInfo);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    private ServerInfo getPortServer(String packet) {

        String[] portServerArray = packet.split(":");
        if (portServerArray != null) {
            String server = portServerArray[0];
            int port = Integer.parseInt(portServerArray[1]);
            ServerInfo serverInfo = new ServerInfo(server, port);
            System.out.println("Balancer " + server + " port " + port);
            return serverInfo;
        }
        return null;
    }
}
