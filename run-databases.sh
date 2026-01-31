# these are scripts that need to be run to start DB, Keycloak and RabbitMq
# DB
docker run -d -p 3306:3306 --name userdb -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=userdb -v userdb_data:/var/lib/mysql mysql
docker run -d -p 3308:3306 --name teamsdb -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=teamsdb -v teamsdb_data:/var/lib/mysql

# Keycloak
docker run -d -p 7080:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:26.5.0 start-dev

# RabbitMq
docker run -it -d --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:4-management