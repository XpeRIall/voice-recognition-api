#!/usr/bin/env bash

sbt docker:publishLocal
docker run --rm -p80:8000 voice-recognition-api:0.0.1
