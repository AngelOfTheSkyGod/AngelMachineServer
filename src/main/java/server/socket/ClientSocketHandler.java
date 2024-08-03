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
import java.util.Arrays;
import java.util.Optional;

@Getter
@Setter
public class ClientSocketHandler extends Thread{

    Integer clientNumber;
    String password;
    Socket clientSocket;
    ClientController clientController;
    ServerController serverController;
    ClientSocketHandler(Socket clientSocket, int clientNumber) throws IOException {
        this.clientSocket = clientSocket;
        this.clientNumber = clientNumber;
        serverController = ServerController.getServerController();
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
                int totalBytesRead = 0;
                int bytesRead = 0;
                int throwArray = in.read(buffer, 0, 3);
                while (totalBytesRead < length){
                    bytesRead = in.read(buffer, 0, Math.min(buffer.length, length - totalBytesRead));
                    if (bytesRead == -1) break;  // End of stream
                    totalBytesRead += bytesRead;
                    result.write(buffer, 0, bytesRead);
                }
                ServerObject serverObject = null;
                if (result.size() > 1){
                    serverObject = ServerObjectParser.parse(result.toString(), Optional.of(clientNumber));
                    serverController.handleCommand(this, serverObject);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
