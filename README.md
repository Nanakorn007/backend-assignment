***Setup Database***

CREATE DATABASE bookdb;

USE bookdb;

CREATE TABLE books (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    published_date DATE NOT NULL
);

***Connect database to backend project***

Update `src/main/resources/application.YAML`

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/bookdb?useSSL=false&serverTimezone=UTC
    username: root
    password: root

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true

***Instructions for running the server***

- git clone https://github.com/Nanakorn007/backend-assignment.git

- Configure MySQL Database follow ***Setup Database*** and ***Connect database to backend project***

- update maven dependencies

```bash
.\mvnw clean install
```

or

sync all maven projects on intelliJ

- run application

```bash
.\mvnw spring-boot:run
```

or

click run on intelliJ

***Integration tests***

```bash
.\mvnw test
```
or

click run on BookControllerIntegrationTest


***API requests and expected responses***

http://localhost:8080/swagger-ui/index.html#

- GET /books?author=Author X

Request

author Author X

Query Param

author         string       ชื่อผู้แต่ง
pageToken      string       Token สำหรับดึงหน้าถัดไป (ได้จาก nextPageToken ของ response)


Response
{
  "data": [
    {
      "author": "Author X",
      "id": 276,
      "publishedDate": "2020-01-01",
      "title": "Book 1"
    },
    {
      "author": "Author X",
      "id": 277,
      "publishedDate": "2020-01-01",
      "title": "Book 2"
    },
    "..."
  ],
  "nextPageToken": "Mjg1"
}

data           array     รายการหนังสือ (สูงสุด 10 รายการ)
nextPageToken  string    Token สำหรับดึงหน้าถัดไป หาก null แสดงว่าไม่มีหน้าถัดไป

- POST /books

Request

{
  "title": "How to be a good wife",
  "author": "Marry Martin",
  "publishedDate": "2546-05-07"
}

title          string    ชื่อหนังสือ (ห้ามว่าง)
author         string    ชื่อผู้แต่ง
publishedDate  date      วันที่พิมพ์ รองรับทั้งปี ค.ศ. และ พ.ศ. (format: YYYY-MM-DD)

Response

{
  "author": "Marry Martin",
  "id": 17,
  "publishedDate": "2003-05-07",
  "title": "How to be a good wife"
}



