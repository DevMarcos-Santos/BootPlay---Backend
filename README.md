Album Store with Spotify Integration - Backend

Welcome to the backend repository of our Album Store application with Spotify API integration! This project is part of our final project and aims to provide a platform for selling music albums, taking advantage of Spotify's API functionalities for accessing information and playing tracks.

Step by step to build the project:

- IMPORTANT: First check if the name of the root folder is "marcos-santos-backend". If not, change it.
  
- Install the .jar files for both projects. The "app-integration-api" and "app-user-api"

![jar](https://github.com/bc-fullstack-04/marcos-santos-backend/assets/166733231/1d01a0f7-0673-44dd-9baa-f803cff4038d)

- Execute o comando "docker-compose -f docker-compose.yml build" para que as imagens do docker sejam criadas.
  
![docker-build](https://github.com/bc-fullstack-04/marcos-santos-backend/assets/166733231/b54fa87c-3315-4df9-952d-0512e0c81ba7)

- Now run the "docker-compose up" command so that the container is initialized. With this, the application will be launched through Docker.
  
![docker-up](https://github.com/bc-fullstack-04/marcos-santos-backend/assets/166733231/59d65a76-a3a3-4a5c-93cf-38beb54caba3)

Acess swagger:

http://localhost:8081/api/swagger-ui/index.html#/

http://localhost:8082/api/swagger-ui/index.html#/

IMPORTANT: If you want to use the application without Docker, simply pause the services through Docker Desktop and start the applications using the IDE used.
