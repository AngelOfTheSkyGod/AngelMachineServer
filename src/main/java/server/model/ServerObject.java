package server.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
public class ServerObject {
    int command;
    String username;
    String password;
    int deviceNum;
}
