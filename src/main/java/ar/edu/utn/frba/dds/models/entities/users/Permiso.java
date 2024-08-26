package ar.edu.utn.frba.dds.models.entities.users;

import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "permisos")
public record Permiso(
    @Column(unique = true, nullable = false, updatable = false)
    @Id
    @NonNull UUID id,

    @Column(unique = true, nullable = false, updatable = false)
    @NonNull String nombre,

    @Column
    String descripcion,

    @ManyToMany(mappedBy = "permisos")
    @NonNull Set<Rol> rolesQueUsan) {
  public Permiso(String nombre) {
    this(UUID.randomUUID(), nombre, null, new HashSet<>());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Permiso permiso = (Permiso) o;
    return Objects.equals(nombre, permiso.nombre);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nombre);
  }
}
