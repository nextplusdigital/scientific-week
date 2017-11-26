package br.com.romaninisistemas.repository;

import br.com.romaninisistemas.domain.Cor;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Cor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CorRepository extends JpaRepository<Cor,Long> {
    
}
