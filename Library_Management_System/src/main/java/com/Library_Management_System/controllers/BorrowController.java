package com.Library_Management_System.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Library_Management_System.models.Book;
import com.Library_Management_System.models.Borrow;
import com.Library_Management_System.models.User;
import com.Library_Management_System.services.BookService;
import com.Library_Management_System.services.BorrowService;

@RestController
@RequestMapping("/user")
public class BorrowController {

	@Autowired
	private  BorrowService borrowService;
	@Autowired
    private  BookService bookService;

    @GetMapping("/available")
    public List<Book> viewAvailableBooks() {
        return bookService.getAvailableBooks();
    }
    
    @GetMapping("/search")
    public List<Book> searchBooks(@RequestParam String title) {
        return bookService.searchBooksByTitle(title);
    }

    @PostMapping("/{bookId}")
    public String borrowBook(@PathVariable Long bookId, @AuthenticationPrincipal UserDetails userDetails) {
        User user = borrowService.findUserByUsername(userDetails.getUsername());
        Book book = bookService.getBookById(bookId);

        return borrowService.borrowBook(book, user);
    }

    @PostMapping("/return/{borrowId}")
    public String returnBook(@PathVariable Long borrowId) {
        Borrow borrow = borrowService.getBorrowById(borrowId);
        return borrowService.returnBook(borrow);
    }
}
