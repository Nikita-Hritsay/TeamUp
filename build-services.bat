@echo off

echo Building cards service...
cd cards
call mvn compile jib:build
cd ..

echo Building users service...
cd users
call mvn compile jib:build
cd ..

echo Building teams service...
cd teams
call mvn compile jib:build
cd ..

echo Building eureka-server...
cd eureka-server
call mvn compile jib:build
cd ..

echo All services built.
