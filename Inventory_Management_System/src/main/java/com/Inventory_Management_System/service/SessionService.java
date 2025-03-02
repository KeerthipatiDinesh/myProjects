package com.Inventory_Management_System.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;

@Service
public class SessionService {
	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	public boolean isUserLoggedIn(String username) {
		return redisTemplate.hasKey("user_session:" + username);
	}

	public void handleLogin(String username, HttpSession session) {
		String userSessionKey = "user_session:" + username;

		String existingSessionId = redisTemplate.opsForValue().get(userSessionKey);
		if (existingSessionId != null) {
			redisTemplate.delete("session:" + existingSessionId);
		}

		redisTemplate.opsForValue().set(userSessionKey, session.getId());
		redisTemplate.opsForValue().set("session:" + session.getId(), username);

		redisTemplate.expire(userSessionKey, 5, TimeUnit.MINUTES);
		redisTemplate.expire("session:" + session.getId(), 5, TimeUnit.MINUTES);

	}

	public void handleLogout(HttpSession session) {
		String sessionId = session.getId();
		String username = redisTemplate.opsForValue().get("session:" + sessionId);

		if (username != null) {
			redisTemplate.delete("user_session:" + username);
			redisTemplate.delete("session:" + sessionId);
		}

		session.invalidate();
	}
}
