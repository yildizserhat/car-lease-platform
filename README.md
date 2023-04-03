
# Car Leasing Platform

The Car-lease Platform API is a REST API that enables you to keep client information, vehicle versions, and other broker-related data.

Customer data consists of
Name
Street
Housenumber  
Zipcode
Place
Emailaddress 
Phonenumber

Car data consists of 
Make
Model
Version
Numberofdoors
CO2-emission
Grossprice
Nettprice

Each Car has LeaseRate and it consists of mileage,duration,interestrate.

Lease rate is calculating as following, 

(((mileage/12)*duration)/Nettprice)+(((Interestrate/100)*Nettprice)/12)



## API Reference
Postman Collection provided in the project.


#### Documentation 

```http
  localhost:8080/documentation 
```



## How To Run Application

In order to use the API, you should get JWT token. First, you should call register endpoint. After having JWT, you should call 'authenticate' endpoint. With JWT token, you can be able to call APIs.

## Tech Stack

**Technologies:** Java 17, Spring Boot , Docker, Junit, Mockito,  Integration Test, Spring Security, JWT token, h2 database.


## Features

- Add/Update/Delete Customer 
- Add/Update/Delete Car
- Calculate Lease Rate when saving/updating the car.

