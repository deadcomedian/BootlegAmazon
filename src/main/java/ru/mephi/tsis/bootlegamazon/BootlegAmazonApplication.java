package ru.mephi.tsis.bootlegamazon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import ru.mephi.tsis.bootlegamazon.dao.entities.StatusEntity;
import ru.mephi.tsis.bootlegamazon.dao.repositories.StatusRepository;

import java.util.Optional;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class BootlegAmazonApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootlegAmazonApplication.class, args);
    }

}
