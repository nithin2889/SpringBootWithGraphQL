package com.getgoingwithgraphql.springbootgraphqlexample.service.datafetchers;

import com.getgoingwithgraphql.springbootgraphqlexample.model.Book;
import com.getgoingwithgraphql.springbootgraphqlexample.repository.BookRepository;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookDataFetcher implements DataFetcher<Book> {

  @Autowired
  BookRepository bookRepository;

  @Override
  public Book get(DataFetchingEnvironment dataFetchingEnvironment) {
    String isn = dataFetchingEnvironment.getArgument("id");
    return bookRepository.getOne(isn);
  }
}
