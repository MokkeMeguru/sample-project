##
# AppServer
#
# @file
# @version 0.1
BUILD_HASH = $(shell git rev-parse HEAD:sourceDirectory HEAD:file.py | git hash-object --stdin)
PROJECT ?= meguru-playground-dev

.PHONY: build
build:
	docker build -t gcr.io/${PROJECT}/app-server:${BUILD_HASH} . -f ./build/server/Dockerfile
	docker push gcr.io/${PROJECT}/app-server:${BUILD_HASH}
# kubectl --namespace app port-forward app-server-65c47bd75c-xt5hl 8080:8888
# end
