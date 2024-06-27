package server;

import ch.qos.logback.core.net.server.Client;
import lombok.Getter;
import server.model.ServerState;
import server.socket.ClientSocketHandler;
import server.socket.ServerSocketHandler;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
@Getter
public class Server {
    HashMap<Integer, ClientSocketHandler> machineToSocketMap = new HashMap<>();
    HashMap<Integer, Integer> clientToMachineMap = new HashMap<>();
    public static Server server = null;
    public static ServerState serverState;
    int port;
    ServerSocketHandler serverSocketHandler = null;
    public Server() throws IOException {
        serverState = new ServerState();
        port = 8080;
        serverSocketHandler = new ServerSocketHandler(port, serverState);
    }
    public static Server getServer() throws IOException {
        if (server == null){
            serverState = new ServerState();
            server = new Server();
        }
        return server;
    }
}
