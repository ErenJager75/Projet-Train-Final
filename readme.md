# Projet Web Services par HALASSA Samy et JUSSEAUME Jonathan

Le sujet: https://www-inf.telecom-sudparis.eu/SIMBAD/courses/doku.php?id=teaching_assistant:web_services:midterm2021_88

## Pré-requis

- Docker
- Les ports suivants disponibles:
  - 8081
  - 8082
  - 8083
  - 4200
  - 5441
  - 5450
  - 5444

## Lancement du projet

L'exécution de ce projet nécessite l'utilisation de Docker et de Docker-Compose. 
Placez vous à la racine du projet et lancez les commandes suivantes:

````shell
docker image build rest-filter-train/. -t rest-filter-train:1.0.0
````

````shell
docker image build soap-book-train/. -t soap-book-train:1.0.0
````

````shell
docker image build trainvago/. -t trainvago:1.0.0
````

````shell
docker compose up
````

## Utiliser l'application

Vous pouvez accéder au client à l'adresse suivante:
````
http://localhost:4200
````

Vous pouvez également accéder au Swagger
````
http://localhost:8082/swagger-ui/index.html
http://localhost:8083/swagger-ui/index.html
````

Vous pouvez aussi accéder aux WSDL
````
http://localhost:8081/service/trainWsdl.wsdl
http://localhost:8081/service/tokenWsdl.wsdl
http://localhost:8081/service/clientWsdl.wsdl
````
