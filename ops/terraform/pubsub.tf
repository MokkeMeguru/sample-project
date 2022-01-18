resource "google_pubsub_topic" "ask_talk" {
  name                       = "ask_talk"
  message_retention_duration = "86600s"
}
