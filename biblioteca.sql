-- MySQL dump 10.13  Distrib 8.0.40, for Linux (x86_64)
--
-- Host: localhost    Database: biblioteca
-- ------------------------------------------------------
-- Server version	8.0.40

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `cds`
--

DROP TABLE IF EXISTS `cds`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cds` (
  `id` int NOT NULL,
  `artista` varchar(100) NOT NULL,
  `genero` varchar(50) NOT NULL,
  `duracion` time NOT NULL,
  `numero_canciones` int NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `cds_ibfk_1` FOREIGN KEY (`id`) REFERENCES `materiales` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cds`
--

LOCK TABLES `cds` WRITE;
/*!40000 ALTER TABLE `cds` DISABLE KEYS */;
INSERT INTO `cds` VALUES (3,'The Beatles','Rock','00:47:23',17);
/*!40000 ALTER TABLE `cds` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `configuracion_prestamos`
--

DROP TABLE IF EXISTS `configuracion_prestamos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `configuracion_prestamos` (
  `tipo_material` enum('Libro','Revista','CD','DVD','Tesis') NOT NULL,
  `max_prestamos` int NOT NULL,
  PRIMARY KEY (`tipo_material`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `configuracion_prestamos`
--

LOCK TABLES `configuracion_prestamos` WRITE;
/*!40000 ALTER TABLE `configuracion_prestamos` DISABLE KEYS */;
INSERT INTO `configuracion_prestamos` VALUES ('Libro',5),('Revista',3),('CD',2),('DVD',2),('Tesis',1);
/*!40000 ALTER TABLE `configuracion_prestamos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dvds`
--

DROP TABLE IF EXISTS `dvds`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dvds` (
  `id` int NOT NULL,
  `director` varchar(100) NOT NULL,
  `duracion` time NOT NULL,
  `genero` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `dvds_ibfk_1` FOREIGN KEY (`id`) REFERENCES `materiales` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dvds`
--

LOCK TABLES `dvds` WRITE;
/*!40000 ALTER TABLE `dvds` DISABLE KEYS */;
INSERT INTO `dvds` VALUES (4,'Christopher Nolan','02:28:00','Ciencia Ficción'),(34,'Manuel Portillo','01:12:25','Accion');
/*!40000 ALTER TABLE `dvds` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `libros`
--

DROP TABLE IF EXISTS `libros`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `libros` (
  `id` int NOT NULL,
  `autor` varchar(100) NOT NULL,
  `genero` varchar(50) DEFAULT NULL,
  `numero_paginas` int NOT NULL,
  `editorial` varchar(100) NOT NULL,
  `isbn` varchar(20) NOT NULL,
  `anio_publicacion` year NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `libros_ibfk_1` FOREIGN KEY (`id`) REFERENCES `materiales` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `libros`
--

LOCK TABLES `libros` WRITE;
/*!40000 ALTER TABLE `libros` DISABLE KEYS */;
INSERT INTO `libros` VALUES (1,'Gabriel García Márquez',NULL,471,'Sudamericana','1234567890',1967),(32,'Brandon Sanderson','Fantasia',1200,'Nova','9788466657662',2015),(35,'Brandon Sanderson','Fantasia',1248,'Nova','9788466657549',2015),(36,'Pablo Cohelo','Ficcion',325,'Planeta','987465419',1990);
/*!40000 ALTER TABLE `libros` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `materiales`
--

DROP TABLE IF EXISTS `materiales`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `materiales` (
  `id` int NOT NULL AUTO_INCREMENT,
  `codigo_interno` varchar(50) NOT NULL,
  `titulo` varchar(200) NOT NULL,
  `tipo` enum('Libro','Revista','CD','DVD','Tesis') NOT NULL,
  `unidades_disponibles` int NOT NULL,
  `ubicacion` varchar(100) NOT NULL,
  `fecha_registro` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `codigo_interno` (`codigo_interno`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `materiales`
--

LOCK TABLES `materiales` WRITE;
/*!40000 ALTER TABLE `materiales` DISABLE KEYS */;
INSERT INTO `materiales` VALUES (1,'LIB00001','Cien años de soledad','Libro',12,'Estante A1','2024-11-11 02:30:51'),(2,'REV00001','National Geographic','Revista',6,'Estante B2','2024-11-11 02:30:51'),(3,'CD00001','Abbey Road','CD',3,'Estante C1','2024-11-11 02:30:51'),(4,'DVD00001','Inception','DVD',1,'Estante D3','2024-11-11 02:30:51'),(5,'TES00001','Big Data en Educación','Tesis',1,'Estante T1','2024-11-11 02:30:51'),(32,'LIB00002','El Camino de los reyes','Libro',3,'Estante 45','2024-11-12 16:09:13'),(33,'TES00002','El Planeta X','Tesis',1,'Estante B5','2024-11-12 16:13:37'),(34,'DVD00002','Mad Max 3','DVD',1,'Estante C3','2024-11-12 16:16:30'),(35,'LIB00003','Palabras Radiantes','Libro',6,'Estanteria 15','2024-11-12 16:40:23'),(36,'LIB00004','El alquimista','Libro',9,'Estanteria 8','2024-11-13 03:12:27');
/*!40000 ALTER TABLE `materiales` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `multas`
--

DROP TABLE IF EXISTS `multas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `multas` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_prestamo` int NOT NULL,
  `monto` decimal(5,2) NOT NULL,
  `pagada` tinyint(1) DEFAULT '0',
  `fecha_generacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `id_prestamo` (`id_prestamo`),
  CONSTRAINT `multas_ibfk_1` FOREIGN KEY (`id_prestamo`) REFERENCES `prestamos` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `multas`
--

LOCK TABLES `multas` WRITE;
/*!40000 ALTER TABLE `multas` DISABLE KEYS */;
INSERT INTO `multas` VALUES (1,1,1.50,0,'2024-11-11 02:30:51'),(2,2,0.00,1,'2024-11-11 02:30:51'),(3,1,11.00,0,'2024-11-12 18:11:11'),(4,1,11.00,0,'2024-11-12 18:11:18'),(5,1,11.00,0,'2024-11-12 18:11:25'),(6,2,10.00,0,'2024-11-12 18:11:32');
/*!40000 ALTER TABLE `multas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prestamos`
--

DROP TABLE IF EXISTS `prestamos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prestamos` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_usuario` int NOT NULL,
  `id_material` int NOT NULL,
  `fecha_prestamo` date NOT NULL,
  `fecha_devolucion` date DEFAULT NULL,
  `mora_acumulada` decimal(5,2) DEFAULT '0.00',
  `estado` enum('Pendiente','Devuelto','Mora') DEFAULT 'Pendiente',
  PRIMARY KEY (`id`),
  KEY `id_usuario` (`id_usuario`),
  KEY `id_material` (`id_material`),
  CONSTRAINT `prestamos_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `prestamos_ibfk_2` FOREIGN KEY (`id_material`) REFERENCES `materiales` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prestamos`
--

LOCK TABLES `prestamos` WRITE;
/*!40000 ALTER TABLE `prestamos` DISABLE KEYS */;
INSERT INTO `prestamos` VALUES (1,3,1,'2024-11-01','2024-11-12',0.00,'Devuelto'),(2,3,2,'2024-11-02','2024-11-12',0.00,'Devuelto'),(3,1,1,'2024-11-12','2024-11-12',0.00,'Devuelto'),(4,1,1,'2024-11-12','2024-11-12',0.00,'Devuelto'),(5,1,1,'2024-11-12','2024-11-12',0.00,'Devuelto'),(6,1,1,'2024-11-12','2024-11-12',0.00,'Devuelto'),(7,3,32,'2024-11-12','2024-11-12',6.00,'Devuelto'),(8,2,1,'2024-11-12',NULL,0.00,'Pendiente'),(9,1,1,'2024-11-12',NULL,0.00,'Pendiente'),(10,3,34,'2024-11-12',NULL,0.00,'Pendiente'),(11,3,34,'2024-11-12',NULL,0.00,'Pendiente'),(12,5,32,'2024-11-12',NULL,0.00,'Pendiente'),(13,5,4,'2024-11-12',NULL,0.00,'Pendiente'),(14,17,1,'2024-11-12',NULL,0.00,'Pendiente'),(15,3,32,'2024-11-12',NULL,0.00,'Pendiente');
/*!40000 ALTER TABLE `prestamos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `revistas`
--

DROP TABLE IF EXISTS `revistas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `revistas` (
  `id` int NOT NULL,
  `editorial` varchar(100) NOT NULL,
  `periodicidad` enum('Diaria','Semanal','Mensual','Anual') NOT NULL,
  `fecha_publicacion` date NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `revistas_ibfk_1` FOREIGN KEY (`id`) REFERENCES `materiales` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `revistas`
--

LOCK TABLES `revistas` WRITE;
/*!40000 ALTER TABLE `revistas` DISABLE KEYS */;
INSERT INTO `revistas` VALUES (2,'NatGeo','Mensual','2024-01-01');
/*!40000 ALTER TABLE `revistas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tesis`
--

DROP TABLE IF EXISTS `tesis`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tesis` (
  `id` int NOT NULL,
  `autor` varchar(100) NOT NULL,
  `titulo_investigacion` varchar(200) NOT NULL,
  `carrera` varchar(100) NOT NULL,
  `universidad` varchar(150) DEFAULT NULL,
  `anio_presentacion` year NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `tesis_ibfk_1` FOREIGN KEY (`id`) REFERENCES `materiales` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tesis`
--

LOCK TABLES `tesis` WRITE;
/*!40000 ALTER TABLE `tesis` DISABLE KEYS */;
INSERT INTO `tesis` VALUES (5,'María López','Impacto del Big Data en la mejora educativa','Ingeniería en Sistemas','Universidad Nacional',2022),(33,'Manuel Aguilera','El Planeta X: Su influencia gravitacional','Licenciatura en Astronomia','Universidad Nacional de El Salvador',2020);
/*!40000 ALTER TABLE `tesis` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `username` varchar(50) DEFAULT NULL,
  `tipo` enum('Administrador','Profesor','Alumno') NOT NULL,
  `correo` varchar(100) NOT NULL,
  `telefono` varchar(15) DEFAULT NULL,
  `password` varchar(100) NOT NULL,
  `fecha_registro` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `estado` enum('Activo','Inactivo') DEFAULT 'Activo',
  PRIMARY KEY (`id`),
  UNIQUE KEY `correo` (`correo`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'Juan Pérez','jperez','Administrador','juan.perez@example.com','555-1234','123456','2024-11-11 02:30:51','Activo'),(2,'María López','mlopez','Profesor','maria.lopez@example.com','555-5678','password456','2024-11-11 02:30:51','Activo'),(3,'Carlos Gómez','cgomez','Alumno','carlos.gomez@example.com','555-9876','123456','2024-11-11 02:30:51','Activo'),(5,'Sujeto Pruebas','test','Administrador','test@example.com','555-1234','test','2024-11-11 03:20:21','Activo'),(17,'Juan Jose','Jjose','Profesor','jose@gmail.com','7489-9876','123456','2024-11-13 03:11:39','Activo');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-11-12 22:12:58
