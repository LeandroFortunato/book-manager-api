package com.techreturners.bookmanager.service;

import com.techreturners.bookmanager.model.Book;
import com.techreturners.bookmanager.repository.BookManagerRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookManagerServiceImpl implements BookManagerService {

    BookManagerRepository bookManagerRepository;

    public BookManagerServiceImpl(BookManagerRepository bookManagerRepository) {
        this.bookManagerRepository = bookManagerRepository;
    }

    @Override
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        bookManagerRepository.findAll().forEach(books::add);
        return books;
    }

    @Override
    public Book insertBook(Book book) {

        try {return bookManagerRepository.save(book);
        }
        catch(Exception e){
            return new Book() ;
        }
    }

    @Override
    public Book getBookById(Long id) {
        Book returnBook;
        try {returnBook = bookManagerRepository.findById(id).get();
            return returnBook;
        }
        catch(Exception e){
            return new Book();
        }
    }

    //User Story 4 - Update Book By Id Solution
    @Override
    public void updateBookById(Long id, Book book) {
        Book retrievedBook = bookManagerRepository.findById(id).get();

        retrievedBook.setTitle(book.getTitle());
        retrievedBook.setDescription(book.getDescription());
        retrievedBook.setAuthor(book.getAuthor());
        retrievedBook.setGenre(book.getGenre());

        bookManagerRepository.save(retrievedBook);
    }

    //Task 1 - Deleting a Book using its ID
    @Override
    public boolean deleteBookById(Long id) {

        try {bookManagerRepository.delete(bookManagerRepository.findById(id).get());
             return true;
        }
        catch(Exception e){
            return false;
        }
     }
}
