# Lab 2 - Paketera tjänster

## Syfte
Syftet med denna lab är att få en återblick i hur man paketerar en tjänst med hjälp av docker samt hur man kan starta flera saker samtidigt med hjälp av docker compose.

## Föregående labb
Under föregående lab skapade vi en backendtjänst med flernivåarkitektur. Bygg vidare på denna tjänst i denna labb. Om du inte har utfört föregående labb kan du hitta ett lösningsförslag i detta repo som du kan använda dig av i denna labb.

## Övergripande målbild
Efter slutförd lab ska din applikation vara:
* paketerad med docker
* pushad till ett remote registry (dockerhub)
* hela din app ska gå att starta via docker compose (service + databas) 

## Ramverk
* Docker
* Docker compose

# Del 1: Docker

## Docker basics

### Skriva docker fil
Vi ska titta lite på hur man skriver en enkel dockerfil. Detta är det mesta du behvöver veta för att komma igång. Det går att göra mycket mer avancerande saker med dessa är ganska nischade till specifika use case.
En docker fil börjar alltid med `FROM <image>` vilket säger vilken bas-image du vill använda. Här kan du tex säga att du vill köra ubuntu eller centos eller så kan du använda mer specifika och säga att min bas-image ska vara java11 och på så sätt få en miljö uppsatt för just java.
på hub.docker.com kan du söka och leta efter relevanta images. Du kan ofta välja mellan olika alternativ. Tex java11 vs java11-alpine eller java11-slim. Det kan enklast sammanfattas som "Helt fullständing preppad image med all och lite till" eller "Detta är det minsta möjliga för att köra en java-app" Allt och lite till kan vara nice om du vill felsöka saker men vi försöker göra den minsta möjliga i produktion för att minska starttider etc.

Sista raden i en docker fil är ofta `CMD <cmd>` som beskriver vad som ska köras när när containern startas. tex i java har vi `CMD ["java", "-jar", "./app.jar"]`

Där emellan kan vi köra massor av saker. De två främsta är: 
`RUN <something>` där du kan exekvera saker i terminalen. Här kan vi tex installera program, modifiera filer etc.
`Add <something>` där du kan kopiera in filer från din lokala maskin till imagen. Tex i java vill vi oftast kopiera in den byggda jar-filen.

#### Uppgift 1 (Optional om du vill återminnas saker från tidigare kurser)
* Skapa en fil a.txt med innehållet `Hello, <name>!` (`echo "Hello, <name>!" > a.txt`)
* Skapa en Dockerfile där du lägger in filen `ADD <something>`
* I dockerfilen byt ut <name> till ditt namn `RUN <something>` Här kan man tex använda sig av sed `sed -i s/\<name\>/dan/g a.txt`
* När docker imagen körs ska den skriva ut innehållet i filen och sedan dö.

För att bygga skriver man `docker build -t lab2-docker-basics .`
För att köra skriver man `docker run --rm --name lab2-docker-basics -t lab2-docker-basics`
För att komma in i dockerimagen om du tex vill testa att köra lite kommandon kan du skriva `docker exec -it lab2-docker-basics bash`.


## Uppgift 2: Dockerize your app
Ett tips är att använda `openjdk:11` som basimage.

Din app körs men den kan inte komma åt din databas? 

1. Docker körs i ett eget nätverk och din databas är på localhost
2. Vi kan använda docker compose för att starta allt i samma nätverk

# Uppgift 3: docker-compose basics
Docker compose är ett bra verktyg när du vill starta flera saker samtidigt som ska kunna prata med varandra. Det avänds oftast inte i produktion men du hittar det nästan överallt på utvecklares lokala maskiner för att sätta upp en lokal testmiljö.
I docker compose beskriver man vad man vill starta med hjälp av en yaml-fil. Det går att göra väldigt mycket i docker compose, men det vi ska fokusera på är främst hur vi sätter upp en enklare utvecklingsmiljö.

En docker compose fil har följande struktur
```
version: "3.9"
services:
  <a name for the service>:
    image: <image name>
    environment:
      - <variable=value>
      - <variable=value>
    ports:
      - "from:to"
    links:
      - <name of the service you want to have started before this>
  <a name for the service>:
    image: <image name>
    ...
    ...
```

Som ett första steg så vill vi starta mysql via docker compose och vi använder det som ett ett sätt att lära oss hur det fungerar.
Fyll i templaten ovan för att starta mysql. Från lab1 lärde vi oss att vi kunde starta mysql via docker med följande kommando `docker run --rm -p"3306:3306" --name mysql -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=lab1 -d mysql:8.0.26`

För att starta allt som du har specifierat skriver du `docker-compose up`. 
För stt stoppa allt som du har specifierat skriver du `docker-compose stop`.
Du kan även starta och stoppa individuella tjänster genom att skriva `docker-compose up mysql` etc.


## Uppgift 4: Docker-compose your app!
Titta i din `application.properties` här finns det lite saker förberett `spring.datasource.url=jdbc:mysql://${MYSQL_HOST:localhost}:3306/lab1
` alla ställen som använder databasen har dessa kryptiska saker. Det betyder, använd environment variablen MYSQL_HOST och om den inte finns, defaulta till localhost.

Dvs vi kan nu i docker-compose sätta den variabeln till dess rätta värde. I docker-compose får alla tjänster ett domännamn som är samma som dess namn. I detta fall är det `mysql`.

Lägg till din app i docker-compose.yml och se till att allt fungerar så som det ska!


## Uppgift 5: Push your image!
Skapa ett konto på dockerhub om du inte redan har det (tror ni gjorde detta under Application Lifecyle Kursen)
Skapa ett repo "bank".

Om du inte redan har döpt din docker-image till `<repo>/bank:1.0.0`

kan du välja mellan
1. kör docker build igen och döp den rätt
2. tagga om din redan byggda image med `docker tag <image-name> <repo>/bank:1.0.0`

Därefter kan du pusha din image med docker push `<repo>/bank:1.0.0`

Kolla så att den kom dit den skulle!
