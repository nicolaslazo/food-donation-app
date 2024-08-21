package ar.edu.utn.frba.dds.models.entities.users;

import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "roles")
public record Rol(
    @Column(unique = true, nullable = false, updatable = false)
    @Id
    @NonNull UUID id,

    @Column(unique = true, nullable = false, updatable = false)
    @NonNull String nombre,

    @ManyToMany
    @JoinTable(
        name = "permisosPorRol",
        joinColumns = @JoinColumn(name = "rolId"),
        inverseJoinColumns = @JoinColumn(name = "permisoId"))
    @NonNull Set<Permiso> permisos) {
  public Rol(String nombre) {
    this(UUID.randomUUID(), nombre, new HashSet<>());
  }

  public Rol(String nombre, Set<Permiso> permisos) {
    this(UUID.randomUUID(), nombre, permisos);
  }

  public boolean tienePermiso(@NonNull Permiso permiso) {
    return permisos.contains(permiso);
  }
}
