package fr.ensiie;

import fr.ensiie.config.CorsConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
public class SoapBookTrainApplication {

    public static void main(String[] args) {
        SpringApplication.run(SoapBookTrainApplication.class, args);
    }

}
