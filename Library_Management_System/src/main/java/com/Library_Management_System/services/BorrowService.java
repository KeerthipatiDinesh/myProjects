package com.Library_Management_System.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Library_Management_System.models.Book;
import com.Library_Management_System.models.Borrow;
import com.Library_Management_System.models.User;
import com.Library_Management_System.repositories.BookRepository;
import com.Library_Management_System.repositories.BorrowRepository;
import com.Library_Management_System.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class BorrowService {

	@Autowired
	private BorrowRepository borrowRepository;
	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private UserRepository userRepository;

	public Borrow getBorrowById(Long id) {
	    return borrowRepository.findById(id).orElseThrow(() -> new RuntimeException("Borrow record not found!"));
	}

	public User findUserByUsername(String username) {
	    return userRepository.findByUsername(username);
	}

	
	@Transactional
	public String borrowBook(Book book, User user) {
	    List<Borrow> activeBorrows = borrowRepository.findByUserAndReturnedFalse(user);
	    for (Borrow borrow : activeBorrows) {
	        if (borrow.getBook().equals(book)) {
	            return "You have already borrowed this book!";
	        }
	    }

	    if (!book.isAvailable()) {
	        return "Book is not available";
	    }

	    book.setAvailable(false);
	    Borrow borrow = new Borrow();
	    borrow.setBook(book);
	    borrow.setUser(user);
	    borrowRepository.save(borrow);

	    bookRepository.save(book);
	    return "Book borrowed successfully";
	}


	 @Transactional
	    public String returnBook(Borrow borrow) {
	        if (borrow.isReturned()) {
	            return "This book has already been returned!";
	        }

	        Book book = borrow.getBook();
	        book.setAvailable(true);
	        borrow.setReturned(true);

	        bookRepository.save(book);
	        borrowRepository.save(borrow);

	        return "Book returned successfully";
	    }
}
