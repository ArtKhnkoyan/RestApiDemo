package com.example.demo.rest;

import com.example.demo.model.Author;
import com.example.demo.model.Book;
import com.example.demo.repository.AuthorRepository;
import com.example.demo.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/authors")
@AllArgsConstructor
public class AuthorEndPoint {

    private AuthorRepository authorRepository;
    private BookRepository bookRepository;

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody Author author) {
        authorRepository.save(author);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<Author>> findAll() {
        return ResponseEntity.ok(authorRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Author> findById(@PathVariable long id) {
        Optional<Author> findById = authorRepository.findById(id);
        if (!findById.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(findById.get());
    }

    @GetMapping("/{authorId}/books")
    public ResponseEntity<List<Book>> findBooksByAuthorId(@PathVariable long authorId) {
        return ResponseEntity.ok(bookRepository.findAllByAuthorId(authorId));
    }

    @PutMapping
    public ResponseEntity update(@RequestBody Author author) {
        Optional<Author> repoAuthor = authorRepository.findById(author.getId());
        if (!repoAuthor.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Optional<Author> mapAuthor = repoAuthor.map(a -> {
            a.setName(author.getName());
            a.setSurname(author.getSurname());
            a.setAge(author.getAge());
            return a;
        });
        authorRepository.save(mapAuthor.get());
        return ResponseEntity.ok().body("author is update");
    }

    @DeleteMapping
    public ResponseEntity delete(@RequestBody Author author) {
        Optional<Author> repoAuthor = authorRepository.findById(author.getId());
        if (!repoAuthor.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        authorRepository.delete(author);
        return ResponseEntity.ok().body("author is delete");
    }
}
