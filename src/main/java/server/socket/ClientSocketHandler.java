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
                byte[] lengthBuffer = new byte[1];
                int length = in.read(lengthBuffer);
                System.out.println("length buffer" + lengthBuffer);
                System.out.println("length: " + 1024);
                int bytesRead = 0;
                while (bytesRead < 1024){
                    bytesRead += in.read(buffer);
                    result.write(buffer, 0, bytesRead);
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
