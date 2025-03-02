package com.Library_Management_System.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Library_Management_System.models.Borrow;
import com.Library_Management_System.models.User;

@Repository
public interface BorrowRepository extends JpaRepository<Borrow, Long> {

	List<Borrow> findByUserAndReturnedFalse(User user);

}
