package az.ingress.service.impl;

import az.ingress.annotation.ConsoleLog;
import az.ingress.config.JwtService;
import az.ingress.exception.NotFoundException;
import az.ingress.exception.VerificationException;
import az.ingress.mail.MailService;
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

import java.util.Random;
import java.util.Set;

import static az.ingress.enums.ExceptionMessage.INCORRECT_VERIFICATION;
import static az.ingress.enums.ExceptionMessage.PASSWORDS_ARE_NOT_EQUAL;
import static az.ingress.enums.ExceptionMessage.USER_ALREADY_EXIST;
import static az.ingress.enums.ExceptionMessage.USER_ARE_NOT_VERIFIED;
import static az.ingress.enums.ExceptionMessage.USER_NOT_FOUND;

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

    private final MailService mailService;

    @ConsoleLog
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
        user.setVerificationCode(new Random().nextInt(1000, 10000));
        user.setAuthorities(Set.of(getUserAuthority()));
        mailService.sendEmail(user.getUsername(), user.getVerificationCode());
        userRepository.save(user);
    }

    @ConsoleLog
    @Override
    public JwtResponse login(LoginRequest loginRequest) {
        User user = userRepository.findUserByUsername(loginRequest.getEmail())
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND.getMessage().formatted(loginRequest.getEmail())));

        if (!user.isEnabled()) {
            throw new VerificationException(USER_ARE_NOT_VERIFIED.getMessage().formatted(loginRequest.getEmail()));
        }

        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        User principal = (User) authenticate.getPrincipal();
        String accessToken = jwtService.generateAccessToken(principal);
        return new JwtResponse(principal.getUsername(), accessToken);
    }

    @ConsoleLog
    @Override
    public void activate(String email, Integer verificationCode) {
        User user = userRepository.findUserByUsername(email)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND.getMessage().formatted(email)));

        if (!user.getVerificationCode().equals(verificationCode)) {
            throw new VerificationException(INCORRECT_VERIFICATION.getMessage().formatted(verificationCode));
        }

        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setCredentialsNonExpired(true);
        user.setAccountNonLocked(true);
        userRepository.save(user);
    }

    @ConsoleLog
    @Override
    public void resendVerificationCode(String email) {
        User user = userRepository.findUserByUsername(email)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND.getMessage().formatted(email)));

        user.setVerificationCode(new Random().nextInt(1000, 10000));
        mailService.sendEmail(email, user.getVerificationCode());
        userRepository.save(user);
    }

    private Authority getUserAuthority() {
        return authorityRepository.findAuthoritiesByAuthority("USER")
                .orElseGet(() -> authorityRepository.save(Authority.builder().authority("USER").build()));
    }
}
