#!/usr/bin/env bash
# see. https://github.com/tonsky/uberdeps
# how to run?
# -> java -cp target/project.jar clojure.main -m app-server.main
set -e
cd "$( dirname "${BASH_SOURCE[0]}"  )"
clojure -M -m uberdeps.uberjar --deps-file ../deps.edn --target ../target/project.jar
