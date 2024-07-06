package az.ingress.service;

import az.ingress.model.dto.request.LoginRequest;
import az.ingress.model.dto.request.RegisterRequest;
import az.ingress.model.dto.response.JwtResponse;

public interface UserService {
    void register(RegisterRequest registerRequest);

    JwtResponse login(LoginRequest loginRequest);

    void activate(String email, Integer verificationCode);

    void resendVerificationCode(String email);
}
