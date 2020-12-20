# Project II - SpaceDrop Server
This is the openapi webserver for SpaceDrop. This server works together with the client by using api calls. When users request their account information in the client, a call is made and the server receives the request. The information is then fetched from the database and served back to the client in the form of json. As you can request data from the server, you can also serve up data that is then processed and written to the database. The SpaceDrop server is written in Java. 

## What is currently possible?
Most endpoints are about users or orders:
- `/api/user` Create a new user, creates and returns unique user token
- `/api/login` Log in an existing user, returns unique user token
- `api/details/user` Get your user information
- `/api/userId/user` Get your own user id
- `api/update/user` Update the user details
- `api/users` Get a list of all users [admin]
- `api/order` Create a new order, returns order information
- `api/orders` Get all orders [admin]
- `api/orders/{id}` Get a order information for a specific order through order id
- `api/rockets` Get a list of all the rockets

## How to run the server locally
Make sure to have Java installed. We are using [Azul Zulu 11](https://www.azul.com/downloads/zulu-community/?package=jdk).

Use an editor of choice. Here we will use either Visual Studio Code or Intellij.

Clone the server repository to your computer
- ssh: `git clone git@git.ti.howest.be:TI/2020-2021/s3/project-ii/projects/groep-03/server.git`
- https: `git clone https://git.ti.howest.be/TI/2020-2021/s3/project-ii/projects/groep-03/server.git`

Open the project in your editor of choice, if you are running with vscode, you will need these two plugins:
- [Gradle Tasks](https://marketplace.visualstudio.com/items?itemName=richardwillis.vscode-gradle).
- [Gradle Language Support](https://marketplace.visualstudio.com/items?itemName=naco-siren.gradle-language).

If you are unsure how to install vscode extensions, then have a look at the [VS code documentation](https://code.visualstudio.com/docs/editor/extension-gallery).

Now you can run the gradle task 'run' and your server should be up and running. 
However, to use the server, you will need a working database and set a port.

## Configuring database and setting a port
For the database, we will be using a local H2 database.

For this we need to make a folder in the root `conf` which contains `config.json`. Here we will set a port and configure the database.
```json
{
  "http": {
    "port": 8080
  },
  "db" : {
    "url": "jdbc:h2:~/mars-db",
    "username": "sa",
    "password": "",
    "webconsole.port": 9000
  }
}
```

First a port is set for the api, here this is port 8080. This means that the api will be accessible at `localhost:8080`.
Second, we configure the database. The database is contained in a file on your local system, here `~/mars-db`. Username and password can be set. We also configure a port for the H2 webinterface, here accessible at `localhost:9000`. The standard credentials are `sa` as username with no password. The config file above can be edited to change these credentials to your liking.


## Testing if setup was successful
If everything went well you should now be able to use the api. Start with visiting `localhost:8080/api/message`, this should return "Hello, Mars!".

## Creating database and populating with mock data
To create all necessary tables, there is a database creation script you can find [here](src/test/resources/testdb-create.sql).
To populate the tables with mock data, you can use a population script you can find [here](src/test/resources/testdb-populate.sql).

You can paste the content of these files in your H2 Console and execute all of it, or run the [H2RepositoryTest](src/test/java/be/howest/ti/mars/logic/data/H2RepositoryTest.java).

To make eveything work, a  manual change is required:
- Change the ID of the user `no-user` to `-10`

## Standard Local locations
 - H2 web client
   - localhost:9000
   - url: ~/mars-db
 - Web api
   - localhost:8080/api/