#!/bin/bash

raspivid -cd MJPEG -w 800 -h 600 -fps 10 -t 0 -n -o - | ./camera-service
