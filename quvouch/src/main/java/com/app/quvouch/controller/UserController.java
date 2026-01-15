package com.app.quvouch.controller;


import com.app.quvouch.dtos.RegisterRequest;
import com.app.quvouch.dtos.UserDto;
import com.app.quvouch.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody RegisterRequest register)
    {
        UserDto createdUser = userService.createUser(register);

        return ResponseEntity.status(201).body(createdUser);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers()
    {
        List<UserDto> allUsers =  userService.getAllUser();

        return ResponseEntity.ok(allUsers);

    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email)
    {
        UserDto userByEmail = userService.getUserByEmail(email);

        return ResponseEntity.ok(userByEmail);
    }

    @GetMapping("/id/{userId}")
    public ResponseEntity<UserDto> getUserId(@PathVariable UUID userId)
    {
        UserDto userById = userService.getUserById(userId);

        return ResponseEntity.ok(userById);
    }

    @GetMapping("/name/{username}")
    public ResponseEntity<UserDto> getUserByName(@PathVariable String username)
    {
        UserDto userByName = userService.getUserByName(username);
        return ResponseEntity.ok(userByName);
    }

    @DeleteMapping("{userid}")
    public ResponseEntity<String> deleteUser(@PathVariable UUID userid)
    {
        String s = userService.deleteUser(userid);
        return ResponseEntity.ok(s);
    }

    @PatchMapping("{userId}")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto,@PathVariable UUID userId)
    {
        UserDto updatedUser = userService.updateUser(userId, userDto);
        return ResponseEntity.ok(updatedUser);
    }
}
