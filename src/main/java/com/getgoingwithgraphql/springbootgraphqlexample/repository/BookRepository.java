package com.getgoingwithgraphql.springbootgraphqlexample.repository;

import com.getgoingwithgraphql.springbootgraphqlexample.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, String> {}
