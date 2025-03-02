package com.Library_Management_System.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.Library_Management_System.models.Book;
import com.Library_Management_System.repositories.BookRepository;

@Service
public class BookService {

	@Autowired
	private BookRepository bookRepository;

	public List<Book> getAvailableBooks() {
	    return bookRepository.findByAvailable(true);
	}

	public Page<Book> getPaginatedBooks(PageRequest pageRequest) {
	    return bookRepository.findAll(pageRequest);
	}

	public Book getBookById(Long id) {
	    return bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found!"));
	}

	
	public List<Book> findAllBooks() {
		return bookRepository.findAll();
	}

	public Book addBook(Book book) {
		return bookRepository.save(book);
	}

	public void deleteBook(Long id) {
		bookRepository.deleteById(id);
	}

	public Book updateBook(Long id, Book bookDetails) {
		Optional<Book> opBook = bookRepository.findById(id);
		if (opBook.isPresent()) {
			Book book = opBook.get();
			book.setTitle(bookDetails.getTitle());
			book.setAuthor(bookDetails.getAuthor());
			book.setGenre(bookDetails.getGenre());
			book.setAvailable(false);

			return bookRepository.save(book);

		}
		return null;
	}

	public List<Book> searchBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }
}
