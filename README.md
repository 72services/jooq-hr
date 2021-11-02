# jOOQ Example Project

This project uses jOOQ, Spring Boot and FlyWay and generates the meta data from the FlyWay migrations.

## Run PostgreSQL

    docker run --name jooq-hr -d -p5434:5432 -e POSTGRES_DB=hr -e POSTGRES_USER=hr -e POSTGRES_PASSWORD=hr postgres 
