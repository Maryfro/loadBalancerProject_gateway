import model.ServerSource;

import java.io.IOException;

public class Main {
    static int BALANCER_PORT = 3001;
    static int GATEWAY_PORT = 3000;
    private static final String DEFAULT_PROPS_PATH = "config/application.props";
    private static final String TCP_OUTER_PORT_KEY = "tcp.outer.port";
    private static final String UDP_FROM_BALANCER_PORT_KEY = "udp.balancer.port";


    public static void main(String[] args) throws IOException {
        String propsPath = args.length > 0 ? args[0] : DEFAULT_PROPS_PATH;
        ApplicationProperties properties = new ApplicationProperties(propsPath);

        int tcpOuterPort = Integer.parseInt(properties.getProperty(TCP_OUTER_PORT_KEY));
        int udpFromBalancerPort = Integer.parseInt(properties.getProperty(UDP_FROM_BALANCER_PORT_KEY));

        ServerSource serverSource = new ServerSource();
        ClientGatewayTcp clientGatewayTcp = new ClientGatewayTcp(serverSource, tcpOuterPort, 3);
        BalancerGatewayUdp fromBalancer = new BalancerGatewayUdp(serverSource, udpFromBalancerPort);
        new Thread(clientGatewayTcp);
        new Thread(fromBalancer);
    }
}
