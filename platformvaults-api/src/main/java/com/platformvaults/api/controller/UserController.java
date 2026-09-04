package com.platformvaults.api.controller;

import com.platformvaults.common.dto.ApiResponse;
import com.platformvaults.common.dto.UserDto;
import com.platformvaults.core.entity.User;
import com.platformvaults.core.service.UserService;
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
