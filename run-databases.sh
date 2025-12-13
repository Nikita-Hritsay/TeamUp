#!/bin/bash

echo "Starting MySQL containers..."

docker run -d -p 3306:3306 --name userdb  -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=userdb  mysql
docker run -d -p 3308:3306 --name teamsdb -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=teamsdb mysql

echo "Waiting for MySQL containers to initialize..."
sleep 5

echo "MySQL containers are up and running."
