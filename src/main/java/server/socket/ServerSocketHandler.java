package server.socket;

import server.Server;
import server.model.ServerState;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
// import java.net.http.HttpRequest;


public class ServerSocketHandler extends Thread {
    int port;
    int clientNumber = 0;
    ServerSocket serverSocket;
    public ServerSocketHandler(int portNum, ServerState serverState) throws IOException {
        port = portNum;
        System.out.println("connecting to port" + port);
        serverSocket = new ServerSocket(portNum, 0, InetAddress.getByName("10.0.0.205"));
        System.out.println("server socket: " + serverSocket);
    }

    @Override
    public void run() {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(100);

        while (Server.serverState.isOpened()){
            try {
                Socket clientSocket = serverSocket.accept();
                executor.submit(()->{
                    ClientSocketHandler clientSocketHandler = null;
                    try {
                        clientSocketHandler = new ClientSocketHandler(clientSocket, clientNumber++);
                        clientSocketHandler.start();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                // break;

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}