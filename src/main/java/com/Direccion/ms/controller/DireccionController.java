package com.Direccion.ms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Direccion.ms.models.entities.Direccion;
import com.Direccion.ms.models.request.ActualizarDireccion;
import com.Direccion.ms.models.request.AgregarDireccion;
import com.Direccion.ms.services.DireccionService;

import jakarta.validation.Valid;

//* Controlador REST que expone los endpoints de gestión de direcciones
//* Es el controlador principal consumido por el MS Usuario:
//*   - POST /api/direcciones → el MS Usuario lo llama para crear un domicilio
//*   - GET  /api/direcciones/{id} → el MS Usuario lo llama para obtener el domicilio completo
//? @RestController = @Controller + @ResponseBody (responde JSON automáticamente)
//? @RequestMapping define el prefijo de todas las rutas de este controlador
@RestController
@RequestMapping("/api/direcciones")
public class DireccionController {

    //* Servicio inyectado que contiene la lógica de negocio
    @Autowired
    private DireccionService direccionService;

    //* GET /api/direcciones — retorna todas las direcciones
    @GetMapping
    public ResponseEntity<List<Direccion>> obtenerTodas() {
        return ResponseEntity.ok(direccionService.obtenerTodasLasDirecciones());
    }

    //* GET /api/direcciones/{id} — consumido por el MS Usuario para obtener el domicilio completo
    //? La respuesta incluye el objeto completo: Direccion → Comuna → Ciudad → Region → Pais
    //? @PathVariable extrae el valor {id} de la URL
    @GetMapping("/{id}")
    public ResponseEntity<Direccion> obtenerPorId(@PathVariable int id) {
        return ResponseEntity.ok(direccionService.obtenerDireccionPorId(id));
    }

    //* GET /api/direcciones/comuna/{idComuna} — retorna todas las direcciones de una comuna
    @GetMapping("/comuna/{idComuna}")
    public ResponseEntity<List<Direccion>> obtenerPorComuna(@PathVariable int idComuna) {
        return ResponseEntity.ok(direccionService.obtenerDireccionesPorComuna(idComuna));
    }

    //* POST /api/direcciones — consumido por el MS Usuario para registrar el domicilio de un usuario
    //? @Valid activa las validaciones del DTO (ej: @NotBlank, @Positive)
    //? @RequestBody deserializa el JSON del request al DTO
    //! Responde con HTTP 201 Created en lugar del 200 por defecto
    //! El id_direccion retornado es el que el MS Usuario debe guardar en su tabla
    @PostMapping
    public ResponseEntity<Direccion> agregar(@Valid @RequestBody AgregarDireccion nuevaDireccion) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(direccionService.agregarDireccion(nuevaDireccion));
    }

    //* PUT /api/direcciones/{id} — actualiza los datos de una dirección existente
    //? El ID viene en el path y los nuevos datos vienen en el body
    @PutMapping("/{id}")
    public ResponseEntity<Direccion> actualizar(@PathVariable int id,
                                                @Valid @RequestBody ActualizarDireccion actDireccion) {
        return ResponseEntity.ok(direccionService.actualizarDireccion(id, actDireccion));
    }

    //* DELETE /api/direcciones/{id} — elimina una dirección por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable int id) {
        return ResponseEntity.ok(direccionService.eliminarDireccion(id));
    }

}
