package com.platformvaults.core.service;

import com.platformvaults.common.dto.UserDto;
import com.platformvaults.core.entity.User;
import com.platformvaults.core.entity.UserStatus;
import com.platformvaults.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public Optional<User> findByTelegramId(Long telegramId) {
        return userRepository.findByTelegramId(telegramId);
    }

    public boolean existsByTelegramId(Long telegramId) {
        return userRepository.existsByTelegramId(telegramId);
    }

    @Transactional
    public User registerUser(Long telegramId, String username, String firstName,
                             String lastName, String languageCode) {
        return userRepository.findByTelegramId(telegramId)
                .map(user -> {
                    user.setUsername(username);
                    user.setFirstName(firstName);
                    user.setLastName(lastName);
                    user.setLanguageCode(languageCode);
                    user.setStatus(UserStatus.ACTIVE);
                    return userRepository.save(user);
                })
                .orElseGet(() -> {
                    User user = User.builder()
                            .telegramId(telegramId)
                            .username(username)
                            .firstName(firstName)
                            .lastName(lastName)
                            .languageCode(languageCode)
                            .status(UserStatus.ACTIVE)
                            .build();
                    return userRepository.save(user);
                });
    }

    public UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .telegramId(user.getTelegramId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .languageCode(user.getLanguageCode())
                .status(user.getStatus().name())
                .createdAt(user.getCreatedAt().toString())
                .build();
    }
}
