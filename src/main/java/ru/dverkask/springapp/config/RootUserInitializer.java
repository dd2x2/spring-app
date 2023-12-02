package ru.dverkask.springapp.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ru.dverkask.springapp.domain.Role;
import ru.dverkask.springapp.domain.UserEntity;
import ru.dverkask.springapp.repositories.UserRepository;

import java.util.Collections;
import java.util.Optional;

@Configuration
@EnableTransactionManagement
public class RootUserInitializer {
    @Autowired private UserRepository userRepository;
    @Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;
    @PostConstruct
    public void initRootUser() {
        Optional<UserEntity> rootUserOptional = userRepository.findByUsername("root");
        if (rootUserOptional.isEmpty()) {
            UserEntity rootUser = new UserEntity();
            rootUser.setUsername("root");
            rootUser.setPassword(bCryptPasswordEncoder.encode("root"));
            rootUser.setRoles(Collections.singleton(Role.ADMIN));
            userRepository.save(rootUser);
        }
    }
}
