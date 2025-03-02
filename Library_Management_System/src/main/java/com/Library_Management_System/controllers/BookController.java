package com.Library_Management_System.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Library_Management_System.models.Book;
import com.Library_Management_System.services.BookService;

@RestController
@RequestMapping("/admin")
public class BookController {

	@Autowired
	private BookService bookService;

	@GetMapping
	public Page<Book> getBooks(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		return bookService.getPaginatedBooks(PageRequest.of(page, size));
	}

	@GetMapping("/search")
	public List<Book> searchBooks(@RequestParam String title) {
		return bookService.searchBooksByTitle(title);
	}

	@PostMapping("/addbook")
	public Book addBook(@RequestBody Book book) {
		return bookService.addBook(book);
	}

	@PutMapping("/{id}")
	public Book updateBook(@PathVariable Long id, @RequestBody Book bookDetails) {
		Book updatedBook = bookService.updateBook(id, bookDetails);
		if (updatedBook != null) {
			return updatedBook;
		} else {
			throw new RuntimeException("Book with ID " + id + " not found");
		}
	}

	@DeleteMapping("/{id}")
	public void deleteBook(@PathVariable Long id) {
		bookService.deleteBook(id);
	}
}
