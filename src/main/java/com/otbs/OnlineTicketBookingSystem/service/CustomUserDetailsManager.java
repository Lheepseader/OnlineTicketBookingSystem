package com.otbs.OnlineTicketBookingSystem.service;

import com.otbs.OnlineTicketBookingSystem.constant.ExceptionMessage;
import com.otbs.OnlineTicketBookingSystem.entity.User;
import com.otbs.OnlineTicketBookingSystem.exception.ChangePasswordException;
import com.otbs.OnlineTicketBookingSystem.exception.NotFoundResponseException;
import com.otbs.OnlineTicketBookingSystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsManager implements UserDetailsManager {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
            .getContextHolderStrategy();


    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<com.otbs.OnlineTicketBookingSystem.entity.User> user = userRepository.findByEmail(username);
        return user.orElseThrow(() -> new NotFoundResponseException(ExceptionMessage.NO_SUCH_USER, username));
    }


    @Override
    public void createUser(UserDetails user) {
        userRepository.save((User) user);
    }

    @Override
    public void updateUser(UserDetails user) {
        userRepository.save((User) user);
    }

    @Override
    public void deleteUser(String username) {
        userRepository.deleteByEmail(username);
    }

    @Transactional
    @Override
    public void changePassword(String oldPassword, String newPassword) {
        Authentication currentUser = securityContextHolderStrategy.getContext().getAuthentication();
        if (currentUser == null) {
            // This would indicate bad coding somewhere
            throw new AccessDeniedException(
                    "Can't change password as no Authentication object found in context " + "for current user.");
        }
        String email = currentUser.getName();

        int numberOfChanges = userRepository.updatePasswordByEmail(passwordEncoder.encode(newPassword), email);
        if (!(numberOfChanges == 1)) {
            throw new ChangePasswordException(numberOfChanges);
        }
        SecurityContextHolder.clearContext();
    }

    @Override
    public boolean userExists(String username) {
        return userRepository.existsByEmail(username);
    }
}
