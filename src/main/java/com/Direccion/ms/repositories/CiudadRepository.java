package com.Direccion.ms.repositories;

import com.Direccion.ms.models.entities.Ciudad;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

//* Repositorio JPA para la entidad Ciudad
//* JpaRepository provee automáticamente: findAll, findById, save, deleteById, existsById, etc.
//? El segundo parámetro (Integer) es el tipo de la clave primaria (id_ciudad)
public interface CiudadRepository extends JpaRepository<Ciudad, Integer> {

    //* Retorna todas las ciudades que pertenecen a una región dada
    //? Spring Data JPA genera el SQL a partir del nombre del método:
    //? "findBy" + "Region" (atributo) + "_" + "IdRegion" (campo del objeto Region) → WHERE region.id_region = ?
    List<Ciudad> findByRegion_IdRegion(int idRegion);

}
