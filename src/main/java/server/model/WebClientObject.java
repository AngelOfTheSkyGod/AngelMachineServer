package server.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
public class WebClientObject {
    String machineUsername;
    String machinePassword;
    String username;
}
