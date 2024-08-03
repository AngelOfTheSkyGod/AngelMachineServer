package server.controller;
import server.model.ServerObject;
import server.Server;
import server.socket.ClientSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class ServerController {
    static Server server = null;
    static HashMap<String, ClientSocketHandler> machineToSocketMap = null;
    static HashMap<Integer, String> clientToMachineMap = null;
    static ServerController serverController = null;

    public ServerController() throws IOException {
        server = Server.getServer();
        System.out.println("server retrieved");
        machineToSocketMap = server.getMachineToSocketMap();
        System.out.println("machine to socket map");
        clientToMachineMap = server.getClientToMachineMap();
        System.out.println("client to machine map");
    }

    public static ServerController getServerController() throws IOException {
        if (serverController == null){
            System.out.println("making new server controller");
            serverController = new ServerController();
        }
        System.out.println("returning: " + serverController);
        return serverController;
    }

    public boolean isCorrectPassword(ClientSocketHandler device, String password){
        return Objects.equals(device.getPassword(), password);
    }

    public void handleMachineToClientSetup(ClientSocketHandler clientSocketHandler, ServerObject serverObject){
        String machineUsername = serverObject.getUsername();
        Integer clientNum = clientSocketHandler.getClientNumber();
        ClientSocketHandler device = machineToSocketMap.get(machineUsername);
        if (machineToSocketMap.containsKey(machineUsername) && Objects.equals(clientToMachineMap.getOrDefault(clientNum, ""), "") && isCorrectPassword(device, serverObject.getPassword())){
            clientToMachineMap.put(clientSocketHandler.getClientNumber(), serverObject.getUsername());
        }
    }

    public void handleSwitchFlip(ClientSocketHandler clientSocketHandler, ServerObject serverObject) throws IOException {
        String machineUsername = serverObject.getUsername();
        Integer clientNum = clientSocketHandler.getClientNumber();
        ClientSocketHandler device = machineToSocketMap.get(machineUsername);
        ServerObject commandObject = new ServerObject();
        commandObject.setCommand(0);
        if (machineToSocketMap.containsKey(serverObject.getUsername()) && Objects.equals(clientToMachineMap.getOrDefault(clientNum, ""), machineUsername) && isCorrectPassword(device, serverObject.getPassword())){
            device.getClientController().sendCommand(clientSocketHandler, commandObject);
        }
    }

    public void setUpMachine(ClientSocketHandler clientSocketHandler, ServerObject serverObject){
        clientSocketHandler.setPassword(serverObject.getPassword());
        machineToSocketMap.put(serverObject.getUsername(), clientSocketHandler);
        System.out.println("machine to socket map: " + machineToSocketMap.toString());
    }

    public void closeServer(){
        Server.serverState.setOpened(false);
    }

    public void handleCommand(ClientSocketHandler clientSocketHandler, ServerObject serverObject) throws IOException {
        System.out.println("command: " + serverObject);
        if (serverObject.getCommand() == 0){
            setUpMachine(clientSocketHandler, serverObject);
        }else if (serverObject.getCommand() == 1){
            handleMachineToClientSetup(clientSocketHandler, serverObject);
        }else if (serverObject.getCommand() == 2){
            handleSwitchFlip(clientSocketHandler, serverObject);
        }else if (serverObject.getCommand() == 3){
            closeServer();
        }
    }
}
