##
# AppServer
#
# @file
# @version 0.1


GCP_PROJECT ?= meguru-playground-dev

ZONE ?= asia-northeast1-b
CLUSTER ?= app-cluster
NAMESPACE ?= app
NAME ?= app-server

.PHONY: lint
lint:
	helm lint .

.PHONY: env
env:
	gcloud container clusters get-credentials ${CLUSTER} --zone ${ZONE} --project ${GCP_PROJECT}
	helm repo update

.PHONY: env apply
apply:
	if [ `kubectl get ns -o=name | grep namespace/${NAMESPACE} | wc -l` -eq 0 ]; then make helm-install; else make helm-upgrade; fi

.PHONY: env helm-install
helm-install:
	kubectl create ns ${NAMESPACE}
	helm install ${NAME} . --namespace ${NAMESPACE}

.PHONY: env helm-upgrade
helm-upgrade:
	helm upgrade ${NAME} . --namespace ${NAMESPACE}

.PHONY: env helm-delete
helm-delete:
	helm delete ${NAME} --namespace ${NAMESPACE}
	kubectl delete ns ${NAMESPACE}

.PHONY: env helm-diff
helm-diff:
	helm diff upgrade ${NAME} . -q --allow-unreleased --namespace ${NAMESPACE} -f values.yaml

# end
