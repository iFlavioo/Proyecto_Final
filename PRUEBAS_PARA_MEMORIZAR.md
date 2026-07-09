# PRUEBAS PARA MEMORIZAR — BookPoint Chile
## Instrucciones
Estas son las 26 pruebas que debes programar en vivo (2 por microservicio).
Son las más sencillas. ¡Memorízalas antes de la presentación!

---

## Usuario

### 1. UsuarioServiceTest — testEliminarUsuario()
```java
@Test
void testEliminarUsuario() {
    doNothing().when(usuarioRepository).deleteById(1L);
    usuarioService.eliminarUsuario(1L);
    verify(usuarioRepository).deleteById(1L);
}
```

### 2. UsuarioControllerIT — testActualizar()
```java
@Test
void testActualizar() throws Exception {
    Usuario guardado = usuarioRepository.save(new Usuario(null, "nombre", "email", "password", "rol"));
    UsuarioDTO dto = new UsuarioDTO(null, "alt-nombre", "alt-email", "alt-password", "alt-rol");
    mockMvc.perform(put("/api/v1/usuarios/" + guardado.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isOk());
}
```

## Producto

### 1. ProductoServiceTest — testEliminarProducto()
```java
@Test
void testEliminarProducto() {
    doNothing().when(productoRepository).deleteById(1L);
    productoService.eliminarProducto(1L);
    verify(productoRepository).deleteById(1L);
}
```

### 2. ProductoControllerIT — testActualizar()
```java
@Test
void testActualizar() throws Exception {
    Producto guardado = productoRepository.save(new Producto(null, "nombre", "descripcion", 99.9, "categoria", "autor", "editorial"));
    ProductoDTO dto = new ProductoDTO(null, "alt-nombre", "alt-descripcion", 199.9, "alt-categoria", "alt-autor", "alt-editorial");
    mockMvc.perform(put("/api/v1/productos/" + guardado.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isOk());
}
```

## Sucursal

### 1. SucursalServiceTest — testEliminarSucursal()
```java
@Test
void testEliminarSucursal() {
    doNothing().when(sucursalRepository).deleteById(1L);
    sucursalService.eliminarSucursal(1L);
    verify(sucursalRepository).deleteById(1L);
}
```

### 2. SucursalControllerIT — testActualizar()
```java
@Test
void testActualizar() throws Exception {
    Sucursal guardado = sucursalRepository.save(new Sucursal(null, "nombre", "direccion", "ciudad", "telefono", "horario"));
    SucursalDTO dto = new SucursalDTO(null, "alt-nombre", "alt-direccion", "alt-ciudad", "alt-telefono", "alt-horario");
    mockMvc.perform(put("/api/v1/sucursales/" + guardado.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isOk());
}
```

## Proveedor

### 1. ProveedorServiceTest — testEliminarProveedor()
```java
@Test
void testEliminarProveedor() {
    doNothing().when(proveedorRepository).deleteById(1L);
    proveedorService.eliminarProveedor(1L);
    verify(proveedorRepository).deleteById(1L);
}
```

### 2. ProveedorControllerIT — testActualizar()
```java
@Test
void testActualizar() throws Exception {
    Proveedor guardado = proveedorRepository.save(new Proveedor(null, "nombre", "email", "telefono", "editorial", true));
    ProveedorDTO dto = new ProveedorDTO(null, "alt-nombre", "alt-email", "alt-telefono", "alt-editorial", true);
    mockMvc.perform(put("/api/v1/proveedores/" + guardado.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isOk());
}
```

## Descuento

### 1. DescuentoServiceTest — testEliminarDescuento()
```java
@Test
void testEliminarDescuento() {
    doNothing().when(descuentoRepository).deleteById(1L);
    descuentoService.eliminarDescuento(1L);
    verify(descuentoRepository).deleteById(1L);
}
```

