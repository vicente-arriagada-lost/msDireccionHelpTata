package com.Direccion.ms.repositories;

import com.Direccion.ms.models.entities.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

//* Repositorio JPA para la entidad Region
//* JpaRepository provee automáticamente: findAll, findById, save, deleteById, existsById, etc.
//? El segundo parámetro (Integer) es el tipo de la clave primaria (id_region)
public interface RegionRepository extends JpaRepository<Region, Integer> {

    //* Retorna todas las regiones que pertenecen a un país dado
    //? Spring Data JPA genera el SQL automáticamente a partir del nombre del método:
    //? "findBy" + "Pais" (atributo) + "_" + "IdPais" (campo del objeto Pais) → WHERE pais.id_pais = ?
    List<Region> findByPais_IdPais(int idPais);

}
