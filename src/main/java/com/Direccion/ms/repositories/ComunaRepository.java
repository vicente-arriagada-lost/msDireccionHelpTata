package com.Direccion.ms.repositories;

import com.Direccion.ms.models.entities.Comuna;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

//* Repositorio JPA para la entidad Comuna
//* JpaRepository provee automáticamente: findAll, findById, save, deleteById, existsById, etc.
//? El segundo parámetro (Integer) es el tipo de la clave primaria (id_comuna)
public interface ComunaRepository extends JpaRepository<Comuna, Integer> {

    //* Retorna todas las comunas que pertenecen a una ciudad dada
    //? Spring Data JPA genera el SQL a partir del nombre del método:
    //? "findBy" + "Ciudad" (atributo) + "_" + "IdCiudad" (campo del objeto Ciudad) → WHERE ciudad.id_ciudad = ?
    List<Comuna> findByCiudad_IdCiudad(int idCiudad);

}
