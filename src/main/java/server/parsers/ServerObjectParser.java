package server.parsers;

import server.model.ServerObject;

import java.util.Optional;
//  command/n
//  username/n
//  password
// /r/n
public class ServerObjectParser {
    public static ServerObject parse(String clientResponse, Optional<Integer> deviceNum){

        String[] objectFieldArray = clientResponse.split("\n");
        Integer num = deviceNum.orElse(-1);
        return ServerObject.builder()
                .command(Integer.parseInt(objectFieldArray[0].trim()))
                .deviceNum(num)
                .username(objectFieldArray[1].trim())
                .password(objectFieldArray[2].replace("\r", "").trim()).build();
    };

}
