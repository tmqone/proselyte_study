package com.tmq.service;

import com.tmq.dto.auth.LoginAuthRequest;
import com.tmq.dto.auth.LoginAuthResponse;
import com.tmq.dto.auth.RegisterAuthRequest;
import com.tmq.dto.auth.RegisterAuthResponse;
import com.tmq.dto.user.CreateUserRequest;
import com.tmq.dto.user.CreateUserResponse;
import com.tmq.dto.user.FindUserWithAllFields;
import com.tmq.exception.AuthException;
import com.tmq.exception.UserExistsException;
import com.tmq.exception.UserNotFoundException;
import com.tmq.model.Role;
import com.tmq.util.BCryptUtil;
import com.tmq.util.JwtUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthService {
    private static final AuthService INSTANCE = new AuthService();
    private static final UserService userService = UserService.getInstance();

    public static AuthService getInstance() {
        return INSTANCE;
    }

    public LoginAuthResponse login (LoginAuthRequest loginAuthRequest) {
        try {
            FindUserWithAllFields user = userService.findByNameWithPassword(loginAuthRequest.username());
            if (BCryptUtil.checkPassword(user.password(), loginAuthRequest.password())){
                return new LoginAuthResponse(loginAuthRequest.username(), JwtUtil.createToken(user.id(), user.role().toString()));
            } else {
                throw new AuthException();
            }
        } catch (UserNotFoundException e){
            throw new AuthException("Неправильный логин или пароль");
        }
    }

    public RegisterAuthResponse register (RegisterAuthRequest registerAuthRequest) {
        if (userService.isUserExistByUsername(registerAuthRequest.username())) throw new UserExistsException();
        CreateUserResponse user = userService.save(new CreateUserRequest(registerAuthRequest.username(), registerAuthRequest.password(), Role.USER));
        return new RegisterAuthResponse(registerAuthRequest.username(), JwtUtil.createToken(user.id(), "USER"));
    }
}
