package pl.wat.wcy.server.service;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.wat.wcy.server.dao.User;
import pl.wat.wcy.server.dao.UserType;
import pl.wat.wcy.server.dto.AuthenticationResponse;
import pl.wat.wcy.server.dto.LoginRequest;
import pl.wat.wcy.server.dto.RegisterRequest;
import pl.wat.wcy.server.repository.UserRepository;
import pl.wat.wcy.server.security.JwtProvider;

import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    public boolean signup(RegisterRequest registerRequest) {
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setName(registerRequest.getName());
        user.setType(UserType.CLIENT);
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        Optional<User> foundUser = userRepository.findByEmail(registerRequest.getEmail());
        if (foundUser.isPresent()) {
            return false;
        }

        userRepository.save(user);
        return true;
    }

    @Transactional(readOnly = true)
    public User getCurrentUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return userRepository.findByEmail(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getUsername()));
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication);
        User currentUser = getCurrentUser();
        return AuthenticationResponse.builder()
                .id(currentUser.getId())
                .authenticationToken(token)
                .name(currentUser.getName())
                .email(loginRequest.getEmail())
                .type(currentUser.getType())
                .build();
    }

    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }

    public boolean isCurrentUserAdmin() {
        if (!isLoggedIn()) return false;

        User currentUser;
        try {
            currentUser = getCurrentUser();
        } catch (UsernameNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        return currentUser.getType() == UserType.ADMIN;
    }
}
