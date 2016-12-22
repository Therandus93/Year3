package cw3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebCw3Application {

	public static void main(String[] args) {
		System.setProperty("db_host", "localhost");
        System.setProperty("db_port", "3306");
        System.setProperty("db_name", "kbs7");
        System.setProperty("db_user", "kbs7");
        System.setProperty("db_pass", "fektowni");
		
		SpringApplication.run(WebCw3Application.class, args);
	}
}