### 2. DescuentoControllerIT — testActualizar()
```java
@Test
void testActualizar() throws Exception {
    Descuento guardado = descuentoRepository.save(new Descuento(null, "codigo", "descripcion", 99.9, "tipo", true, java.time.LocalDate.now()));
    DescuentoDTO dto = new DescuentoDTO(null, "alt-codigo", "alt-descripcion", 199.9, "alt-tipo", true, java.time.LocalDate.now());
    mockMvc.perform(put("/api/v1/descuentos/" + guardado.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isOk());
}
```

## Resena

### 1. ResenaServiceTest — testEliminarResena()
```java
@Test
void testEliminarResena() {
    doNothing().when(resenaRepository).deleteById(1L);
    resenaService.eliminarResena(1L);
    verify(resenaRepository).deleteById(1L);
}
```

### 2. ResenaControllerIT — testActualizar()
```java
@Test
void testActualizar() throws Exception {
    Resena guardado = resenaRepository.save(new Resena(null, 1L, 1L, "comentario", 5, java.time.LocalDate.now()));
    ResenaDTO dto = new ResenaDTO(null, 1L, 1L, "alt-comentario", 10, java.time.LocalDate.now());
    mockMvc.perform(put("/api/v1/resenas/" + guardado.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isOk());
}
```

## Devolucion

### 1. DevolucionServiceTest — testEliminarDevolucion()
```java
@Test
void testEliminarDevolucion() {
    doNothing().when(devolucionRepository).deleteById(1L);
    devolucionService.eliminarDevolucion(1L);
    verify(devolucionRepository).deleteById(1L);
}
```

### 2. DevolucionControllerIT — testActualizar()
```java
@Test
void testActualizar() throws Exception {
    Devolucion guardado = devolucionRepository.save(new Devolucion(null, 1L, 1L, "motivo", "estado", java.time.LocalDate.now()));
    DevolucionDTO dto = new DevolucionDTO(null, 1L, 1L, "alt-motivo", "alt-estado", java.time.LocalDate.now());
    mockMvc.perform(put("/api/v1/devoluciones/" + guardado.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isOk());
}
```

## Notificacion

### 1. NotificacionServiceTest — testEliminarNotificacion()
```java
@Test
void testEliminarNotificacion() {
    doNothing().when(notificacionRepository).deleteById(1L);
    notificacionService.eliminarNotificacion(1L);
    verify(notificacionRepository).deleteById(1L);
}
```

### 2. NotificacionControllerIT — testActualizar()
```java
@Test
void testActualizar() throws Exception {
    Notificacion guardado = notificacionRepository.save(new Notificacion(null, 1L, "mensaje", "tipo", true, java.time.LocalDate.now()));
    NotificacionDTO dto = new NotificacionDTO(null, 1L, "alt-mensaje", "alt-tipo", true, java.time.LocalDate.now());
    mockMvc.perform(put("/api/v1/notificaciones/" + guardado.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isOk());
}
```

## Pedido

### 1. PedidoServiceTest — testEliminarPedido()
```java
@Test
void testEliminarPedido() {
    doNothing().when(pedidoRepository).deleteById(1L);
    pedidoService.eliminarPedido(1L);
    verify(pedidoRepository).deleteById(1L);
}
```

### 2. PedidoControllerIT — testActualizar()
```java
@Test
void testActualizar() throws Exception {
    Pedido guardado = pedidoRepository.save(new Pedido(null, 1L, 1L, 5, "estado", java.time.LocalDate.now(), "direccionEntrega"));
    PedidoDTO dto = new PedidoDTO(null, 1L, 1L, 10, "alt-estado", java.time.LocalDate.now(), "alt-direccionEntrega");
    mockMvc.perform(put("/api/v1/pedidos/" + guardado.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isOk());
}
```

## Inventario

