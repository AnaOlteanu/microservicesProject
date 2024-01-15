# Microservices RestAPI project using Spring Cloud architecture
Master's project designed to showcase the implementation of a RestAPI application using the Spring Cloud microservices architecture. It provides the communication between three components (services): Employee, Order and Bonus. An employee is handling an order, while the bonus is used when calculating the salary. 
## Technologies used
- **Spring Cloud Config Server**
- **Feign Rest Client**
- Service discovery -- Naming Server **Eureka**
- Intelligent routing, load balancing –- **Cloud Load Balancer**
- Fault tolerance -- **Resilience4j**, default behavior in case of failure
- Deploy with **Docker** and **Kubernetes**
- **Swagger** documentation
- **Error handling**
- **Hateos** links
- **H2 database**
## Features provided by each component
1. **Employee**
      - attributes: _id, first name, last name, employment date, salary_
  - list of all the employees
  - find employee by id
  - delete employee by id
2. **Order**
      - attributes: _id, details, country, client name, employee id, order date_
  - list all the orders
  - find order by id
  - retrieve details about the employee handling the order with a given id
3. **Bonus**
      - attributes: _month, year, version_
  - it assists in calculating the final salary
  - it is in the form of a percentage
  - can be annually or monthly
  - is created based on the propertied found in the configuration file on the server

## Flow description
For inter-microservice communication is utilized **Spring Cloud Config**. The property files were stored on [GitHub](https://github.com/AnaOlteanu/config_files) and the server facilitated their retrieval and reading, allowing the project to utilize this information. Each service independently accessed and utilized these properties for its specific needs.
As the discovery server is used **Eureka Server**, which retains information about all client-type services. Each microservice registers itself with the Eureka server, providing information about the port and IP address it runs on. Additionally, the Eureka Server communicates with the central server (Config Server).
<br/>
To facilitate the development of client-type web services and ensure load balancing is utilized **Feign Client**. There was a need to use **two** Feign Client **proxies** for two different scenarios:
1. Initially, it was used in the **Employee** component. The goal is that when an employee is requested based on their ID, their **salary** is returned with the calculated **bonus value**. The Employee microservice makes a request to an instance of the **Bonus** microservice from the service discovery layer, discovering two available instances. The Employee microservice can retain the addresses of the instances of the Bonus microservice and apply its own load-balancing strategy.
2. The second time it is used in the **Order** component. The objective is that when an order is returned, the **details of the employee** handling it should also be displayed. The Order microservice makes a request for an instance of the Employee microservice, finding a single available instance.

To handle situations where an instance is not available, it is utilized **Resilience4j**. If an instance for **Bonus** is not available, a **fallback method** will be invoked. This fallback method returns only the **details present in the database**, preventing the occurrence of an _"Internal Server Error"_. Similarly, the case where an instance for **Employee** is unavailable is managed in the same way.

For a more comprehensive presentation of endpoints it is integrated Swagger and incorporated **Hateos** links. Consequently, for each request, informative links are provided.

Deployment was executed using **Docker Hub** and the **Google Cloud Kubernetes Engine**. The initial step involved creating **Docker image**s and pushing them to Docker Hub. These images were then stored in a **container** on **Google Cloud** and configured in the cluster within the Kubernetes Engine.









