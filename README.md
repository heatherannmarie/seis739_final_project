# SEIS 739 Final Project - Safe Spend
This repository contains all the source code for my final project, Safe Spend.\
Safe Spend is an allowance and spending management app that helps parents navigate teaching their children good financial skills in an increasingly digital world.

## Main Features
Parents can create new accounts for themselves, and create accounts for their children.\
Parents can add new chores and store items and edit existing ones, viewable by them and all their children.\
Parents can assign chores to a specific child or keep the chores unassigned.\
Children can mark chores as complete, This notifies their parent, who then approves the completion and completes the allowance transaction.\
Children can select items they wish to buy from the family store.\
Parents and Children can view a list of all their transactions.\

## Technology Stack
Database: MySQL local database\
Backend: Java Spring Boot, using Spring Boot version 3.5.6, Java version 21, and Maven\
Frontend: Angular CLI version 19.2.8\

## Running the Application
Run a MySQL database locally. Change the spring.datasource.url in application.properties. The Tables are defined in the Entity classes and will be automatically generated when you run the project.\
Start the spring boot application by running mvn clean install, then mvn spring-boot:run\
Start the Angular application by running ng serve --proxy-config proxy.conf.json.
Go to localhost:4200/home and view the application!

## Features To Add
A secure authentication system still needs to be added.\
The transaction tables could be improved in a couple ways - they could include the names of the parent and child accounts, and they could display relative to the current user rather than always showing debits and credits from the pov of the child account.\
CSS files in the Angular frontend could be cleaned up and consolidated.\

## System Diagrams

### C4 System Context Diagram
![C4 System Context Diagram](https://imgur.com/6nXTK32)\\
### C4 Container Diagram
![C4 System Container Diagram](https://imgur.com/muYGkOb)\\

### C4 Component/Code Diagram Hybrid of Spring Boot App
![C4 Component/Code Diagram Hybrid of Spring Boot App](https://imgur.com/etNmznA)\\
