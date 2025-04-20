# API

## Descripción del Proyecto

** API** Prueba técnica sebastian picardo.

### Funciones Implementadas:

1. **Crear usuario**:

   End point que crea un usuario
   Ej curl --location --request POST 'http://localhost:8080/api/users/createUser' \
   --header 'Content-Type: application/json' \
   --header 'Cookie: JSESSIONID=BC6AC0ABEEDFA42153EE22BAC3F615A6' \
   --data-raw '{
   "name": "sebastian picardo",
   "email": "seba@ejemplocl",
   "password": "seba123",
   "phones": [
   {
   "number": "11",
   "citycode": "1",
   "contrycode": "57"
   }
   ]
   }'

2. **Login**:

   Endpoint que permite hacer el login y devuelve un token
   curl --location --request POST 'http://localhost:8080/api/users/login' \
   --header 'Content-Type: application/json' \
   --header 'Cookie: JSESSIONID=BC6AC0ABEEDFA42153EE22BAC3F615A6' \
   --data-raw '{
   "email": "seba@ejemplocl",
   "password": "seba123"
   }'

---

2. **swagger**:
http://localhost:8080/swagger-ui.html

