package com.techreturners.bookmanager.controller;

import com.techreturners.bookmanager.model.Book;
import com.techreturners.bookmanager.service.BookManagerService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/book")
public class BookManagerController {

    BookManagerService bookManagerService;

    public BookManagerController(BookManagerService bookManagerService) {
        this.bookManagerService = bookManagerService;
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookManagerService.getAllBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping({"/{bookId}"})
    public ResponseEntity<Book> getBookById(@PathVariable Long bookId) {

        Book returnedBook = bookManagerService.getBookById(bookId);

        if (returnedBook.equals(new Book())){ // empty
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("book", "/api/v1/book/" + bookId.toString()+" not found!");
            return new ResponseEntity<>(httpHeaders,HttpStatus.NO_CONTENT);
        }else {
            return new ResponseEntity<>( returnedBook,HttpStatus.OK);
        }
    }

    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody Book book) {

        Book newBook = bookManagerService.insertBook(book);

        HttpHeaders httpHeaders = new HttpHeaders();

        if (newBook.equals(new Book())){ // empty
            httpHeaders.add("book", "/api/v1/book/" + "could not be added !");
            return new ResponseEntity<>(httpHeaders,HttpStatus.NO_CONTENT);
        }else {
            httpHeaders.add("book", "/api/v1/book/" + newBook.getId().toString());
            return new ResponseEntity<>(newBook, httpHeaders, HttpStatus.CREATED);
        }
    }

    //User Story 4 - Update Book By Id Solution
    @PutMapping({"/{bookId}"})
    public ResponseEntity<Book> updateBookById(@PathVariable("bookId") Long bookId, @RequestBody Book book) {

        HttpHeaders httpHeaders = new HttpHeaders();

        if (bookManagerService.getBookById(bookId).equals(new Book())){ // book not found
            httpHeaders.add("book", "/api/v1/book/" + bookId.toString()+" not found!");
            return new ResponseEntity<>(httpHeaders,HttpStatus.NO_CONTENT);
        }else {
            bookManagerService.updateBookById(bookId, book);
            httpHeaders.add("book","/api/v1/book/" +
                                                book.getId().toString()); // extra return info for the user
            return new ResponseEntity<>(bookManagerService.getBookById(bookId), httpHeaders,HttpStatus.OK);
        }
    }

    //Task 1 - Deleting a Book using its ID
    @DeleteMapping({"/{bookId}"})
    public ResponseEntity<Book> deleteBookById(@PathVariable("bookId") Long bookId) {

        HttpHeaders httpHeaders = new HttpHeaders();
        if (bookManagerService.getBookById(bookId).equals(new Book())){ // book not found
            httpHeaders.add("book", "/api/v1/book/" + bookId.toString()+" not found!");
            return new ResponseEntity<>(httpHeaders,HttpStatus.NO_CONTENT);
        } else if (bookManagerService.deleteBookById(bookId)) {
            httpHeaders.add("book", "/api/v1/book/" + bookId.toString() + " deleted successfully!");
            return new ResponseEntity<>(httpHeaders, HttpStatus.OK);
        } else {
            httpHeaders.add("book", "/api/v1/book/" + bookId.toString() + " could not be deleted!");
            return new ResponseEntity<>(httpHeaders, HttpStatus.NO_CONTENT);
        }
    }
}
