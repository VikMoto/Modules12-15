package com.homework.springboot.featureNote.user.dto;

import com.homework.springboot.featureNote.user.Gender;
import com.homework.springboot.featureNote.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserDto {
    private String email;
    private String fullName;
    private LocalDate birthday;
    private int age;
    private Gender gender;

    public static UserDto fromUser(User user) {
        int age =(int) ChronoUnit.YEARS.between(user.getBirthday(), LocalDate.now());
        return UserDto.builder()
                .email(user.getEmail())
                .fullName(user.getFullName())
                .birthday(user.getBirthday())
                .age(age)
                .gender(user.getGender())
                .build();
    }

    public static User fromDto(UserDto userDto) {
        return User.builder()
                .email(userDto.getEmail())
                .fullName(userDto.getFullName())
                .birthday(userDto.getBirthday())
                .gender(userDto.getGender())
                .build();
    }
}
