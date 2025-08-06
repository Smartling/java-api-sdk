#!/usr/bin/env bash

set -e

echo "Checking if we should release"
if ! ./mvnw -Prelease conventional-commits:validate; then
    echo "No changes to release"
    exit 0
fi

./mvnw -B -Prelease -DskipTests -Darguments='-DskipTests' -s .circleci/settings.xml conventional-commits:version release:prepare

# Get the tag created by release:prepare
TAG=$(git describe --tags --abbrev=0)
echo "Deploying tag: $TAG"

# Checkout the tagged version
git checkout $TAG

# Deploy using central-publishing-maven-plugin
./mvnw -B -Prelease -DskipTests -s .circleci/settings.xml clean deploy
