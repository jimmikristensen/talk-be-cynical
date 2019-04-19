package dk.tv2.cynical.gateway.graphql

import dk.tv2.cynical.gateway.datafetcher.ContentDataFetcher
import dk.tv2.cynical.gateway.datafetcher.RecommendationDataFetcher
import dk.tv2.cynical.gateway.model.Content

import graphql.GraphQL
import graphql.TypeResolutionEnvironment
import graphql.schema.GraphQLObjectType
import graphql.schema.TypeResolver
import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser
import graphql.schema.idl.TypeDefinitionRegistry
import groovy.transform.CompileStatic
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.core.io.ResourceResolver


import javax.inject.Singleton

@Factory
@CompileStatic
class GraphQLFactory {

    @Bean
    @Singleton
    GraphQL graphQL(ResourceResolver resourceResolver, RecommendationDataFetcher helloDataFetcher, ContentDataFetcher hello2DataFetcher) {

        def schemaParser = new SchemaParser()
        def schemaGenerator = new SchemaGenerator()

        // Parse the schema.
        def typeRegistry = new TypeDefinitionRegistry()
        typeRegistry.merge(schemaParser.parse(new BufferedReader(new InputStreamReader(
                resourceResolver.getResourceAsStream("classpath:schema.graphqls").get()))))

        // Create the runtime wiring.
        def runtimeWiring = RuntimeWiring.newRuntimeWiring()
                .type("Query", {
                    typeWiring -> typeWiring
                    .dataFetcher('editorialContent', hello2DataFetcher)
                    .dataFetcher("recommendationContent", helloDataFetcher)
                })
                .type("Content", {
                    typeWiring -> typeWiring
                    .typeResolver(getTypeResolver())
                })
                .build()

        // Create the executable schema.
        def graphQLSchema = schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring)

        // Return the GraphQL bean.
        return GraphQL.newGraphQL(graphQLSchema).build()

    }

    private TypeResolver getTypeResolver() {
        return new TypeResolver() {
            @Override
            GraphQLObjectType getType(TypeResolutionEnvironment env) {
                Content content = env.getObject()
                if (content.type == 'Movie') {
                    return env.getSchema().getObjectType("Movie")
                } else if (content.type == 'Series') {
                    return env.getSchema().getObjectType("Series")
                } else {
                    return env.getSchema().getObjectType("Episode")
                }
            }
        }
    }
}
