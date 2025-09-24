# 🛒 Ejercicio técnico: Gestión de pedidos de un e-commerce

## 📋 Descripción
Implementar un servicio REST con **Spring Boot** que permita gestionar pedidos de un e-commerce.
El sistema debe permitir crear pedidos, asociarlos a clientes y productos, y controlar el flujo de estado de cada pedido.

---

## 🗂️ Entidades JPA

### Customer
- `id` (Long, PK)
- `name` (String)
- `email` (String, único, válido)

### Product
- `id` (Long, PK)
- `name` (String)
- `price` (BigDecimal, positivo)

### Order
- `id` (Long, PK)
- `customer` (ManyToOne con Customer)
- `products` (ManyToMany con Product)
- `status` (enum: `CREATED`, `PAID`, `SHIPPED`, `DELIVERED`)
- `createdAt` (LocalDateTime)

---

## 🚀 Endpoints REST

### Pedidos
- `POST /orders` → crear un pedido con productos.
- `GET /orders/{id}` → obtener un pedido por ID.
- `PATCH /orders/{id}/pay` → marcar un pedido como **pagado**.
- `PATCH /orders/{id}/ship` → marcar un pedido como **enviado**.
- `PATCH /orders/{id}/deliver` → marcar un pedido como **entregado**.

### Clientes
- `GET /customers/{id}/orders` → listar pedidos de un cliente.

---

## 🔒 Reglas de negocio
1. No se puede pagar un pedido vacío (sin productos).
2. Solo se puede pagar un pedido en estado **CREATED**.
3. Solo se puede enviar un pedido en estado **PAID**.
4. Solo se puede entregar un pedido en estado **SHIPPED**.

---

## ✨ Extras (opcional si queda tiempo)
- Validar que el email del cliente tenga formato correcto.
- Manejar errores con `@RestControllerAdvice`.
- Documentar API con Swagger/OpenAPI.
- **Testing**:
  - Unit test en `OrderService` para validar reglas de negocio.
  - WebMvcTest en `OrderController` para probar endpoints.

---

## ✅ Lo que se espera
- Proyecto funcional con Spring Boot.
- Persistencia con Spring Data JPA (puede usarse H2 en memoria).
- Código limpio, organizado y legible.
- Manejo de validaciones y errores.
- Tests demostrando cobertura de lógica y endpoints.

---