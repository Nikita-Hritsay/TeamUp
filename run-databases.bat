@echo off
echo Starting MySQL containers if not already running...

REM ====== cardsdb ======
docker ps -a --filter "name=cardsdb" --filter "status=running" --format "{{.Names}}" | findstr /i "^cardsdb$" >nul
if errorlevel 1 (
    docker rm -f cardsdb >nul 2>&1
    docker run -d -p 3307:3306 --name cardsdb -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=cardsdb mysql
    echo Started cardsdb
) else (
    echo cardsdb already running
)

REM ====== teamsdb ======
docker ps -a --filter "name=teamsdb" --filter "status=running" --format "{{.Names}}" | findstr /i "^teamsdb$" >nul
if errorlevel 1 (
    docker rm -f teamsdb >nul 2>&1
    docker run -d -p 3308:3306 --name teamsdb -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=teamsdb mysql
    echo Started teamsdb
) else (
    echo teamsdb already running
)

REM ====== userdb ======
docker ps -a --filter "name=userdb" --filter "status=running" --format "{{.Names}}" | findstr /i "^userdb$" >nul
if errorlevel 1 (
    docker rm -f userdb >nul 2>&1
    docker run -d -p 3306:3306 --name userdb -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=userdb mysql
    echo Started userdb
) else (
    echo userdb already running
)

echo Waiting for containers to initialize...
timeout /t 1 >nul

echo MySQL containers are up and running.
