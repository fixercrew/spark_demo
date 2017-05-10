# spark_demo

## What is this?
This is a minimal project that shows how to deploy a micro-micro service using Spark as a web framework and using
EBean as a persistence layer


## Prerequisites
- Java 8
- Docker for mac
- Intellij

## For local development
- Start up a test database:
```bash
docker pull postgres
docker run --name some-postgres -p 5432:5432 -e POSTGRES_PASSWORD=<your password here> -d postgres
```

- Create the test table (only need to do this once). You might need to wait a minute for the previous step to have the database ready
```bash
docker run -it --rm --link some-postgres:postgres postgres psql -h postgres -U postgres
create table users (id int, name varchar);
(CTRL-D to exit)
```

- Set up Intellij:
  - Install the "ebean 10.x enhancement plugin"
  - Look under the 'Build' menu and ensure that 'Ebean enhancement' is checked
  - Recompile the app before running it

- Run the app
  - Run through intellij
  - GET http://localhost:4567/users -> You should see an empty 200 response
  - POST http://localhost:4567/users with a payload like below and you should see the object returned to you
```{ 'id': 1, 'name': 'Moe Howard' }```
