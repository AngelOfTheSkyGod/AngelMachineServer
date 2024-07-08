package server.socket;

import lombok.Getter;
import lombok.Setter;
import server.Server;
import server.controller.ClientController;
import server.controller.ServerController;
import server.model.ServerObject;
import server.parsers.ServerObjectParser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Optional;

@Getter
@Setter
public class ClientSocketHandler extends Thread{

    Integer clientNumber;
    String password;
    Socket clientSocket;
    ClientController clientController;
    ClientSocketHandler(Socket clientSocket, int clientNumber) {
        this.clientSocket = clientSocket;
        this.clientNumber = clientNumber;
    }

    @Override
    public void run() {
        try {
            InputStream in = clientSocket.getInputStream();
            OutputStream out = clientSocket.getOutputStream();
            clientController = new ClientController(clientSocket, in, out);

            ByteArrayOutputStream result = new ByteArrayOutputStream();
            while(Server.serverState.isOpened()){
                byte[] buffer = new byte[1024];
                int b;
                while((b = in.read(buffer)) != -1){
                    result.write(buffer, 0, b);
                }
                System.out.println(result);

                ServerObject serverObject = null;
                if (result.size() > 1){
                    serverObject = ServerObjectParser.parse(result.toString(), Optional.of(clientNumber));
                    ServerController.handleCommand(this, serverObject);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
