package server;

import lombok.Getter;
import server.model.ServerState;
import server.socket.ClientSocketHandler;
import server.socket.ServerSocketHandler;

import java.io.IOException;
import java.util.HashMap;
@Getter
public class Server {
    public static HashMap<String, ClientSocketHandler> machineToSocketMap = new HashMap<>();
    public static HashMap<Integer, String> clientToMachineMap = new HashMap<>();
    public static Server server = null;
    public static ServerState serverState;
    static int port;
    ServerSocketHandler serverSocketHandler = null;
    public Server() throws IOException {
        serverState = new ServerState();
        port = 8081;
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
