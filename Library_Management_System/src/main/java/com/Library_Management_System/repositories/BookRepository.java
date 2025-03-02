package com.Library_Management_System.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Library_Management_System.models.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

	List<Book> findByAvailable(boolean b);

	List<Book> findByTitleContainingIgnoreCase(String title);

}
