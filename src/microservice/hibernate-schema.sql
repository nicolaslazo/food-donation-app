create table canjeo (
    id bigint not null,
    fecha timestamp not null,
    idColaborador binary(255) not null,
    idRecompensa bigint not null,
    primary key (id)
)

create table colaborador (
    idUsuario binary(255) not null,
    latitud double,
    longitud double,
    primary key (idUsuario)
)

create table contacto (
    tipoContacto varchar(31) not null,
    id bigint not null,
    destinatario varchar(255) not null,
    chatId bigint,
    idUsuario binary(255),
    primary key (id)
)

create table cuidadoHeladera (
    id bigint not null,
    fechaContribucionRealizada timestamp,
    idColaborador binary(255),
    idHeladera bigint,
    primary key (id)
)

create table direccionResidencia (
    idUsuario binary(255) not null,
    barrio varchar(255) not null,
    calle varchar(255) not null,
    ciudad varchar(255) not null,
    codigoPostal varchar(255) not null,
    numeroDeCasa varchar(255) not null,
    pais varchar(255) not null,
    piso varchar(255),
    provincia varchar(255) not null,
    unidad varchar(255),
    primary key (idUsuario)
)

create table donacionDinero (
    id bigint not null,
    fechaContribucionRealizada timestamp,
    idColaborador binary(255),
    frecuenciaEnDias integer,
    monto double,
    primary key (id)
)

create table heladera (
    id bigint not null,
    capacidadEnViandas integer not null,
    fechaInstalacion timestamp not null,
    momentoDeUltimaTempRegistrada timestamp,
    nombre varchar(255) not null,
    latitud double,
    longitud double,
    ultimaTemperaturaRegistradaEnCelsius double,
    idColaborador binary(255) not null,
    primary key (id)
)

create table incidente (
    id bigint not null,
    descripcion varchar(255),
    fecha timestamp not null,
    imagen varchar(255),
    tipo varchar(255) not null,
    colaborador binary(255),
    idHeladera bigint not null,
    primary key (id)
)

create table permiso (
    id binary(255) not null,
    descripcion varchar(255),
    nombre varchar(255) not null,
    primary key (id)
)

create table permisosPorRol (
    idRol binary(255) not null,
    idPermiso binary(255) not null,
    primary key (idRol, idPermiso)
)

create table personaVulnerable (
    id binary(255) not null,
    fechaRegistro timestamp not null,
    menoresACargo integer not null,
    domicilio binary(255),
    idReclutador binary(255) not null,
    primary key (id)
)

create table recompensa (
    id bigint not null,
    costoEnPuntos bigint not null,
    imagen varchar(255),
    nombre varchar(255) not null,
    rubro varchar(255) not null,
    stockInicial integer not null,
    idProveedor binary(255) not null,
    primary key (id)
)

create table rol (
    id binary(255) not null,
    nombre varchar(255) not null,
    primary key (id)
)

create table rolesAsignados (
    idRol binary(255) not null,
    idUsuario binary(255) not null,
    primary key (idRol, idUsuario)
)

create table suscripcion (
    id bigint not null,
    parametro integer,
    tipo varchar(255) not null,
    idColaborador binary(255) not null,
    idHeladera bigint not null,
    primary key (id)
)

create table usuario (
    id binary(255) not null,
    apellido varchar(255) not null,
    contrasenia varchar(255) not null,
    tipo integer,
    valor integer,
    fechaNacimiento date,
    primerNombre varchar(255) not null,
    primary key (id)
)

create table vianda (
    id bigint not null,
    caloriasTotales integer not null,
    descripcion varchar(255) not null,
    fechaCaducidad timestamp not null,
    fechaDonacion timestamp not null,
    pesoEnGramos double not null,
    idColaborador binary(255) not null,
    idHeladera bigint,
    primary key (id)
)

alter table contacto 
    drop constraint UKchher2sppf5fvfno4w6liete

alter table contacto 
    add constraint UKchher2sppf5fvfno4w6liete unique (idUsuario, destinatario)

alter table contacto 
    drop constraint UKf007q0pgaho879fo959gtuylk

alter table contacto 
    add constraint UKf007q0pgaho879fo959gtuylk unique (tipoContacto, destinatario)

alter table contacto 
    drop constraint UK_pigpcs14wrxdygwkg2ga93uhq

alter table contacto 
    add constraint UK_pigpcs14wrxdygwkg2ga93uhq unique (chatId)

alter table cuidadoHeladera 
    drop constraint UK_ohwr9daqw2ydvux6rywy0gm7e

alter table cuidadoHeladera 
    add constraint UK_ohwr9daqw2ydvux6rywy0gm7e unique (id)

alter table cuidadoHeladera 
    drop constraint UK_88oqls36asg8px6ei40jca45h

alter table cuidadoHeladera 
    add constraint UK_88oqls36asg8px6ei40jca45h unique (idHeladera)

