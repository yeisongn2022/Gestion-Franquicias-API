terraform {
  required_providers {
    mongodbatlas = {
      source = "mongodb/mongodbatlas"
    }
  }
}

provider "mongodbatlas" {
  public_key  = var.public_key
  private_key = var.private_key
}

# Creación del Proyecto en MongoDB Atlas
resource "mongodbatlas_project" "proyecto" {
  name   = "GestionFranquiciasProject"
  org_id = var.org_id
}

# Creación de un clúster
resource "mongodbatlas_cluster" "cluster" {
  project_id   = mongodbatlas_project.proyecto.id
  name         = "ClusterFranquicias"
  cluster_type = "REPLICASET"

  replication_specs {
    num_shards = 1
    regions_config {
      region_name     = "US_EAST_1"
      electable_nodes = 3
      priority        = 7
      read_only_nodes = 0
    }
  }

  provider_name               = "TENANT"
  backing_provider_name       = "AWS"
  provider_region_name        = "US_EAST_1"
  provider_instance_size_name = "FREE"
}

# Configurar acceso IP
resource "mongodbatlas_project_ip_access_list" "test" {
  project_id = mongodbatlas_project.proyecto.id
  cidr_block = "0.0.0.0/0"
  comment    = "Acceso total para evaluacion tecnica"
}