### 1. InventarioServiceTest — testEliminarInventario()
```java
@Test
void testEliminarInventario() {
    doNothing().when(inventarioRepository).deleteById(1L);
    inventarioService.eliminarInventario(1L);
    verify(inventarioRepository).deleteById(1L);
}
```

### 2. InventarioControllerIT — testActualizar()
```java
@Test
void testActualizar() throws Exception {
    Inventario guardado = inventarioRepository.save(new Inventario(null, 1L, 1L, 5, 5));
    InventarioDTO dto = new InventarioDTO(null, 1L, 1L, 10, 10);
    mockMvc.perform(put("/api/v1/inventario/" + guardado.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isOk());
}
```

## Transferencia

### 1. TransferenciaServiceTest — testEliminarTransferencia()
```java
@Test
void testEliminarTransferencia() {
    doNothing().when(transferenciaRepository).deleteById(1L);
    transferenciaService.eliminarTransferencia(1L);
    verify(transferenciaRepository).deleteById(1L);
}
```

### 2. TransferenciaControllerIT — testActualizar()
```java
@Test
void testActualizar() throws Exception {
    Transferencia guardado = transferenciaRepository.save(new Transferencia(null, 1L, 1L, 1L, 5, "estado", java.time.LocalDate.now()));
    TransferenciaDTO dto = new TransferenciaDTO(null, 1L, 1L, 1L, 10, "alt-estado", java.time.LocalDate.now());
    mockMvc.perform(put("/api/v1/transferencias/" + guardado.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isOk());
}
```

## Despacho

### 1. DespachoServiceTest — testEliminarDespacho()
```java
@Test
void testEliminarDespacho() {
    doNothing().when(despachoRepository).deleteById(1L);
    despachoService.eliminarDespacho(1L);
    verify(despachoRepository).deleteById(1L);
}
```

### 2. DespachoControllerIT — testActualizar()
```java
@Test
void testActualizar() throws Exception {
    Despacho guardado = despachoRepository.save(new Despacho(null, 1L, 1L, "estado", java.time.LocalDate.now(), "direccionDestino"));
    DespachoDTO dto = new DespachoDTO(null, 1L, 1L, "alt-estado", java.time.LocalDate.now(), "alt-direccionDestino");
    mockMvc.perform(put("/api/v1/despachos/" + guardado.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isOk());
}
```

## Venta (ESPECIAL - múltiples productos)

### 1. VentaServiceTest — testEliminarVenta()
```java
@Test
void testEliminarVenta() {
    doNothing().when(ventaRepository).deleteById(1L);
    ventaService.eliminarVenta(1L);
    verify(ventaRepository).deleteById(1L);
}
```

### 2. VentaControllerTest — testActualizarVenta()
```java
@Test
void testActualizarVenta() throws Exception {
    VentaDetalleDTO det = new VentaDetalleDTO(null, 1L, 2, 15990.0, 31980.0);
    VentaDTO dto = new VentaDTO(1L, 1L, 1L, java.time.LocalDate.now(), 31980.0, List.of(det));
    Venta actualizada = new Venta(); actualizada.setId(1L);
    actualizada.setDetalles(new java.util.ArrayList<>());
    Mockito.when(ventaService.actualizarVenta(eq(1L), ArgumentMatchers.<Venta>any())).thenReturn(actualizada);
    mockMvc.perform(put("/api/v1/ventas/1").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isOk());
}
```

### 3. VentaControllerIT — testListarVentas()
```java
@Test
void testListarVentas() throws Exception {
    VentaDetalleDTO d = new VentaDetalleDTO(null, 1L, 1, 5000.0, null);
    VentaDTO dto = new VentaDTO(null, 1L, 1L, LocalDate.now(), null, List.of(d));
    mockMvc.perform(post("/api/v1/ventas").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto))).andExpect(status().isOk());
    mockMvc.perform(get("/api/v1/ventas")).andExpect(status().isOk())
        .andExpect(jsonPath("$[0]").exists());
}
```
