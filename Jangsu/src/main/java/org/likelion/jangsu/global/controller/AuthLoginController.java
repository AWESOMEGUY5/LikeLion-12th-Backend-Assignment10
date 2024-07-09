package org.likelion.jangsu.global.controller;

import lombok.RequiredArgsConstructor;
import org.likelion.jangsu.common.error.ErrorCode;
import org.likelion.jangsu.global.dto.Token;
import org.likelion.jangsu.global.service.AuthLoginService;
import org.likelion.jangsu.user.domain.User;
import org.likelion.jangsu.user.domain.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;


@RestController
@RequiredArgsConstructor
@RequestMapping("/login/oauth2")
public class AuthLoginController {
    private final AuthLoginService authLoginService;
    private final UserRepository userRepository;

    /*
        @GetMapping("/code/{registrationId}")
        public void googleLogin(@RequestParam String code, @PathVariable String registrationId) {
            authLoginService.socialLogin(code, registrationId);
        }
    */
    @GetMapping("/test")
    public User test(Principal principal) {
        Integer id = Integer.parseInt(principal.getName());

        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(ErrorCode.USER_NOT_SIGNED_UP.getMessage()));
    }

    /*
        @GetMapping("/code/{registrationId}")
        public void googleLogin(@RequestParam String code, @PathVariable String registrationId) {
            authLoginService.socialLogin(code, registrationId);
        }
    */
    @GetMapping("/code/google")
    public Token googleCallBack(@RequestParam(name = "code") String code) {
        String googleAccessToken = authLoginService.getGoogleAccessToken(code);
        return loginOrSignup(googleAccessToken);
    }

    public Token loginOrSignup(String googleAccessToken) {
        return authLoginService.loginOrSignUp(googleAccessToken);
    }
}
