package com.kma.specification.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.kma.specification.domain.dto.BookDto;
import com.kma.specification.domain.entities.BookEntity;
import com.kma.specification.domain.type.BookStatus;

@SpringBootTest
class BookServiceTest {

    @MockBean
    private DateProvider dateProvider;
    @Autowired
    private BookService bookService;

    @Test
    void shouldReturnNewestOnlyBooks() {
        final LocalDateTime currentDateTime = LocalDateTime.of(2021, 4, 1, 0, 0, 0);
        when(dateProvider.getCurrentDateTime()).thenReturn(currentDateTime);

        assertThat(bookService.getNewestBook())
            .allMatch(book -> book.getCreatedTimestamp().isAfter(currentDateTime))
            .allMatch(book -> book.getStatus() == BookStatus.ACTIVE)
            .extracting(BookDto::getId)
            .containsExactly(1, 5);
    }

    @Test
    void shouldSelectBooksWithCriteriaApi() {
        final LocalDateTime currentDateTime = LocalDateTime.of(2020, 4, 1, 0, 0, 0);
        when(dateProvider.getCurrentDateTime()).thenReturn(currentDateTime);

        assertThat(bookService.searchBooksCriteriaApi(null, null))
            .allMatch(book -> book.getCreatedTimestamp().isAfter(currentDateTime))
            .extracting(BookEntity::getId)
            .containsExactly(2, 3, 4, 5);
    }

    @Test
    void rsqlTest() {
        assertThat(bookService.rsqlSearch(0, 3, "authors.id=in=(2)", "category.id,desc"))
            .extracting(BookDto::getId)
            .containsExactly(2, 5);
    }

}