package com.example.service;

import org.springframework.stereotype.Service;

import com.example.cache.MyCacheEvict;
import com.example.cache.MyCacheable;
import com.example.dto.BookDto;
import com.example.dto.EditBookDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BookService {

    @MyCacheable(cacheName = "books", keyIndex = 0)
    public BookDto getBookById(final int id) {
        log.info("get by id");
        return BookDto.of(id, "book-" + id);
    }

    @MyCacheEvict(cacheName = "books", keyIndex = 0)
    public void updateBook(final int id, final EditBookDto editBookDto) {
        log.info("Update book: {}. edit request: {}", id, editBookDto);
    }

    @MyCacheEvict(cacheName = "books", fullClean = true)
    public void multipleBookUpdate() {
        log.info("Update multiple books");
    }

}
