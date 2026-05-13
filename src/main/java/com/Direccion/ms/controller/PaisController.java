package com.Direccion.ms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Direccion.ms.models.entities.Pais;
import com.Direccion.ms.models.request.ActualizarPais;
import com.Direccion.ms.models.request.AgregarPais;
import com.Direccion.ms.services.PaisService;

import jakarta.validation.Valid;

//* Controlador REST que expone los endpoints de gestión de países
//? @RestController = @Controller + @ResponseBody (responde JSON automáticamente)
//? @RequestMapping define el prefijo de todas las rutas de este controlador
@RestController
@RequestMapping("/api/paises")
public class PaisController {

    //* Servicio inyectado que contiene la lógica de negocio
    @Autowired
    private PaisService paisService;

    //* GET /api/paises — retorna todos los países
    @GetMapping
    public ResponseEntity<List<Pais>> obtenerTodos() {
        return ResponseEntity.ok(paisService.obtenerTodosLosPaises());
    }

    //* GET /api/paises/{id} — retorna un país por su ID
    //? @PathVariable extrae el valor {id} de la URL
    @GetMapping("/{id}")
    public ResponseEntity<Pais> obtenerPorId(@PathVariable int id) {
        return ResponseEntity.ok(paisService.obtenerPaisPorId(id));
    }

    //* POST /api/paises — crea un nuevo país
    //? @Valid activa las validaciones del DTO (ej: @NotBlank)
    //? @RequestBody deserializa el JSON del request al DTO
    //! Responde con HTTP 201 Created en lugar del 200 por defecto
    @PostMapping
    public ResponseEntity<Pais> agregar(@Valid @RequestBody AgregarPais nuevoPais) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paisService.agregarPais(nuevoPais));
    }

    //* PUT /api/paises/{id} — actualiza los datos de un país existente
    //? El ID viene en el path y los nuevos datos vienen en el body
    @PutMapping("/{id}")
    public ResponseEntity<Pais> actualizar(@PathVariable int id,
                                           @Valid @RequestBody ActualizarPais actPais) {
        return ResponseEntity.ok(paisService.actualizarPais(id, actPais));
    }

    //* DELETE /api/paises/{id} — elimina un país por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable int id) {
        return ResponseEntity.ok(paisService.eliminarPais(id));
    }

}
