package server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class AngelserverApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(AngelserverApplication.class, args);
		Server server = new Server();
		server.getServerSocketHandler().start();
	}

}
