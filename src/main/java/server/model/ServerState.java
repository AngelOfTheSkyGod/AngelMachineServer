package server.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServerState {
    boolean opened;
    public ServerState(){
        opened = true;
    }
}