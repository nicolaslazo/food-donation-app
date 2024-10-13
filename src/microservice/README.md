Acá voy a dejar una guía para explicar cómo hago para levantar el microservicio desde cero. El setup va a variar dependiendo de cómo hosteen su servidor MySQL, su configuración de Hibernate y cómo deciden correr uvicorn.

Primero, levantamos la base de datos desde Docker. El usuario va a ser `root` y la contraseña `mysql`.
```bash
docker run --detach --name=test-mysql -p 3306:3306 --env="MYSQL_ROOT_PASSWORD=mysql" -v /var/run/mysqld:/var/run/mysqld mysql:8
```

Nos conectamos por `mysql` y creamos la base de datos.
```bash
docker exec -it test-mysql mysql -pmysql
mysql> create database prueba;
```

Después levantamos el servidor de Javalin, que va a crear las tablas. La configuración de `persistence.xml` debería ser esta:
```xml
<property name="hibernate.connection.driver_class" value="com.mysql.jdbc.Driver" />
<property name="hibernate.connection.url" value="jdbc:mysql://localhost:3306/prueba" />
<property name="hibernate.connection.username" value="root" />
<property name="hibernate.connection.password" value="mysql" />
<property name="hibernate.dialect" value="org.hibernate.dialect.MySQL8Dialect" />
```

Después llenamos la db con datos de prueba.
```bash
docker exec -i test-mysql mysql -pmysql < seeder.sql
```

Creamos el virtualenv, lo activamos e instalamos las dependencias
```bash
python3 -m venv venv
source venv/bin/activate
pip3 install -r requirements.txt
```

Finalmente, corremos el servidor
```bash
uvicorn main:app --reload
```

Una vez que esté corriendo, se puede consultar la documentación navegando a http://127.0.0.1:8000/docs
