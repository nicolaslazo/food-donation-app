package ar.edu.utn.frba.dds.models.entities.users;

import io.javalin.security.RouteRole;
import lombok.Getter;
import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "permiso")
public class Permiso implements RouteRole, Serializable {
  @Column(name = "id", unique = true, nullable = false, updatable = false)
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NonNull Long id;

  @Column(name = "nombre", unique = true, nullable = false, updatable = false)
  @Getter
  @NonNull String nombre;

  @Column(name = "descripcion")
  String descripcion;

  @ManyToMany(mappedBy = "permisos")
  @NonNull
  Set<Rol> rolesQueUsan;

  public Permiso(@NonNull String nombre, String descripcion, @NonNull Set<Rol> rolesQueUsan) {
    this.nombre = nombre;
    this.descripcion = descripcion;
    this.rolesQueUsan = new HashSet<>(rolesQueUsan);
  }

  public Permiso(@NonNull String nombre) {
    this(nombre, null, new HashSet<>());
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
