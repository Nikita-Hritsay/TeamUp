#!/bin/bash

echo "Building cards service..."
cd cards
mvn compile jib:build
cd ..

echo "Building users service..."
cd users
mvn compile jib:build
cd ..

echo "Building teams service..."
cd teams
mvn compile jib:build
cd ..

echo "Building eureka-server..."
cd eureka-server
mvn compile jib:build
cd ..

echo "Building eureka-server..."
cd spring-cloud-server
mvn compile jib:build
cd ..

echo "All services have been built successfully."
