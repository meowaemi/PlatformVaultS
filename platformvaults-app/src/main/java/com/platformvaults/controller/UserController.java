package com.platformvaults.controller;

import com.platformvaults.dto.ApiResponse;
import com.platformvaults.dto.UserDto;
import com.platformvaults.entity.User;
import com.platformvaults.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{telegramId}")
    public ApiResponse<UserDto> getUserByTelegramId(@PathVariable Long telegramId) {
        Optional<User> user = userService.findByTelegramId(telegramId);
        return user
                .map(u -> ApiResponse.success(userService.toDto(u)))
                .orElseGet(() -> ApiResponse.error("User not found", "Пользователь не найден"));
    }
}
