package com.Direccion.ms.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.Direccion.ms.models.entities.Ciudad;
import com.Direccion.ms.models.entities.Region;
import com.Direccion.ms.models.request.ActualizarCiudad;
import com.Direccion.ms.models.request.AgregarCiudad;
import com.Direccion.ms.repositories.CiudadRepository;
import com.Direccion.ms.repositories.RegionRepository;

//* Servicio que encapsula toda la lógica de negocio relacionada a ciudades
//? @Service marca esta clase para que Spring la detecte e inyecte donde se necesite
@Service
public class CiudadService {

    //* Spring inyecta automáticamente los repositorios gracias a @Autowired
    @Autowired
    private CiudadRepository ciudadRepository;

    //* Se necesita RegionRepository para validar que la región existe antes de crear/actualizar
    @Autowired
    private RegionRepository regionRepository;

    //* Retorna la lista completa de ciudades registradas en la BD
    public List<Ciudad> obtenerTodasLasCiudades() {
        return ciudadRepository.findAll();
    }

    //* Retorna todas las ciudades que pertenecen a una región dada
    //? Útil para poblar selects en cascada: al seleccionar una región, se cargan sus ciudades
    public List<Ciudad> obtenerCiudadesPorRegion(int idRegion) {
        return ciudadRepository.findByRegion_IdRegion(idRegion);
    }

    //* Busca una ciudad por su ID
    //! Lanza HTTP 404 si el ID no existe en la BD
    public Ciudad obtenerCiudadPorId(int id_ciudad) {
        return ciudadRepository.findById(id_ciudad)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ciudad no encontrada."));
    }

    //* Crea una nueva ciudad a partir del DTO y la persiste en la BD
    //! Lanza HTTP 404 si la región referenciada (id_region) no existe — integridad garantizada
    public Ciudad agregarCiudad(AgregarCiudad nuevaCiudad) {
        Region region = regionRepository.findById(nuevaCiudad.getId_region())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Región no encontrada."));
        Ciudad ciudad = new Ciudad();
        ciudad.setNombre_ciudad(nuevaCiudad.getNombre_ciudad());
        //* Se asigna el objeto Region completo para que JPA gestione la FK
        ciudad.setRegion(region);
        return ciudadRepository.save(ciudad);
    }

    //* Actualiza los datos de una ciudad existente
    //? id_ciudad viene del path de la URL, no del body del request
    //? save() detecta que el ID ya existe en la BD y ejecuta UPDATE en lugar de INSERT
    public Ciudad actualizarCiudad(int id_ciudad, ActualizarCiudad actCiudad) {
        Ciudad ciudad = ciudadRepository.findById(id_ciudad)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ciudad no encontrada."));
        Region region = regionRepository.findById(actCiudad.getId_region())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Región no encontrada."));
        ciudad.setNombre_ciudad(actCiudad.getNombre_ciudad());
        ciudad.setRegion(region);
        return ciudadRepository.save(ciudad);
    }

    //* Elimina una ciudad por su ID
    //! Lanza HTTP 404 si el ID no existe
    //! Precaución: si la ciudad tiene comunas asociadas, la BD lanzará un error de FK
    public String eliminarCiudad(int id_ciudad) {
        if (ciudadRepository.existsById(id_ciudad)) {
            ciudadRepository.deleteById(id_ciudad);
            return "Ciudad eliminada correctamente.";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ciudad no encontrada.");
        }
    }

}
