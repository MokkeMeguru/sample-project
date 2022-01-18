variable "gcp_region" {}
variable "gcp_zone" {}
variable "gcp_project" {
  default = "meguru-playground-dev"
}

provider "google" {
  project = var.gcp_project
  region  = var.gcp_region
  zone    = var.gcp_zone
}

provider "google-beta" {
  project = var.gcp_project
  region  = var.gcp_region
  zone    = var.gcp_zone
}
