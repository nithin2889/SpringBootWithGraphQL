package com.getgoingwithgraphql.springbootgraphqlexample.service;

import com.getgoingwithgraphql.springbootgraphqlexample.model.Book;
import com.getgoingwithgraphql.springbootgraphqlexample.repository.BookRepository;
import com.getgoingwithgraphql.springbootgraphqlexample.service.datafetchers.AllBooksDataFetcher;
import com.getgoingwithgraphql.springbootgraphqlexample.service.datafetchers.BookDataFetcher;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class GraphQLService {

  @Autowired BookRepository bookRepository;

  @Value("classpath:books.graphql")
  Resource resource;

  private GraphQL graphQL;
  @Autowired private AllBooksDataFetcher allBooksDataFetcher;
  @Autowired private BookDataFetcher bookDataFetcher;

  // load schema at application start up
  @PostConstruct
  private void loadSchema() throws IOException {
    val schemaParser = new SchemaParser();
    val schemaGenerator = new SchemaGenerator();

    // load books into the book repository
    loadDataIntoHSql();
    // get the schema
    File schemaFile = resource.getFile();
    // parse schema
    TypeDefinitionRegistry typeRegistry = schemaParser.parse(schemaFile);
    RuntimeWiring wiring = buildRuntimeWiring();
    GraphQLSchema schema = schemaGenerator.makeExecutableSchema(typeRegistry, wiring);
    graphQL = GraphQL.newGraphQL(schema).build();
  }

  private void loadDataIntoHSql() {
    Stream.of(
            new Book(
                "123",
                "Book Of Clouds",
                "Kindle Edition",
                new String[] {"Chloe Aridjis"},
                "Nov 2017"),
            new Book(
                "124",
                "Cloud Arch & Engineering",
                "Oreilly",
                new String[] {"Peter", "Sam"},
                "Jan 2015"),
            new Book(
                "125",
                "Java 9 Programming",
                "Oreilly",
                new String[] {"Venkat", "Ram"},
                "Dec 2016"))
        .forEach(book -> bookRepository.save(book));
  }

  private RuntimeWiring buildRuntimeWiring() {
    return RuntimeWiring.newRuntimeWiring()
        .type(
            "Query",
            typeWiring ->
                typeWiring
                    .dataFetcher("allBooks", allBooksDataFetcher)
                    .dataFetcher("book", bookDataFetcher))
        .build();
  }

  public GraphQL getGraphQL() {
    return graphQL;
  }
}
