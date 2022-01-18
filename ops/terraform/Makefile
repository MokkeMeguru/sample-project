##
# Terraform setup
#
# @file
# @version 0.1

.PHONY: plan
plan:
	terraform plan

.PHONY: lint
lint:
	tflint

.PHONY: apply
apply: lint
	terraform apply

.PHONY: auto-apply
auto-apply: lint
	terraform apply -auto-approve

.PHONY: destroy
destroy:
	terraform destroy
# end
