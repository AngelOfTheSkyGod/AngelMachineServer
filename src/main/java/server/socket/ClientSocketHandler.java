package server.socket;

import lombok.Getter;
import lombok.Setter;
import server.Server;
import server.controller.ClientController;
import server.model.ServerObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

@Getter
@Setter
public class ClientSocketHandler extends Thread{

    ServerObject serverObject;
    Integer clientNumber;
    String password;
    Socket clientSocket;
    ClientController clientController;
    ClientSocketHandler(Socket clientSocket, int clientNumber) throws IOException {
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
                for (int i = 0; i < 1024; i++){
                    if (buffer[i] == '0'){
                        break;
                    }
                    System.out.println(buffer[i]);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
