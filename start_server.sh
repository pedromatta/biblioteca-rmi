#!/bin/bash

cd ./Server
LOCAL_IP=$(ip route get 1.1.1.1 | awk '/src/ {print $7}')
rm ./RMI/*.class 
javac -encoding utf8 ./RMI/*.java
LC_ALL=C.UTF-8 java RMI.BibliotecaServer "$LOCAL_IP"
