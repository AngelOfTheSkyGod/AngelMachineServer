package server.controller;
import server.model.ServerObject;
import server.Server;
import server.model.ServerState;
import server.socket.ClientSocketHandler;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Objects;

public class ServerController {
    static Server server = null;
    static HashMap<Integer, ClientSocketHandler> machineToSocketMap = null;
    static HashMap<Integer, Integer> clientToMachineMap = null;

    public ServerController() throws IOException {
        server = Server.getServer();
        machineToSocketMap = server.getMachineToSocketMap();
        clientToMachineMap = server.getClientToMachineMap();
    }

    public static boolean isCorrectPassword(ClientSocketHandler device, String password){
        return Objects.equals(device.getPassword(), password);
    }

    public static void handleMachineToClientSetup(ClientSocketHandler clientSocketHandler, ServerObject serverObject){
        Integer deviceNum = serverObject.getDeviceNum();
        Integer clientNum = clientSocketHandler.getClientNumber();
        ClientSocketHandler device = machineToSocketMap.get(deviceNum);
        if (machineToSocketMap.containsKey(deviceNum) && Objects.equals(clientToMachineMap.getOrDefault(clientNum, -1), -1) && isCorrectPassword(device, serverObject.getPassword())){
            clientToMachineMap.put(clientSocketHandler.getClientNumber(), deviceNum);
        }
    }

    public static void handleSwitchFlip(ClientSocketHandler clientSocketHandler, ServerObject serverObject) throws IOException {
        Integer deviceNum = serverObject.getDeviceNum();
        Integer clientNum = clientSocketHandler.getClientNumber();
        ClientSocketHandler device = machineToSocketMap.get(deviceNum);
        ServerObject commandObject = new ServerObject();
        commandObject.setCommand(0);
        if (machineToSocketMap.containsKey(deviceNum) && Objects.equals(clientToMachineMap.getOrDefault(clientNum, -1), clientNum) && isCorrectPassword(device, serverObject.getPassword())){
            device.getClientController().sendCommand(clientSocketHandler, commandObject);
        }
    }
    public static void handleCommand(ClientSocketHandler clientSocketHandler, ServerState serverState, ServerObject serverObject) throws IOException {
        if (serverObject.getCommand() == 0){
            clientSocketHandler.setPassword(serverObject.getPassword());
            server.getMachineToSocketMap().put(serverObject.getDeviceNum(), clientSocketHandler);
        }else if (serverObject.getCommand() == 1){
            handleMachineToClientSetup(clientSocketHandler, serverObject);
        }else if (serverObject.getCommand() == 2){
            handleSwitchFlip(clientSocketHandler, serverObject);
        }else if (serverObject.getCommand() == 3){
            Server.serverState.setOpened(false);
        }
    }
}
