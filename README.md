# FeedApp_Group1
The goal of the project is to develop a running prototype of a full-stack FeedApp. The prototype is implemented using a three-layer architecture that separates user presentation, core business logic, and data management. The presentation layer is handled by the Svelte-based Single Page Application, which delivers the interactive user interface. The business logic layer is implemented in Spring Boot and built using Gradle, providing domain processing, authentication, and REST endpoints. The data management tier includes the PostgreSQL relational database for persistent storage, Redis as a cache, and RabbitMQ for asynchronous event distribution. This separation makes it easier to extend or modify individual parts of the system.


##To run the FeedApp application

- Clone the Git repository
- Build and start the Docker containers:
    - `docker compose up`
- Or, if you want to build the images from code:
        - `docker compose up --build`
- Open [http://localhost:8081](http://localhost:8081) to interact with FeedApp