alter table donacionDinero 
    drop constraint UK_b593uww3i59tmyna2r3npoewa

alter table donacionDinero 
    add constraint UK_b593uww3i59tmyna2r3npoewa unique (id)

alter table heladera 
    drop constraint UKot00pp42qx2opjsli3juuekyi

alter table heladera 
    add constraint UKot00pp42qx2opjsli3juuekyi unique (latitud, longitud)

alter table permiso 
    drop constraint UK_nwe6lkk7x7sbw94xcmbwgvycu

alter table permiso 
    add constraint UK_nwe6lkk7x7sbw94xcmbwgvycu unique (nombre)

alter table personaVulnerable 
    drop constraint UK_j8h6gr0bsy1jqmirloeyt8tww

alter table personaVulnerable 
    add constraint UK_j8h6gr0bsy1jqmirloeyt8tww unique (domicilio)

alter table rol 
    drop constraint UK_43kr6s7bts1wqfv43f7jd87kp

alter table rol 
    add constraint UK_43kr6s7bts1wqfv43f7jd87kp unique (nombre)

alter table suscripcion 
    drop constraint UKn0qvyl3m27gfrd32y31ne5mdi

alter table suscripcion 
    add constraint UKn0qvyl3m27gfrd32y31ne5mdi unique (idColaborador, idHeladera, tipo)
Hibernate: create sequence hibernate_sequence start with 1 increment by 1

alter table canjeo 
    add constraint FKgh2m05cc5ss1kh1st882ctd4 
    foreign key (idColaborador) 
    references colaborador

alter table canjeo 
    add constraint FKif72wwua8ao3u0ruako6pi2ey 
    foreign key (idRecompensa) 
    references recompensa

alter table colaborador 
    add constraint FKh3hjhrn20gdb0rebgdsw8j9m0 
    foreign key (idUsuario) 
    references usuario

alter table contacto 
    add constraint FKc13k8prkoa8up9qidbgoorwmy 
    foreign key (idUsuario) 
    references usuario

alter table cuidadoHeladera 
    add constraint FKk0v4yp3bvx2ftsnhsvxgeyqa8 
    foreign key (idHeladera) 
    references heladera

alter table cuidadoHeladera 
    add constraint FK_866uonbcf8dqvn7ah4bn6elbu 
    foreign key (idColaborador) 
    references colaborador

alter table direccionResidencia 
    add constraint FK6e7fg1mvl738oe9d8fsxah7og 
    foreign key (idUsuario) 
    references usuario

alter table donacionDinero 
    add constraint FK_i8qkknfnfoh2c1qrf4lgk3yp2 
    foreign key (idColaborador) 
    references colaborador

alter table heladera 
    add constraint FKg8dldp6yyh8a28kbthaou3h21 
    foreign key (idColaborador) 
    references colaborador

alter table incidente 
    add constraint FKbh5m678ul3i3954cd932k0la9 
    foreign key (colaborador) 
    references colaborador

alter table incidente 
    add constraint FKom3l4cw7o9v8khvl759kdbebw 
    foreign key (idHeladera) 
    references heladera

alter table permisosPorRol 
    add constraint FKe8neknhcg4cqili64hebrq62l 
    foreign key (idPermiso) 
    references permiso

alter table permisosPorRol 
    add constraint FK40rt26a6d1ny7jvrtqfev3e02 
    foreign key (idRol) 
    references rol

alter table personaVulnerable 
    add constraint FKb46sde1gk64eanh82k2duhi3x 
    foreign key (domicilio) 
    references direccionResidencia

alter table personaVulnerable 
    add constraint FKa1ud6k161phutd6xj9rjnxubg 
    foreign key (idReclutador) 
    references colaborador

alter table personaVulnerable 
    add constraint FKrtmhk3ewky8481obt8wpvoyb 
    foreign key (id) 
    references usuario

alter table recompensa 
    add constraint FKsepm4fow5g5b0m956iux876gq 
    foreign key (idProveedor) 
    references colaborador

alter table rolesAsignados 
    add constraint FKr28yf72ae43ftjvj1ejwk0oxc 
    foreign key (idUsuario) 
    references rol

alter table rolesAsignados 
    add constraint FKcin1gdlj4deoihhrejrk9lugm 
    foreign key (idRol) 
    references usuario

alter table suscripcion 
    add constraint FKqagl0eni8aklol18ex7k24v5n 
    foreign key (idColaborador) 
    references colaborador

alter table suscripcion 
    add constraint FKcen543tpgo21a0gegvwkj653g 
    foreign key (idHeladera) 
    references heladera

alter table vianda 
    add constraint FKhu3emrqeosy9kr8rf34vw3g3r 
    foreign key (idColaborador) 
    references colaborador

alter table vianda 
    add constraint FKis4sfy7eq1fv20j0j3bh81hww 
    foreign key (idHeladera) 
    references heladera
