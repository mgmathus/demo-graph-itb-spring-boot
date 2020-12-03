package com.itb.demo;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;

import java.util.HashMap;
import java.util.Map;

@Component
public class GraphQLProvider {

    @Autowired
    GraphQLDataFetchers graphQLDataFetchers;

    private GraphQL graphQL;

    @PostConstruct
    public void init() throws IOException {
        URL url = Resources.getResource("schema.graphqls");
        String sdl = Resources.toString(url, Charsets.UTF_8);
        GraphQLSchema graphQLSchema = buildSchema(sdl);
        this.graphQL = GraphQL.newGraphQL(graphQLSchema).build();
    }

    private GraphQLSchema buildSchema(String sdl) {
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(sdl);
        RuntimeWiring runtimeWiring = buildWiring();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
    }

    private RuntimeWiring buildWiring() {
        Map<String, DataFetcher> df = new HashMap<>();
        df.put("films", graphQLDataFetchers.getFilmsDataFetcher());
        return RuntimeWiring.newRuntimeWiring()
                .type(newTypeWiring("Query")
                        .dataFetcher("filmById", graphQLDataFetchers.getFilmByIdDataFetcher()))
                .type(newTypeWiring("Film")
                        .dataFetcher("director", graphQLDataFetchers.getDirectorDataFetcher()))
                .type(newTypeWiring("Director")
                        .dataFetcher("country", graphQLDataFetchers.getCountryDataFetcher()))
                .type(newTypeWiring("Query")
                        .dataFetcher("cast", graphQLDataFetchers.getCastDataFetcher()))
                .type(newTypeWiring("Query")
                        .dataFetchers(df))
                .build();
    }

    @Bean
    public GraphQL graphQL() {
        return graphQL;
    }

}
