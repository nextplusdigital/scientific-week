package br.com.romaninisistemas.repository;

import br.com.romaninisistemas.domain.TipoAnimal;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TipoAnimal entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipoAnimalRepository extends JpaRepository<TipoAnimal,Long> {
    
}
