package ar.edu.utn.frba.dds.models.entities.users;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.CascadeType;
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
@Table(name = "rol")
public class Rol {
  @Column(name = "id", unique = true, nullable = false, updatable = false)
  @Id
  @NonNull UUID id;

  @Column(name = "nombre", unique = true, nullable = false, updatable = false)
  @NonNull String nombre;

  @Getter
  @Setter
  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinTable(
      name = "permisosPorRol",
      joinColumns = @JoinColumn(name = "idRol", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "idPermiso", referencedColumnName = "id"))
  @NonNull
  Set<Permiso> permisos;

  public Rol(
      @NonNull UUID id,
      @NonNull String nombre,
      @NonNull Set<Permiso> permisos) {
    this.id = id;
    this.nombre = nombre;
    this.permisos = permisos;
  }

  public Rol(String nombre) {
    this(UUID.randomUUID(), nombre, new HashSet<>());
  }

  public Rol(String nombre, Set<Permiso> permisos) {
    this(UUID.randomUUID(), nombre, permisos);
  }

  protected Rol() {
  }

  public boolean tienePermiso(@NonNull Permiso permiso) {
    return permisos.contains(permiso);
  }
}
