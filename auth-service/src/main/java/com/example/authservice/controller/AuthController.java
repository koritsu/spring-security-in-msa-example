package com.example.authservice.controller;

import com.example.authservice.config.security.JwtUtil;
import com.example.authservice.controller.dto.LoginRequestDto;
import com.example.authservice.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final RedisService redisService;
    private final JwtUtil jwtTokenUtil;

    @PostMapping("/login")
    public String login(@RequestBody LoginRequestDto loginRequestDto) {
        // TODO: 인증 서버 로그인 시, 토큰 발급 처리

        // 사용자 인증 생성
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.username(), loginRequestDto.password())
        );

        // 인증 성공 시, Redis 저장을 위한 처리를 위해 User를 가져옵니다.
        User user = (User) authentication.getPrincipal();

        // userDto는 redis에 저장하기 위한 중간 dto 입니다.
        var userDto = UserDto.fromUser(user);

        // redis에 저장될 때 user: 라는 prefix와 username을 합쳐 키를 만들고 userDto를 직렬화하여 저장합니다.

        redisService.setValue("user:" + loginRequestDto.username(), userDto);

        // JWT 토큰 생성 후 리턴

        return jwtTokenUtil.createToken(user.getUsername(), user.getAuthorities());

    }

    private record UserDto(String userName, Collection<String> roles) {
        public static UserDto fromUser(User user) {
            return new UserDto(
                    user.getUsername(),
                    user.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toList())
            );
        }
    }
}
