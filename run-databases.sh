#!/bin/bash

echo "Starting MySQL containers..."

docker run -d -p 3306:3306 --name userdb -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=userdb -v userdb_data:/var/lib/mysql mysql
docker run -d -p 3308:3306 --name teamsdb -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=teamsdb -v teamsdb_data:/var/lib/mysql mysql

echo "Waiting for MySQL containers to initialize..."
sleep 5

echo "MySQL containers are up and running."
