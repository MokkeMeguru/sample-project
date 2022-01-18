variable "argocd_location" {
  default = "asia-northeast1-b"
}

resource "google_container_cluster" "app" {
  name                     = "app-cluster"
  location                 = var.argocd_location
  remove_default_node_pool = true
  initial_node_count       = 1

  workload_identity_config {
    workload_pool = "${var.gcp_project}.svc.id.goog"
  }
}

# ArgoCD
resource "google_container_node_pool" "argocd_nodes" {
  name       = "argocd-node-pool"
  location   = var.argocd_location
  cluster    = google_container_cluster.app.name
  node_count = 1

  node_config {
    preemptible  = true
    machine_type = "n1-standard-1"
  }
}
