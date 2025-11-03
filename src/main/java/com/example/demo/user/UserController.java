package com.example.demo.user;

import com.example.demo.user.dto.LoginRequest;
import com.example.demo.user.dto.UserCreateRequest;
import com.example.demo.user.dto.UserResponse;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {
	private final UserService userService;
	public UserController(UserService userService) { this.userService = userService; }
	
	@PostMapping("/users")
	public ResponseEntity<Long> create(@Valid @RequestBody UserCreateRequest req) {
	Long id = userService.create(req);
	return ResponseEntity.ok(id);
	}
	
	@GetMapping("/users/{id}")
	public ResponseEntity<UserResponse> findById(@PathVariable Long id) {
	return ResponseEntity.ok(userService.get(id));
	}
	
	@PostMapping("/login")
	public ResponseEntity<String> login(@Valid @RequestBody LoginRequest req) {
	boolean ok = userService.verifyLogin(req.getEmail(), req.getPassword());
	if (ok) return ResponseEntity.ok("LOGIN_OK");
	return ResponseEntity.status(401).body("LOGIN_FAIL");
	}
}