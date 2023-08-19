FROM maven:3.8.6-amazoncorretto-17

WORKDIR /url-shortener
COPY . .
RUN mvn clean install

CMD mvn spring-boot:run