#!/bin/bash

if [ "$TRAVIS_REPO_SLUG" == "Blazebit/blaze-storage" ] && 
    [ "$TRAVIS_BRANCH" == "master" ] &&
    [ "$TRAVIS_PULL_REQUEST" == "false" ] &&
    [ "$TRAVIS_JDK_VERSION" == "openjdk7" ]; then

  echo "Starting snapshot deployment..."
  mvn -s .travis-settings.xml -DperformRelease -DskipTests -Dgpg.skip=true deploy
  echo "Snapshots deployed!"

else
  echo "Skipping snapshot deployment..."
fi
