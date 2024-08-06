package server.controller;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import server.Server;
import server.model.ServerObject;
import server.socket.ClientSocketHandler;


@RestController
public class WebController {

    static HashMap<String, ClientSocketHandler> machineToSocketMap = Server.machineToSocketMap;
    static HashMap<Integer, String> clientToMachineMap = Server.clientToMachineMap;
    @CrossOrigin(origins = "www.quinonesangel.com:3000")
    @GetMapping("/connect")
    public int connect(ServerObject object) {
        return ServerController.handleMachineToClientSetup(object);
    }

}
