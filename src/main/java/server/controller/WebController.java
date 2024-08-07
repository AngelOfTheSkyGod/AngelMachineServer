package server.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import server.model.WebClientObject;


@RestController
public class WebController {

//    @CrossOrigin(origins = "www.quinonesangel.com:3000")
    @GetMapping("/connect")
    public int connect(WebClientObject object) {
        return ServerController.handleMachineToClientSetup(object);
    }
    @GetMapping("/flipSwitch")
    public boolean flipSwitch(WebClientObject object) throws IOException {
        return ServerController.handleSwitchFlip(object);
    }
}
