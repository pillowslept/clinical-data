# clinical-data

Definición de una arquitectura basada en servicios REST que proporcione un ambiente controlado para el acceso y manipulación de datos clínicos.

### Lenguajes soportados

* Java
* Python
* R

### Tecnologías usadas en el desarrollo

* Java
* Spring Boot
* Maven
* Log4j
* ITextPDF
* MySQL

### Prerrequisitos

Requisitos para la construcción y funcionamiento

```
Eclipse, Spring Tool Suite (STS) o similares
```

```
Consumidor de servicios REST como Advance Rest Client, Postman o similares
```

```
Configurar como variables de entorno Java, Python y R
```

```
Acceso de lectura, escritura y ejecución de un folder configurable
```

### Servicios definidos

Para comenzar, los servicios expuestos por la aplicación deberán ser consumidos de la siguiente manera: 

```
http://localhost:9000
```

Usando como base la URL anterior más la ruta de despliegue de los servicios:

```
http://localhost:9000/ClinicalData/
```

Ahora, todos los servicios son definidos dentro el siguiente subpath:

|
http://localhost:9000/ClinicalData/api/
|

Finalmente, cada servicios construido:

```
http://localhost:9000/ClinicalData/api/investigator/create *Protocolo PUT*
```

```
http://localhost:9000/ClinicalData/api/investigator/activate/{identifier} *Protocolo POST*
```

```
http://localhost:9000/ClinicalData/api/investigator/inactivate *Protocolo POST*
```

```
http://localhost:9000/ClinicalData/api/file/upload *Protocolo POST*
```

```
http://localhost:9000/ClinicalData/api/processData/start  *Protocolo POST*
```

```
http://localhost:9000/ClinicalData/api/processData/state/{identifier} *Protocolo GET*
```

```
http://localhost:9000/ClinicalData/api/processData/result/{identifier} *Protocolo GET*
```

```
http://localhost:9000/ClinicalData/api/report/request/{identifier} *Protocolo GET*
```

```
http://localhost:9000/ClinicalData/api/report/investigator/{investigatorId} *Protocolo GET*
```

## Autores

* **Juan Camilo Velásquez Vanegas** - [Juan Camilo Velásquez](https://github.com/pillowslept)
