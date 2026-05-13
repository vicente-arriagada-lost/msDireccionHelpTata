package com.Direccion.ms.repositories;

import com.Direccion.ms.models.entities.Direccion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

//* Repositorio JPA para la entidad Direccion
//* JpaRepository provee automáticamente: findAll, findById, save, deleteById, existsById, etc.
//? El segundo parámetro (Integer) es el tipo de la clave primaria (id_direccion)
public interface DireccionRepository extends JpaRepository<Direccion, Integer> {

    //* Retorna todas las direcciones que pertenecen a una comuna dada
    //? Spring Data JPA genera el SQL a partir del nombre del método:
    //? "findBy" + "Comuna" (atributo) + "_" + "IdComuna" (campo del objeto Comuna) → WHERE comuna.id_comuna = ?
    List<Direccion> findByComuna_IdComuna(int idComuna);

}
