# LiterAlura

LiterAlura es un catálogo interactivo de libros desarrollado en Java. Esta aplicación de consola permite buscar, registrar y consultar información de libros y autores desde la API Gutendex, almacenando los datos en una base de datos PostgreSQL. Diseñada como parte de un desafío de aprendizaje, LiterAlura es ideal para explorar tecnologías back-end modernas como Java, Spring y PostgreSQL.

---

## Características principales

1. **Buscar libros por título**

   - Consultar la API Gutendex.
   - Registrar el libro obtenido (con título, autor, idioma y descargas) en la base de datos.
   - Evitar duplicados al registrar libros.

2. **Listar libros registrados**

   - Mostrar todos los libros guardados en la base de datos.

3. **Listar autores registrados**

   - Mostrar los autores asociados a los libros en la base de datos.

4. **Listar autores vivos en un año específico**

   - Filtrar autores que estuvieron vivos en un año dado.

5. **Filtrar libros por idioma**

   - Permitir consultas de libros según idiomas específicos como `ES`, `EN`, `FR`, o `PT`.

---

## Tecnologías utilizadas

- **Java:** Versión 17.
- **Spring Boot:** Framework para construir aplicaciones back-end.
- **PostgreSQL:** Base de datos relacional para almacenar información de libros y autores.
- **Jackson:** Biblioteca para mapear datos JSON.

---

## Dependencias

```xml
<dependencies>
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
        <version>2.18.1</version>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

---

## Configuración

### Archivo `application.properties`

El archivo de configuración incluye las variables de entorno necesarias para conectar con la base de datos PostgreSQL:

```properties
spring.datasource.url=jdbc:postgresql://${DB_HOST}/${DB_NAME}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver
hibernate.dialect=org.hibernate.dialect.HSQLDialect

spring.jpa.hibernate.ddl-auto=update

spring.jpa.show-sql=true
spring.jpa.format-sql=true

server.port=5432
```

### Requisitos previos

- **PostgreSQL** instalado y configurado.
- Configurar las variables de entorno:
  - `DB_HOST`: Dirección del servidor de la base de datos.
  - `DB_NAME`: Nombre de la base de datos.
  - `DB_USER`: Usuario de la base de datos.
  - `DB_PASSWORD`: Contraseña del usuario.

---

## Ejecución

### Compilar y ejecutar el proyecto

1. Clonar el repositorio:

   ```bash
   git clone <url-del-repositorio>
   cd literalura
   ```

2. Compilar el proyecto con Maven:

   ```bash
   mvn clean install
   ```

3. Ejecutar la aplicación:

   ```bash
   mvn spring-boot:run
   ```

### Ejemplos de uso

#### Buscar un libro por título

**Caso exitoso:**

Entrada:

```plaintext
Escribe el nombre del libro:
isla
```

Salida:

```plaintext
Se agregó un nuevo libro. 


--------------------------------------------
Libro = Treasure Island
Autor = Stevenson, Robert Louis
Idioma = en
Número de descargas = 8238.0
--------------------------------------------
```

#### Listar libros registrados

Entrada:

```plaintext
******** Menú ********

1 - Buscar libro por título
2 - Lista de libros registrados

Opción:

2
```

Salida:

```plaintext
Los libros registrados en la base de datos son:

--------------------------------------------
Libro = La piedra angular: novela
Autor = Pardo Bazán, Emilia, condesa de
Idioma = es
Número de descargas = 95.0
--------------------------------------------


--------------------------------------------
Libro = La Odisea
Autor = Homer
Idioma = es
Número de descargas = 4630.0
--------------------------------------------


--------------------------------------------
Libro = Treasure Island
Autor = Stevenson, Robert Louis
Idioma = en
Número de descargas = 8238.0
--------------------------------------------
```

#### Filtrar libros por idioma

Entrada:

```plaintext
Escribe el idioma de los libros que deseas consultar. Utilice solo las dos letras que estan entre []:

[ES]: Español
[EN]: Inglés
[FR]: Francés
[PT]: Portugués
[IT]: Italiano

es
```

Salida:

```plaintext
--------------------------------------------
Libro = Don Quijote
Autor = Cervantes Saavedra, Miguel de
Idioma = es
Número de descargas = 16017.0
--------------------------------------------


--------------------------------------------
Libro = La Odisea
Autor = Homer
Idioma = es
Número de descargas = 4630.0
--------------------------------------------


--------------------------------------------
Libro = La piedra angular: novela
Autor = Pardo Bazán, Emilia, condesa de
Idioma = es
Número de descargas = 95.0
--------------------------------------------


 Estan registrados 3 libros en [es]
```

#### Listar autores vivos en un año específico

Entrada:

```plaintext
Ingresa el año para listar los autores vivos:

1816
```

Salida:

```plaintext
Los autores vivos registrados en 1816 en la base de datos son: 

Autor: Austen, Jane
Fecha de nacimiento: 1775
Fecha de fallecimiento: 1817
Libros: Pride and Prejudice
```

---

## Base de datos

LiterAlura utiliza PostgreSQL como base de datos relacional para almacenar información de libros y autores. Asegúrese de tener PostgreSQL configurado correctamente antes de ejecutar la aplicación.

---

## Notas adicionales

- Este proyecto no incluye una licencia.
- No se incluyen menciones a colaboradores adicionales.

---

¡Gracias por explorar LiterAlura!
