#!/bin/bash

rm -rf ./out
mkdir ./out


javac -cp ./lib/*:./lib/hummingbird-pi/*:./src/main/java/* -d ./out ./src/main/java/com/nerf/turret/Servo.java ./src/main/java/com/nerf/turret/Turret.java ./src/main/java/com/nerf/turret/ControlResource.java ./src/main/java/com/nerf/turret/NerfTurretServer.java ./src/main/java/com/nerf/turret/ServoTest.java ./src/main/java/com/nerf/turret/AutoResource.java ./src/main/java/com/nerf/turret/TurretResource.java  ./src/main/java/com/nerf/turret/TriggerResource.java ./src/main/java/com/nerf/turret/AutoResource.java ./src/main/java/com/nerf/turret/ControlModeResource.java ./src/main/java/com/nerf/turret/VelocityResource.java ./src/main/java/com/nerf/facerecognition/Camera.java ./src/main/java/com/nerf/facerecognition/DetectResult.java ./src/main/java/com/nerf/facerecognition/FaceRecognitionService.java

cp -r ./src/main/resources/. ./out
cp -r ./lib ./out