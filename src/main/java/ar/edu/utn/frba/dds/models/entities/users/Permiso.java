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
@Table(name = "permiso")
public class Permiso {
  @Column(name = "id", unique = true, nullable = false, updatable = false)
  @Id
  @NonNull UUID id;

  @Column(name = "nombre", unique = true, nullable = false, updatable = false)
  @NonNull String nombre;

  @Column(name = "descripcion")
  String descripcion;

  @ManyToMany(mappedBy = "permisos")
  @NonNull Set<Rol> rolesQueUsan;

  public Permiso(@NonNull UUID id, @NonNull String nombre, String descripcion, @NonNull Set<Rol> rolesQueUsan) {
    this.id = id;
    this.nombre = nombre;
    this.descripcion = descripcion;
    this.rolesQueUsan = rolesQueUsan;
  }

  public Permiso(String nombre) {
    this(UUID.randomUUID(), nombre, null, new HashSet<>());
  }

  protected Permiso() {
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
