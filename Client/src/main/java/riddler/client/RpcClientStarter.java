package riddler.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import riddler.client.controller.GuiController;
import riddler.client.controller.LoginController;
import riddler.client.controller.MainWindowController;
import riddler.network.rpcprotocol.ServicesRpcProxy;
import riddler.services.Services;

import java.io.IOException;
import java.util.Properties;

public class RpcClientStarter extends Application {
    private static final int defaultChatPort = 44444;
    private static final String defaultServer = "localhost";

    @Override
    public void start(Stage stage) throws IOException {
        Properties props = readProperties();
        Services service = createProxy(props);
        GuiController.setSrv(service);

        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/login.fxml"));
        Scene loginScene = new Scene(loginLoader.load());
        LoginController loginController = loginLoader.getController();

        FXMLLoader mainWindowLoader = new FXMLLoader(getClass().getResource("/main-window.fxml"));
        Parent mainWindowRoot = mainWindowLoader.load();
        MainWindowController mainWindowController = mainWindowLoader.getController();

        loginController.setMainController(mainWindowController);
        loginController.setMainWindowRoot(mainWindowRoot);

        stage.setTitle("Teledon Login");
        stage.setScene(loginScene);
        stage.show();
    }

    private static Services createProxy(Properties clientProps) {
        String serverIP = clientProps.getProperty("server.host", defaultServer);
        int serverPort = defaultChatPort;

        try {
            serverPort = Integer.parseInt(clientProps.getProperty("server.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultChatPort);
        }
        System.out.println("[PROXY] Using server IP " + serverIP);
        System.out.println("[PROXY] Using server port " + serverPort);

        return new ServicesRpcProxy(serverIP, serverPort);
    }

    private static Properties readProperties() {
        Properties properties = new Properties();
        try {
            properties.load(RpcClientStarter.class.getResourceAsStream("/client.config"));
            System.out.println("[PROXY] Server properties set.");
            properties.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find server.config " + e);
            return null;
        }
        return properties;
    }
}
