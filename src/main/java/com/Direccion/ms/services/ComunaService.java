package com.Direccion.ms.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.Direccion.ms.models.entities.Ciudad;
import com.Direccion.ms.models.entities.Comuna;
import com.Direccion.ms.models.request.ActualizarComuna;
import com.Direccion.ms.models.request.AgregarComuna;
import com.Direccion.ms.repositories.CiudadRepository;
import com.Direccion.ms.repositories.ComunaRepository;

//* Servicio que encapsula toda la lógica de negocio relacionada a comunas
//? @Service marca esta clase para que Spring la detecte e inyecte donde se necesite
@Service
public class ComunaService {

    //* Spring inyecta automáticamente los repositorios gracias a @Autowired
    @Autowired
    private ComunaRepository comunaRepository;

    //* Se necesita CiudadRepository para validar que la ciudad existe antes de crear/actualizar
    @Autowired
    private CiudadRepository ciudadRepository;

    //* Retorna la lista completa de comunas registradas en la BD
    public List<Comuna> obtenerTodasLasComunas() {
        return comunaRepository.findAll();
    }

    //* Retorna todas las comunas que pertenecen a una ciudad dada
    //? Útil para poblar selects en cascada: al seleccionar una ciudad, se cargan sus comunas
    public List<Comuna> obtenerComunasPorCiudad(int idCiudad) {
        return comunaRepository.findByCiudad_IdCiudad(idCiudad);
    }

    //* Busca una comuna por su ID
    //! Lanza HTTP 404 si el ID no existe en la BD
    public Comuna obtenerComunaPorId(int id_comuna) {
        return comunaRepository.findById(id_comuna)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comuna no encontrada."));
    }

    //* Crea una nueva comuna a partir del DTO y la persiste en la BD
    //! Lanza HTTP 404 si la ciudad referenciada (id_ciudad) no existe — integridad garantizada
    public Comuna agregarComuna(AgregarComuna nuevaComuna) {
        Ciudad ciudad = ciudadRepository.findById(nuevaComuna.getId_ciudad())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ciudad no encontrada."));
        Comuna comuna = new Comuna();
        comuna.setNombre_comuna(nuevaComuna.getNombre_comuna());
        //* Se asigna el objeto Ciudad completo para que JPA gestione la FK
        comuna.setCiudad(ciudad);
        return comunaRepository.save(comuna);
    }

    //* Actualiza los datos de una comuna existente
    //? id_comuna viene del path de la URL, no del body del request
    //? save() detecta que el ID ya existe en la BD y ejecuta UPDATE en lugar de INSERT
    public Comuna actualizarComuna(int id_comuna, ActualizarComuna actComuna) {
        Comuna comuna = comunaRepository.findById(id_comuna)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comuna no encontrada."));
        Ciudad ciudad = ciudadRepository.findById(actComuna.getId_ciudad())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ciudad no encontrada."));
        comuna.setNombre_comuna(actComuna.getNombre_comuna());
        comuna.setCiudad(ciudad);
        return comunaRepository.save(comuna);
    }

    //* Elimina una comuna por su ID
    //! Lanza HTTP 404 si el ID no existe
    //! Precaución: si la comuna tiene direcciones asociadas, la BD lanzará un error de FK
    public String eliminarComuna(int id_comuna) {
        if (comunaRepository.existsById(id_comuna)) {
            comunaRepository.deleteById(id_comuna);
            return "Comuna eliminada correctamente.";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comuna no encontrada.");
        }
    }

}
