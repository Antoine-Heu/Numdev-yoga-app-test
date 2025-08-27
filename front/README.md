# Yoga - Frontend

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 14.1.0.

## Prerequisites

- Node.js (version 14 or higher)
- npm (Node Package Manager)
- MySQL database
- Backend application running (see backend README)

## Start the project

Git clone:

> git clone https://github.com/OpenClassrooms-Student-Center/P5-Full-Stack-testing

Go inside folder:

> cd front

Install dependencies:

> npm install

Launch Front-end:

> npm run start

Application will be accessible at: `http://localhost:4200`


## Ressources

### Mockoon env 

### Postman collection

For Postman import the collection

> ressources/postman/yoga.postman_collection.json 

by following the documentation: 

https://learning.postman.com/docs/getting-started/importing-and-exporting-data/#importing-data-into-postman


### MySQL

SQL script for creating the schema is available `ressources/sql/script.sql`

By default the admin account is:
- login: yoga@studio.com
- password: test!1234


### Test

#### E2E

Launching e2e test:

> npm run e2e

Generate coverage report (you should launch e2e test before):

> npm run e2e:coverage

Report is available here:

> front/coverage/lcov-report/index.html

#### Unitary test

Launching test:

> npm run test

for following change:

> npm run test:watch

#### Unit Test Coverage

Unit test coverage is included when running jest tests.

## Additional Commands

### Linting

Run ESLint to check code quality:

> npm run lint

### Cypress Commands

Open Cypress test runner:

> npm run cypress:open

Run Cypress tests in headless mode:

> npm run cypress:run

## Technologies

- Angular 14.2.0
- Angular Material
- RxJS
- Jest (unit testing)
- Cypress (E2E testing)
- TypeScript
