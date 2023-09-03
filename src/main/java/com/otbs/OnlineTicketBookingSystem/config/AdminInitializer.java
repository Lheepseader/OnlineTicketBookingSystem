package com.otbs.OnlineTicketBookingSystem.config;

import com.otbs.OnlineTicketBookingSystem.entity.Role;
import com.otbs.OnlineTicketBookingSystem.entity.User;
import com.otbs.OnlineTicketBookingSystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AdminInitializer {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String password;

    @EventListener(ContextRefreshedEvent.class)
    public void initAdminUser() {
        Optional<User> optionalUser = userRepository.findByRole(Role.ADMIN);

        if (optionalUser.isEmpty()) {
            User adminUser = new User();
            adminUser.setRole(Role.ADMIN);
            adminUser.setEmail(adminEmail);
            adminUser.setPassword(passwordEncoder.encode(password));

            userRepository.save(adminUser);
        }
    }

}
