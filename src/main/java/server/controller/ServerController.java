package server.controller;
import server.model.ServerObject;
import server.Server;
import server.socket.ClientSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class ServerController {
    static Server server;

    static {
        try {
            server = Server.getServer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static HashMap<String, ClientSocketHandler> machineToSocketMap = server.getMachineToSocketMap();
    static HashMap<Integer, String> clientToMachineMap = server.getClientToMachineMap();

    public static boolean isCorrectPassword(ClientSocketHandler device, String password){
        return Objects.equals(device.getPassword(), password);
    }

    public static void handleMachineToClientSetup(ClientSocketHandler clientSocketHandler, ServerObject serverObject){
        String machineUsername = serverObject.getUsername();
        Integer clientNum = clientSocketHandler.getClientNumber();
        ClientSocketHandler device = machineToSocketMap.get(machineUsername);
        if (machineToSocketMap.containsKey(machineUsername) && Objects.equals(clientToMachineMap.getOrDefault(clientNum, ""), "") && isCorrectPassword(device, serverObject.getPassword())){
            clientToMachineMap.put(clientSocketHandler.getClientNumber(), serverObject.getUsername());
        }
    }

    public static void handleSwitchFlip(ClientSocketHandler clientSocketHandler, ServerObject serverObject) throws IOException {
        String machineUsername = serverObject.getUsername();
        Integer clientNum = clientSocketHandler.getClientNumber();
        ClientSocketHandler device = machineToSocketMap.get(machineUsername);
        ServerObject commandObject = new ServerObject();
        commandObject.setCommand(0);
        if (machineToSocketMap.containsKey(serverObject.getUsername()) && Objects.equals(clientToMachineMap.getOrDefault(clientNum, ""), machineUsername) && isCorrectPassword(device, serverObject.getPassword())){
            device.getClientController().sendCommand(clientSocketHandler, commandObject);
        }
    }

    public static void setUpMachine(ClientSocketHandler clientSocketHandler, ServerObject serverObject){
        clientSocketHandler.setPassword(serverObject.getPassword());
        machineToSocketMap.put(serverObject.getUsername(), clientSocketHandler);
        System.out.println("machine to socket map: " + machineToSocketMap.toString());
    }

    public static void closeServer(){
        Server.serverState.setOpened(false);
    }

    public static void handleCommand(ClientSocketHandler clientSocketHandler, ServerObject serverObject) throws IOException {
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
