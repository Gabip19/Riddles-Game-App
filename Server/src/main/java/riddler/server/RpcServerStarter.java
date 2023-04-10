package riddler.server;

import riddler.network.server.AbstractServer;
import riddler.network.server.RpcConcurrentServer;
import riddler.repository.UserRepository;
import riddler.repository.jdbc.UserDBRepository;
import riddler.server.service.ConcreteService;
import riddler.services.Services;

import java.io.IOException;
import java.util.Properties;

public class RpcServerStarter {
    private static final int defaultPort = 44444;

    public static void main(String[] args) {
        Properties serverProps = readProperties();
        Services services = initService(serverProps);

        int chatServerPort = defaultPort;
        try {
            chatServerPort = Integer.parseInt(serverProps.getProperty("server.port"));
        } catch (NumberFormatException e){
            System.err.println("Wrong port number" + e.getMessage());
            System.err.println("Using default port " + defaultPort);
        }

        System.out.println("Starting server on port: " + chatServerPort);
        AbstractServer server = new RpcConcurrentServer(chatServerPort, services);
        try {
            server.start();
        } catch (Exception e) {
            System.err.println("Error starting the server" + e.getMessage());
        } finally {
            try {
                server.stop();
            } catch(Exception e){
                System.err.println("Error stopping the server " + e.getMessage());
            }
        }
    }

    private static Properties readProperties() {
        Properties properties = new Properties();
        try {
            properties.load(RpcServerStarter.class.getResourceAsStream("/server.config"));
            System.out.println("Server properties set.");
            properties.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find server.config " + e);
            return null;
        }
        return properties;
    }

    private static Services initService(Properties props) {
        UserRepository userRepo = new UserDBRepository(props);

        return new ConcreteService(
                userRepo
        );
    }
}
