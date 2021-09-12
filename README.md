# restbox

**restbox** is designed as simple micro service that exposes REST API for educational purposes. It can be used by future 
software testers to understand REST API basics but also to identify bugs in business logic with tools such as jMeter or 
Postman. Micro service is distributed with in memory SQL relational database, so testing can also research this layer. 
**restbox** exposes really simple REST API connected with movies - business logic is inspired by 
[Filmweb](https://filmweb.pl) (one of the most popular movies catalog in Poland). **restbox** project was created 
exclusively for software testing course blog posts series available on [Para w IT](https://paraw.it).

Exposed by **restbox** REST API operations:
  1. Get movies - returns list of movies including pagination, sorting and filtering.
  2. Get single movie details - returns details about single movie.
  3. Add new movie - add new movie to the catalog based on incoming payload.
  4. Edit existing movie - modifies existing in catalog movie data.
  5. Delete existing movie - deletes all information about movie in catalog.
  6. Get actors - returns list of actors including pagination, sorting and filtering.
  7. Add new actor - add new actor to the catalog based on incoming payload.
  8. Edit existing actor - modifies existing in catalog actor data.
  9. Delete existing actor - deletes all information about actor in catalog.
