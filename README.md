MERN Repo: https://github.com/saitejakanumuri/BookMyShowClone

I have changed backend framework to use springboot and database to postgres

concepts i worked on:

postgres : gcp cloudSQL
springboot : 3.2.5  
java-jdk: 17
JWT
ControllerAdvice - GlobalExceptionHandlng - (global exception capture/ no need handling in every endpoint.)
Aspects - AOP - (performance monitoring/ helps security injection control)
SecurityConfig - (require for security)
DTO's - lightweight java objects (we configure them before sending to API Response)
Stripe Integration - payment gateway
ResendAPI  - send mails
Jackson


steps to clone and make it work in your laptop

config:
1. In client folder
 create .env
 REACT_APP_STRIPE_PUBLISHABLE_KEY=xx

2. In spring-backend/src/main/resources folder
create application.yml

  spring:
  application:
    name: bookmyshow-backend
  datasource:
    url: ${DATABASE_URL:} 
    username: ${DATABASE_USERNAME:postgres}
    password: ${DATABASE_PASSWORD:}
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        default_schema: public
    open-in-view: false

server:
  port: ${PORT:8081}

app:
  jwt-secret: ${JWT_SECRET:xxx}
  jwt-expiration-ms: 86400000
  frontend-url: ${FRONTEND_URL:http://localhost:3000}
  stripe-key: ${STRIPE_KEY:}
  resend-api-key: ${RESEND_API_KEY:}

My Social Profiles:

LinkedIn: https://www.linkedin.com/in/saitejakanumuri

gmail: saitejakanumuri29565@gmail.com


