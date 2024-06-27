package server.controller;

import server.model.ServerObject;
import server.socket.ClientSocketHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientController {
    Socket clientSocket;
    InputStream in = null;
    OutputStream out = null;
    public ClientController(Socket clientSocket, InputStream in, OutputStream out) throws IOException {
        this.clientSocket = clientSocket;
        this.in = in;
        this.out = out;
    }


    public void sendCommand(ClientSocketHandler clientSocketHandler, ServerObject serverObject) throws IOException {
        out.write(serverObject.getCommand());
    }
}
