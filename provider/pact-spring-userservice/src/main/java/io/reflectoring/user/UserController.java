package io.reflectoring.user;

import io.reflectoring.User;
import io.reflectoring.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserController {

    private UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping(path = "/user-service/users")
    public ResponseEntity<IdObject> createUser(@RequestBody @Valid User user) {
        User savedUser = this.userRepository.save(user);
        return ResponseEntity
                .status(201)
                .body(new IdObject(savedUser.getId()));
    }
}
