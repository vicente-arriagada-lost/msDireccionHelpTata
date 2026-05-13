package com.Direccion.ms.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.Direccion.ms.models.entities.Pais;
import com.Direccion.ms.models.entities.Region;
import com.Direccion.ms.models.request.ActualizarRegion;
import com.Direccion.ms.models.request.AgregarRegion;
import com.Direccion.ms.repositories.PaisRepository;
import com.Direccion.ms.repositories.RegionRepository;

//* Servicio que encapsula toda la lógica de negocio relacionada a regiones
//? @Service marca esta clase para que Spring la detecte e inyecte donde se necesite
@Service
public class RegionService {

    //* Spring inyecta automáticamente los repositorios gracias a @Autowired
    @Autowired
    private RegionRepository regionRepository;

    //* Se necesita PaisRepository para validar que el país existe antes de crear/actualizar
    @Autowired
    private PaisRepository paisRepository;

    //* Retorna la lista completa de regiones registradas en la BD
    public List<Region> obtenerTodasLasRegiones() {
        return regionRepository.findAll();
    }

    //* Retorna todas las regiones que pertenecen a un país dado
    //? Útil para poblar selects en cascada: al seleccionar un país, se cargan sus regiones
    public List<Region> obtenerRegionesPorPais(int idPais) {
        return regionRepository.findByPais_IdPais(idPais);
    }

    //* Busca una región por su ID
    //! Lanza HTTP 404 si el ID no existe en la BD
    public Region obtenerRegionPorId(int id_region) {
        return regionRepository.findById(id_region)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Región no encontrada."));
    }

    //* Crea una nueva región a partir del DTO y la persiste en la BD
    //! Lanza HTTP 404 si el país referenciado (id_pais) no existe — integridad garantizada
    public Region agregarRegion(AgregarRegion nuevaRegion) {
        Pais pais = paisRepository.findById(nuevaRegion.getId_pais())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "País no encontrado."));
        Region region = new Region();
        region.setNombre_region(nuevaRegion.getNombre_region());
        //* Se asigna el objeto Pais completo para que JPA gestione la FK
        region.setPais(pais);
        return regionRepository.save(region);
    }

    //* Actualiza los datos de una región existente
    //? id_region viene del path de la URL, no del body del request
    //? save() detecta que el ID ya existe en la BD y ejecuta UPDATE en lugar de INSERT
    public Region actualizarRegion(int id_region, ActualizarRegion actRegion) {
        Region region = regionRepository.findById(id_region)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Región no encontrada."));
        Pais pais = paisRepository.findById(actRegion.getId_pais())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "País no encontrado."));
        region.setNombre_region(actRegion.getNombre_region());
        region.setPais(pais);
        return regionRepository.save(region);
    }

    //* Elimina una región por su ID
    //! Lanza HTTP 404 si el ID no existe
    //! Precaución: si la región tiene ciudades asociadas, la BD lanzará un error de FK
    public String eliminarRegion(int id_region) {
        if (regionRepository.existsById(id_region)) {
            regionRepository.deleteById(id_region);
            return "Región eliminada correctamente.";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Región no encontrada.");
        }
    }

}
