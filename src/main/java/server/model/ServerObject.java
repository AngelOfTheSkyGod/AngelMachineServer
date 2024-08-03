package server.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
public class ServerObject {
    int command;
    String username;
    String password;
    int deviceNum;
}
