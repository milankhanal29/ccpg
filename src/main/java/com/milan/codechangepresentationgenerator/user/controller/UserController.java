package com.milan.codechangepresentationgenerator.user.controller;
import com.milan.codechangepresentationgenerator.shared.endpoint.EndpointConstants;
import com.milan.codechangepresentationgenerator.user.entity.User;
import com.milan.codechangepresentationgenerator.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @GetMapping(EndpointConstants.GET_BY_ID)
    public User getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }
    @PutMapping("update-user/{userId}")
    public User updateUser(@PathVariable int userId, @RequestBody User user) {
        user.setId(userId);
        return userService.updateUserById(user, userId);
    }
    @GetMapping("/user-details")
    public ResponseEntity<?> getUserDetails(@RequestParam String email) {
        Optional<User> userOptional = userService.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("get-all-user")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
    @GetMapping("/get-userid-by-email")
    public ResponseEntity<?> getUserIdByEmail(@RequestParam String email) {
        int userId = userService.getUserIdByEmail(email);
        return ResponseEntity.ok(userId);
    }
    @GetMapping("/get-user-name/{userId}")
    public String getUserNameById(@PathVariable int userId) {
        return userService.getUserNameById(userId);
    }

    @GetMapping("/get-user-info/by-jwt")
    public User getUSerByJwtToken(@RequestHeader("Authorization") String jwt) throws Exception {
        return userService.findUserByJwtToken(jwt);
    }

}


