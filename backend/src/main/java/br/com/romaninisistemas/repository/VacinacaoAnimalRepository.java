package br.com.romaninisistemas.repository;

import br.com.romaninisistemas.domain.VacinacaoAnimal;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the VacinacaoAnimal entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VacinacaoAnimalRepository extends JpaRepository<VacinacaoAnimal,Long> {
    
}
