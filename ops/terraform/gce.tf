data "google_compute_image" "image" {
  project = "debian-cloud"
  family  = "debian-9"
}

# resource "google_compute_instance" "vm_instance" {
#   name         = "terraform-instance"
#   machine_type = "f1-micro"

#   boot_disk {
#     initialize_params {
#       image = data.google_compute_image.image.self_link
#     }
#   }

#   network_interface {
#     network = google_compute_network.vpc_network.self_link
#     access_config {
#     }
#   }
# }
