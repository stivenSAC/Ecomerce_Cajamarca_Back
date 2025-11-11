# E-commerce Mujeres de Cajamarca - Backend

API REST para e-commerce desarrollada con Spring Boot 3.5.7 y Java 21.

## 🚀 Tecnologías

- **Java 21**
- **Spring Boot 3.5.7**
- **Spring Security + JWT**
- **PostgreSQL**
- **Docker & Docker Compose**
- **Maven**

## 🐳 Ejecutar con Docker

### Prerrequisitos
- Docker
- Docker Compose

### Comandos
```bash
# Construir y levantar servicios
docker-compose up --build

# Solo levantar (si ya está construido)
docker-compose up

# Ejecutar en background
docker-compose up -d

# Parar servicios
docker-compose down

# Parar y eliminar volúmenes
docker-compose down -v
```

## 📡 Endpoints

### 🌐 Públicos (sin autenticación)
- `GET /api/products` - Ver todos los productos
- `GET /api/products/{id}` - Ver producto específico
- `GET /api/products/search?nombre=` - Buscar productos
- `POST /api/auth/login` - Iniciar sesión
- `POST /api/auth/register` - Registrarse

### 🔒 Privados (requieren JWT)
- `POST /api/products` - Crear producto
- `PUT /api/products/{id}` - Actualizar producto
- `DELETE /api/products/{id}` - Eliminar producto
- `GET /api/products/my-products` - Mis productos
- `GET /api/users/profile` - Ver perfil
- `PUT /api/users/profile` - Actualizar perfil
- `PATCH /api/users/profile` - Actualización parcial
- `DELETE /api/users/profile` - Eliminar cuenta

## 🖼️ Compresión de Imágenes

El sistema comprime automáticamente las imágenes:
- Acepta hasta 50MB
- Comprime a <200KB manteniendo calidad
- Formatos: JPG, PNG, WEBP, GIF, BMP

## 🌐 URLs

- **Backend:** http://localhost:8085
- **Base de datos:** localhost:5432 (solo accesible internamente)

## 📝 Variables de Entorno

```env
JWT_SECRET=tu_clave_secreta_aqui
POSTGRES_DB=ecommerce
POSTGRES_USER=postgres
POSTGRES_PASSWORD=password
```

## 🚀 Deploy en Azure

1. Sube el código a GitHub
2. Crea Azure Container Apps
3. Conecta con tu repositorio
4. Azure construirá automáticamente con Docker

¡Listo para producción! 🎯