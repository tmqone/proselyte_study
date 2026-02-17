package com.tmq.individuals_api.controller;

import com.tmq.individuals_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthControllerV1 {
    private final UserService userService;
}
