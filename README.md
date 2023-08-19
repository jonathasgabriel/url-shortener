# Simple URL Shortener service

### Scope

This service is able to:

* Shorten a given URL and return shortened link;
* Expand a shortened URL into the original one and redirect to the original location.
* Expire URLs after a configured retention period based on the last access time

This service supports caching of URLs to allow high traffic. Different caching providers can be configured 
through the following types (_env_ variable: _CACHE_TYPE_):

* _redis_ - Redis based caching
* _simple_ - Simple In-Memory caching
* _none_ - No caching

(e.g. Redis, In-Memory).

Check the _application.properties_ file to find out configurations that are available for the app.

### How to run the app locally

* Default profile (this uses MySql database)
  
```mvn spring-boot:run```

* memorydb profile (this uses H2 database)

```mvn spring-boot:run -Dspring-boot.run.profiles=memorydb```

### How to run tests

```mvn test```

### How to run the app using docker
* Navigate to /<project-root>/docker
* Make sure docker daemon is running

```docker-compose up```

This will use **docker-compose.yml** file to set up and start our services (database and app). 
You can add ```-d``` flag to run the process as a daemon.

*Note: you may see that the application is failing to start, is because the database is not ready yet. It will restart until it can run properly.*

*Note 2: check **.env** file for environment variables.*

* To stop services (if running as daemon):

```docker-compose down```

* To apply changes to sources, we need to instruct docker to build again to generate a new image. 

  
  ```docker-compose up --build```

