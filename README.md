# clinical-data

Definición de una arquitectura basada en servicios REST que proporcione un ambiente controlado para el acceso y manipulación de datos clínicos.

### Archivos soportados

* Java (.java)
* Python (.py)
* R (.r)

### Tecnologías usadas en el desarrollo

* Java
* Spring Boot
* Maven
* Log4j
* ITextPDF
* MySQL

### Prerrequisitos

* Eclipse, Spring Tool Suite (STS) o similares

* Consumidor de servicios REST como Advance Rest Client, Postman o similares

* Configurar como variables de entorno Java, Python y R

* Acceso de lectura, escritura y ejecución de un folder configurable


### Servicios definidos

```
http://localhost:9000/ClinicalData/api/investigator/create *PUT*
```

```
http://localhost:9000/ClinicalData/api/investigator/activate/{identifier} *POST*
```

```
http://localhost:9000/ClinicalData/api/investigator/inactivate *POST*
```

```
http://localhost:9000/ClinicalData/api/file/upload *POST*
```

```
http://localhost:9000/ClinicalData/api/processData/start  *POST*
```

```
http://localhost:9000/ClinicalData/api/processData/state/{identifier} *GET*
```

```
http://localhost:9000/ClinicalData/api/processData/result/{identifier} *GET*
```

```
http://localhost:9000/ClinicalData/api/report/request/{identifier} *GET*
```

```
http://localhost:9000/ClinicalData/api/report/investigator/{investigatorId} *GET*
```

```
http://localhost:9000/ClinicalData/api/response/result/{identifier} *GET*
```

```
http://localhost:9000/ClinicalData/api/response/request/{identifier} *GET*
```

## Autores

* **Juan Camilo Velásquez Vanegas** - [Juan Camilo Velásquez](https://github.com/pillowslept)

