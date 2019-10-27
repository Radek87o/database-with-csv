# App user database with CSV upload #

The application provides a function to save users directly from csv file to database. In the process of saving to the database, all file lines
are validated and then mapped to the POJO, and finally mapped to in-memory H2 database. You can communicate with the application using REST API

## Functionalities ##

The application has the following functionalities:
- uploading csv file and mapping its validated lines to the java object and saving to in-memory H2 database. Upload of the consecutive files save the new records to database and does not remove already existing records.
- getting paginated (5 results per page) and sorted by age, records from the database with the option to select page by its number
- getting number of app users
- getting user by its id
- searching users by last name
- getting the oldest user having phone number
- deleting all users or a single by his id

## Tech/frameworks used ##

<img src="https://whirly.pl/wp-content/uploads/2017/05/spring.png" width="200"><img src="http://yaqzi.pl/wp-content/uploads/2016/12/apache_maven.png" width="200">
<img src="https://upload.wikimedia.org/wikipedia/commons/2/2c/Mockito_Logo.png" width="200">
<img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTNkximiwITI1smJcOkn_bx2Zk_RnNKnmDq23Ua26wTVd_YNJcWgw" width="200">
<img src="https://shiftkeylabs.ca/wp-content/uploads/2017/02/JUnit_logo.png" width="200">
<img src="https://jules-grospeiller.fr/media/logo_competences/lang/json.png" width="200">
<img src="https://www.h2database.com/html/images/h2-logo-2.png" width="200">
<img src="https://i2.wp.com/bykowski.pl/wp-content/uploads/2018/07/hibernate-2.png?w=300" width="200">
<img src="http://mapstruct.org/images/mapstruct.png" width="200">

## Instalation ##

* JDK 11
* Apache Maven 3.x

## API ##

Application is available on localhost:8080. Use ```http://localhost:8080/appUsers```:

To test all possibilities REST API, use [Postman](https://www.getpostman.com) or another tool. In order to upload csv file by postman in section [Body]
select ``` form data ``` and then insert name of param in ```key``` [as type - select file] and upload file from the folder 

## Setup Database ##

To use **hibernate**, first configure it on your computer (using H2, PgAdmin or another tool) and in [application.properties](https://github.com/Radek87o/database-with-csv/blob/master/src/main/resources/application.properties)

By default - database is set up as below: 
```
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.url=jdbc:h2:mem:appuserdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.username=sa
spring.datasource.password=```
