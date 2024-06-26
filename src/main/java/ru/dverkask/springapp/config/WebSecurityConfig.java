    package ru.dverkask.springapp.config;

    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.web.SecurityFilterChain;
    import ru.dverkask.springapp.domain.UserEntity;
    import ru.dverkask.springapp.repositories.UserRepository;

    import java.util.Optional;

    @Configuration
    public class WebSecurityConfig {
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.authorizeHttpRequests(authorize -> authorize
                            .requestMatchers("/login").permitAll()
                            .requestMatchers("/admin/**").hasRole("ADMIN")
                            .requestMatchers("/seller/**").hasRole("SELLER")
                            .requestMatchers("/storekeeper/**").hasRole("STOREKEEPER")
                            .anyRequest().authenticated())
                    .formLogin(form -> form
                            .usernameParameter("username")
                            .defaultSuccessUrl("/", false)
                            .permitAll())
                    .logout(LogoutConfigurer::permitAll);

            http.logout(logout -> logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login"));
            return http.build();
        }

        public static UserEntity getUserEntity(UserRepository userRepository) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();
            Optional<UserEntity> user = userRepository.findByUsername(currentPrincipalName);

            return user.orElse(null);
        }

        @Bean
        public BCryptPasswordEncoder bCryptPasswordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }
