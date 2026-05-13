package com.Direccion.ms.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.Direccion.ms.models.entities.Comuna;
import com.Direccion.ms.models.entities.Direccion;
import com.Direccion.ms.models.request.ActualizarDireccion;
import com.Direccion.ms.models.request.AgregarDireccion;
import com.Direccion.ms.repositories.ComunaRepository;
import com.Direccion.ms.repositories.DireccionRepository;

//* Servicio que encapsula toda la lógica de negocio relacionada a direcciones
//* Es el punto de integración principal con el MS Usuario:
//*   - El MS Usuario llama POST /api/direcciones para crear el domicilio de un usuario
//*   - El MS Usuario llama GET /api/direcciones/{id} para obtener el domicilio completo
//? @Service marca esta clase para que Spring la detecte e inyecte donde se necesite
@Service
public class DireccionService {

    //* Spring inyecta automáticamente los repositorios gracias a @Autowired
    @Autowired
    private DireccionRepository direccionRepository;

    //* Se necesita ComunaRepository para validar que la comuna existe antes de crear/actualizar
    @Autowired
    private ComunaRepository comunaRepository;

    //* Retorna la lista completa de direcciones registradas en la BD
    public List<Direccion> obtenerTodasLasDirecciones() {
        return direccionRepository.findAll();
    }

    //* Retorna todas las direcciones que pertenecen a una comuna dada
    public List<Direccion> obtenerDireccionesPorComuna(int idComuna) {
        return direccionRepository.findByComuna_IdComuna(idComuna);
    }

    //* Busca una dirección por su ID — endpoint principal consumido por el MS Usuario
    //! Lanza HTTP 404 si el ID no existe en la BD
    //? La respuesta incluye el objeto Comuna completo con Ciudad, Región y País anidados
    public Direccion obtenerDireccionPorId(int id_direccion) {
        return direccionRepository.findById(id_direccion)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dirección no encontrada."));
    }

    //* Crea una nueva dirección a partir del DTO y la persiste en la BD
    //! Lanza HTTP 404 si la comuna referenciada (id_comuna) no existe — integridad garantizada
    //? El id_direccion generado es el que el MS Usuario debe guardar en su tabla de usuarios
    public Direccion agregarDireccion(AgregarDireccion nuevaDireccion) {
        Comuna comuna = comunaRepository.findById(nuevaDireccion.getId_comuna())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comuna no encontrada."));
        Direccion direccion = new Direccion();
        direccion.setCalle(nuevaDireccion.getCalle());
        direccion.setNumero(nuevaDireccion.getNumero());
        //* Se asigna el objeto Comuna completo para que JPA gestione la FK
        direccion.setComuna(comuna);
        return direccionRepository.save(direccion);
    }

    //* Actualiza los datos de una dirección existente
    //? id_direccion viene del path de la URL, no del body del request
    //? save() detecta que el ID ya existe en la BD y ejecuta UPDATE en lugar de INSERT
    public Direccion actualizarDireccion(int id_direccion, ActualizarDireccion actDireccion) {
        Direccion direccion = direccionRepository.findById(id_direccion)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dirección no encontrada."));
        Comuna comuna = comunaRepository.findById(actDireccion.getId_comuna())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comuna no encontrada."));
        direccion.setCalle(actDireccion.getCalle());
        direccion.setNumero(actDireccion.getNumero());
        direccion.setComuna(comuna);
        return direccionRepository.save(direccion);
    }

    //* Elimina una dirección por su ID
    //! Lanza HTTP 404 si el ID no existe
    //! Si el MS Usuario tiene usuarios con este id_direccion, quedará como referencia huérfana
    public String eliminarDireccion(int id_direccion) {
        if (direccionRepository.existsById(id_direccion)) {
            direccionRepository.deleteById(id_direccion);
            return "Dirección eliminada correctamente.";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dirección no encontrada.");
        }
    }

}
