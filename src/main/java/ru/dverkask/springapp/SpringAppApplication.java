package ru.dverkask.springapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.sql.SQLException;

@SpringBootApplication
public class SpringAppApplication {
    public static void main(String[] args) throws SQLException {
        SpringApplication.run(SpringAppApplication.class, args);
    }

}
