package br.com.romaninisistemas.repository;

import br.com.romaninisistemas.domain.Animal;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Animal entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnimalRepository extends JpaRepository<Animal,Long> {
    
}
