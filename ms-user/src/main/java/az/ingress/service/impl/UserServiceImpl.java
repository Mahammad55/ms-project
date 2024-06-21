package az.ingress.service.impl;

import az.ingress.config.JwtService;
import az.ingress.model.dto.request.LoginRequest;
import az.ingress.model.dto.request.RegisterRequest;
import az.ingress.model.dto.response.JwtResponse;
import az.ingress.model.entity.Authority;
import az.ingress.model.entity.User;
import az.ingress.exception.AlreadyExistException;
import az.ingress.exception.PasswordIncorrectException;
import az.ingress.mapper.UserMapper;
import az.ingress.repository.AuthorityRepository;
import az.ingress.repository.UserRepository;
import az.ingress.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

import static az.ingress.enums.ExceptionMessage.PASSWORDS_ARE_NOT_EQUAL;
import static az.ingress.enums.ExceptionMessage.USER_ALREADY_EXIST;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final AuthorityRepository authorityRepository;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    @Override
    public void register(RegisterRequest registerRequest) {
        userRepository.findUserByUsername(registerRequest.getEmail()).ifPresent(user -> {
            throw new AlreadyExistException(USER_ALREADY_EXIST.getMessage().formatted(registerRequest.getEmail()));
        });

        if (!registerRequest.getPassword().equals(registerRequest.getRepeatPassword())) {
            throw new PasswordIncorrectException(PASSWORDS_ARE_NOT_EQUAL.getMessage().formatted(registerRequest.getPassword(), registerRequest.getRepeatPassword()));
        }

        User user = userMapper.requestToEntity(registerRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setAuthorities(Set.of(getUserAuthority()));
        userRepository.save(user);
    }

    @Override
    public JwtResponse login(LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        User user = (User) authenticate.getPrincipal();
        String accessToken = jwtService.generateAccessToken(user);
        return new JwtResponse(user.getUsername(), accessToken);
    }

    private Authority getUserAuthority() {
        return authorityRepository.findAuthoritiesByAuthority("USER")
                .orElseGet(() -> authorityRepository.save(Authority.builder().authority("USER").build()));
    }
}
