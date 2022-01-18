##
# Talking App-Server
#
# @file
# @version 0.1
PROJECT ?= meguru-playground-dev
RUNTIME ?= python39
REGION ?= asia-northeast1
FUNCTION_NAME := talking_app_server
VPC_CONNECTOR := vpcconn
TRIGGER_TOPIC := ask_talk

deploy:
	gcloud beta functions deploy ${FUNCTION_NAME} \
		--region ${REGION} \
		--runtime ${RUNTIME} \
		--trigger-topic ${TRIGGER_TOPIC} \
		--project ${PROJECT} \
		--vpc-connector projects/${PROJECT}/locations/${REGION}/connectors/${VPC_CONNECTOR}

destroy:
	gcloud beta functions delete ${FUNCTION_NAME} \
		--region ${REGION}

sample-message:
	gcloud pubsub topics publish ask_talk --message="hello"
# end
