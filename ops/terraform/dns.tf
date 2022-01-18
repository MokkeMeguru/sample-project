resource "google_dns_managed_zone" "private-zone" {
  name       = "private-zone"
  dns_name   = "private-function.app.internal."
  visibility = "private"
  private_visibility_config {
    networks {
      network_url = google_compute_network.terraform_network.id
    }
  }
}

resource "google_dns_record_set" "talking_functions" {
  name = "talking.${google_dns_managed_zone.private-zone.dns_name}"
  type = "A"
  ttl  = 300

  managed_zone = google_dns_managed_zone.private-zone.name
  # see. https://medium.com/google-cloud/calling-an-internal-gke-service-from-cloud-functions-2958f9218355:
  # see. k8s -> app-server templates service.yml
  rrdatas = ["10.146.0.18"]
}
