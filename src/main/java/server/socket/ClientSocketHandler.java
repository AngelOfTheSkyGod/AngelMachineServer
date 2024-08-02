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
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
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
                in.read(buffer, 0, 1);
                int length = Byte.toUnsignedInt(buffer[0]);
                System.out.println("length: " + length);
                int totalBytesRead = 0;
                int bytesRead = 0;
                while (totalBytesRead < length){
                    bytesRead = in.read(buffer, 0, Math.min(buffer.length, length - totalBytesRead));
                    if (bytesRead == -1) break;  // End of stream
                    totalBytesRead += bytesRead;
                    result.write(buffer, 0, bytesRead);
                }
                System.out.println("Out of loop");

                byte[] byteArray = result.toByteArray();
                String base64String = Base64.getEncoder().encodeToString(byteArray);
                System.out.println(base64String);  // Base64 representation
//                if (result.size() > 1){
//                    serverObject = ServerObjectParser.parse(result.toString(), Optional.of(clientNumber));
//                    System.out.println("serverobject: " + serverObject);
//                    ServerController.handleCommand(this, serverObject);
//                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
