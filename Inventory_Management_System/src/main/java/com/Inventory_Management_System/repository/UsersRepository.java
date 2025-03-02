package com.Inventory_Management_System.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.Inventory_Management_System.model.Users;

@Repository
public class UsersRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final RowMapper<Users> userRowMapper = (rs, rowNum) -> {
		Users user = new Users();
		user.setId(rs.getInt("id"));
		user.setUsername(rs.getString("username"));
		user.setPassword(rs.getString("password"));
		user.setRole(rs.getString("role"));
		return user;
	};

	public UsersRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public Users findByUsername(String username) {
		String sql = "SELECT * FROM users WHERE TRIM(username) = ?";
		try {
			return jdbcTemplate.queryForObject(sql, userRowMapper, username);
		} catch (Exception e) {
			return null;
		}
	}

	public void save(Users user) {
		String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
		jdbcTemplate.update(sql, user.getUsername(), user.getPassword(), user.getRole());
	}
	
	 public boolean existsByUsername(String username) {
	        String sql = "SELECT COUNT(*) FROM users WHERE TRIM(username) = ?";
	        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, username);
	        return count != null && count > 0;
	    }
}
