package com.robertgarcia.template.security;


import com.robertgarcia.template.modules.login.LoginView;
import com.robertgarcia.template.modules.users.service.UserService;
import com.vaadin.flow.spring.security.VaadinAwareSecurityContextHolderStrategyConfiguration;
import com.vaadin.flow.spring.security.VaadinSecurityConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@Import(VaadinAwareSecurityContextHolderStrategyConfiguration.class)
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http,
                                            UserService userDetailsService) throws Exception {

        http.rememberMe(rem -> rem
                .rememberMeParameter("remember-me")
                .tokenValiditySeconds(24 * 60 * 60) // 24 horas
                .key("super-secret-key-123"))
                .userDetailsService(userDetailsService);

        return http.with(VaadinSecurityConfigurer.vaadin(), vaadin -> {
            vaadin.loginView(LoginView.class);
        }).build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
