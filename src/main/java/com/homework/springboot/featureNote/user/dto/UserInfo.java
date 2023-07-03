package com.homework.springboot.featureNote.user.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserInfo {
    private UserDto userDto;
    private List<String> addresses;
}
