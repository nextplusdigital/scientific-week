package br.com.romaninisistemas.repository;

import br.com.romaninisistemas.domain.Remedio;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Remedio entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RemedioRepository extends JpaRepository<Remedio,Long> {
    
}
