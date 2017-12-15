#!/bin/bash

export DISPLAY=:0.0
xhost +

sudo java -cp ./out/:./out/resources/web/*:./out/lib/*:./out/lib/hummingbird-pi/* com.nerf.turret.NerfTurretServer

xhost -

