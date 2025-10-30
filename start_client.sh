#!/bin/bash

cd ./Client
rm -f ./RMI/*.class 
javac -encoding utf8 ./RMI/*.java
LC_ALL=C.UTF-8 java RMI.BibliotecaClient
