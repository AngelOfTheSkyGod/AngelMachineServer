package server.controller;
import server.model.ServerObject;
import server.Server;
import server.model.WebClientObject;
import server.socket.ClientSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class ServerController {
    static HashMap<String, ClientSocketHandler> machineToSocketMap = Server.machineToSocketMap;
    static HashMap<String, String> clientToMachineMap = Server.clientToMachineMap;
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
    public static boolean isAuthenticated(WebClientObject webClientObject){
        String machineUsername = webClientObject.getMachineUsername();
        String machinePassword = webClientObject.getMachinePassword();
        String clientUserName = webClientObject.getUsername();
        ClientSocketHandler device = machineToSocketMap.get(machineUsername);
        return machineToSocketMap.containsKey(machineUsername) && Objects.equals(clientToMachineMap.getOrDefault(clientUserName, ""), "") && isCorrectPassword(device, machinePassword);
    }
    public static int handleMachineToClientSetup(WebClientObject webClientObject){
        String machineUsername = webClientObject.getMachineUsername();
        String clientUserName = webClientObject.getUsername();
        if (isAuthenticated(webClientObject)){
            clientToMachineMap.put(clientUserName, machineUsername);
            return 1;
        }
        return 0;
    }

    public static boolean handleSwitchFlip(WebClientObject webClientObject) throws IOException {
        if (!isAuthenticated(webClientObject)){
            return false;
        }
        String machineUsername = webClientObject.getMachineUsername();
        ClientSocketHandler device = machineToSocketMap.get(machineUsername);
        ServerObject commandObject = new ServerObject();
        commandObject.setCommand(0);
        device.getClientController().sendCommand(device, commandObject);
        return true;
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
        }else if (serverObject.getCommand() == 3){
            closeServer();
        }
    }
}
