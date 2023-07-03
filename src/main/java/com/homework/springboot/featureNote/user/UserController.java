package com.homework.springboot.featureNote.user;

import com.homework.springboot.featureNote.user.dto.DeleteUserResponse;
import com.homework.springboot.featureNote.user.dto.SaveUserResponse;
import com.homework.springboot.featureNote.user.dto.UserDto;
import com.homework.springboot.featureNote.user.dto.UserInfo;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@RestController
public class UserController {
    private final UserService userService;
    private final UserValidateService userValidateService;


    @GetMapping("/list")
    public List<UserDto> list() {
       return userService.findAll().stream()
               .map(UserDto::fromUser)
               .collect(Collectors.toList());
    }

    @PostMapping("/save")
    public SaveUserResponse save(@RequestBody UserDto userDto) {
        if (!userValidateService.isEmailValid(userDto.getEmail())) {
            return SaveUserResponse.failed(SaveUserResponse.Error.invalidEmail);
        }
        User user = UserDto.fromDto(userDto);
        userService.save(user);
        return SaveUserResponse.success();
    }

    @PostMapping("/delete/{email}")
    public DeleteUserResponse delete(@PathVariable("email") String email,
                                     HttpServletResponse response) {
        if(!userService.exist(email)) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return DeleteUserResponse.failed(DeleteUserResponse.Error.userNotFound);
        }

        userService.deleteByEmail(email);
        return DeleteUserResponse.success();

    }

    @PostMapping("/deleteAll")
    public DeleteUserResponse deleteAll(@RequestBody List<String> emails) { //["user1@mail","user2@mail"]
        boolean thereAreNotExistEmail = emails == null || emails.stream().anyMatch(e -> !userService.exist(e));

        System.out.println("emails = " + emails);
        if (thereAreNotExistEmail) {
            return DeleteUserResponse.failed(DeleteUserResponse.Error.userNotFound);
        }
//        userService.deleteAll(emails);
        userService.deleteByIds(emails);
        return DeleteUserResponse.success();
    }

    @GetMapping("/search")
    public List<UserDto> search(@RequestParam(name = "query", required = false) String query,
                                HttpServletResponse response) {
        if (!userValidateService.isSearchQueryValid(query)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return Collections.emptyList();
        }

//        return userService.searchQuery(query).stream()
//                .map(UserDto::fromUser)
//                .collect(Collectors.toList());
        return userService.searchJdbc(query).stream()
                .map(UserDto::fromUser)
                .collect(Collectors.toList());
    }
    @GetMapping("/search-emails")
    public List<String> searchEmails(@RequestParam(name = "query", required = false) String query,
                                HttpServletResponse response) {
        if (!userValidateService.isSearchQueryValid(query)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return Collections.emptyList();
        }

        return userService.searchQueryEmails(query);
    }

    @GetMapping("/countPeopleOlderThan/{age}")
    public int countPeopleOlderThan(@PathVariable("age") int age) {
        return userService.countPeopleOlderThan(age);
    }

    @GetMapping("/info/{email}")
    public UserInfo getUserInfo(@PathVariable("email") String email){
        return userService.getUserInfoV2(email);
    }

//    @GetMapping("/between")
//    public List<User> getUsersBetween(@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)  LocalDate start,
//                                      @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
//        return userService.getUsersBetween(start,end);
//    }
    @GetMapping("/between")
    public List<User> getUsersBetween(@RequestParam("start") String start,
                                      @RequestParam("end") String  end) {
        return userService.getUsersBetween(LocalDate.parse(start), LocalDate.parse(end));
    }
}
