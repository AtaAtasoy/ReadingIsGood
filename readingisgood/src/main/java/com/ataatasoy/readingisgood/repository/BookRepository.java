package com.ataatasoy.readingisgood.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ataatasoy.readingisgood.models.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}
