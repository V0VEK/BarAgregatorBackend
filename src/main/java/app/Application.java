package app;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

@SpringBootApplication
public class Application {


    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
    }
}
