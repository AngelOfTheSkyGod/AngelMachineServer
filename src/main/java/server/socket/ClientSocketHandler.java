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
                    System.out.println("in loop");
                    result.write(buffer, 0, b);
                    System.out.println("receiving2: " +  result);
                    System.out.println("in loop");
                }
                System.out.println("out of loop");
                System.out.println(result);
                System.out.println("after result");
                ServerObject serverObject = null;
                System.out.println("result size: " + result.size() + " result: " + result);
                if (result.size() > 1){
                    serverObject = ServerObjectParser.parse(result.toString(), Optional.of(clientNumber));
                    System.out.println("serverobject: " + serverObject);
                    ServerController.handleCommand(this, serverObject);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
