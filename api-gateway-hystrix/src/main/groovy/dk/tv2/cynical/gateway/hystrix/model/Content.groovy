package dk.tv2.cynical.gateway.hystrix.model

class Content {
    Boolean isCircuitBreakerOpen
    Boolean isResponseFromFallback
    Boolean isResponseShortCircuited
    String id
    String api
    String type
    String title
    String description
    String imageURL
    Integer numEpisodes
    Integer numSeasons
    String genre
    Integer episode
    Integer season
}
