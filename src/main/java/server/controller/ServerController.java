package server.controller;
import server.model.ServerObject;
import server.Server;
import server.socket.ClientSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class ServerController {
    static HashMap<String, ClientSocketHandler> machineToSocketMap = Server.machineToSocketMap;
    static HashMap<Integer, String> clientToMachineMap = Server.clientToMachineMap;
    static ServerController serverController = null;

    public static ServerController getServerController() throws IOException {
        if (serverController == null){
            serverController = new ServerController();
        }
        return serverController;
    }

    public static boolean isCorrectPassword(ClientSocketHandler device, String password){
        return Objects.equals(device.getPassword(), password);
    }

    public static int handleMachineToClientSetup(ServerObject serverObject){
        String machineUsername = serverObject.getUsername();
        Integer clientNum = serverObject.getDeviceNum();
        ClientSocketHandler device = machineToSocketMap.get(machineUsername);
        if (machineToSocketMap.containsKey(machineUsername) && Objects.equals(clientToMachineMap.getOrDefault(clientNum, ""), "") && isCorrectPassword(device, serverObject.getPassword())){
            clientToMachineMap.put(clientNum, serverObject.getUsername());
            return 1;
        }
        return 0;
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
        System.out.println("command: " + serverObject.toString());
        if (serverObject.getCommand() == 0){
            setUpMachine(clientSocketHandler, serverObject);
        }else if (serverObject.getCommand() == 1){
            handleMachineToClientSetup(serverObject);
        }else if (serverObject.getCommand() == 2){
            handleSwitchFlip(clientSocketHandler, serverObject);
        }else if (serverObject.getCommand() == 3){
            closeServer();
        }
    }
}
