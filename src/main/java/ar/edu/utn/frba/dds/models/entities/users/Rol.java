package ar.edu.utn.frba.dds.models.entities.users;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "rol")
public class Rol {
  @Getter
  @Column(name = "id", unique = true, nullable = false, updatable = false)
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NonNull Long id;

  @Column(name = "nombre", unique = true, nullable = false, updatable = false)
  @NonNull String nombre;

  @Getter
  @Setter
  @ManyToMany
  @JoinTable(
      name = "permisosPorRol",
      joinColumns = @JoinColumn(name = "idRol", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "idPermiso", referencedColumnName = "id"))
  @NonNull Set<Permiso> permisos;

  public Rol(
      @NonNull String nombre,
      @NonNull Set<Permiso> permisos) {
    this.nombre = nombre;
    this.permisos = new HashSet<>(permisos);
  }

  public Rol(String nombre) {
    this(nombre, new HashSet<>());
  }

  protected Rol() {
  }

  public boolean tienePermiso(@NonNull Permiso permiso) {
    return permisos.contains(permiso);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Rol rol)) return false;
    return Objects.equals(nombre, rol.nombre);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nombre);
  }
}
