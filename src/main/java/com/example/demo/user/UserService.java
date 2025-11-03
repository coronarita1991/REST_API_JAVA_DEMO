package com.example.demo.user;

import com.example.demo.user.dto.UserCreateRequest;
import com.example.demo.user.dto.UserResponse;

import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

@Service
public class UserService {
	private final ConcurrentHashMap<Long, InternalUser> store = new ConcurrentHashMap<>();
	private final AtomicLong seq = new AtomicLong(1L);

	public Long create(UserCreateRequest req) {
	boolean exists = store.values().stream()
	.anyMatch(u -> u.email.equalsIgnoreCase(req.getEmail()));
	if (exists) throw new IllegalArgumentException("이미 존재하는 email입니다.");

	Long id = seq.getAndIncrement();
	store.put(id, new InternalUser(id, req.getName(), req.getEmail(), req.getPassword()));
	return id;
	}

	public UserResponse get(Long id) {
	InternalUser u = store.get(id);
	if (u == null) throw new NoSuchElementException("해당 ID의 사용자가 없습니다.");
	return new UserResponse(u.id, u.name, u.email);
	}

	public boolean verifyLogin(String email, String password) {
	return store.values().stream()
	.anyMatch(u -> u.email.equalsIgnoreCase(email) && u.password.equals(password));
	}

	// 간단한 내부 레코드(실무에선 엔티티/암호화/리포지토리 사용)
	private record InternalUser(Long id, String name, String email, String password) {}
}