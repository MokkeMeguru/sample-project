resource "google_compute_network" "terraform_network" {
  provider                = google-beta
  name                    = "terraform-network"
  auto_create_subnetworks = false
}

# VPC
resource "google_compute_network" "default" {
  name                    = "cloudrun-network"
  provider                = google-beta
  auto_create_subnetworks = false
}

# VPC access connector
resource "google_vpc_access_connector" "connector" {
  name          = "vpcconn"
  provider      = google-beta
  region        = "asia-northeast1"
  ip_cidr_range = "10.8.0.0/28"
  network       = google_compute_network.terraform_network.name
}
