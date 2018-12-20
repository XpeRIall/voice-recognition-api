#!/usr/bin/env bash

sbt docker:publishLocal
docker run --rm -p8000:9999 voice-recognition-api:0.0.1
