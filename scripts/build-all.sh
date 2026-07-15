#!/usr/bin/env bash

### Use MavenCentral
./gradlew build

### Use GitHub Packages
if [ "$GITHUB_ACTOR" != "" ]; then
    ./gradlew build -Prepo=github \
        -Pgpr.user=$GITHUB_ACTOR \
        -Pgpr.key=$GITHUB_TOKEN
fi

### Use Jitpack
./gradlew build -Prepo=jitpack