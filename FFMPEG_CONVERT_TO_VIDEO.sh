#!/bin/bash

if [[ "$#" -eq "1" ]] || [[ "$#" -eq "2" ]]; then
	if [[ "$#" -eq "1" ]]; then
		ffmpeg -i $1 -vf scale=640:480,setdar=4:3 -c:v mjpeg intermediateOutput.mjpeg
	else
		ffmpeg -i $1 -vf scale=640:480,setdar=4:3 -c:v mjpeg -q:v $2 intermediateOutput.mjpeg	
	fi
	
	ffmpeg -i intermediateOutput.mjpeg -c:v copy output.avi
		
else
	echo "Please Enter 1 Arugument (Filename) or 2 Arguments (Filename & Conversion Quality). Quality ranges from 30(Worst) to 2(Best)."
fi



