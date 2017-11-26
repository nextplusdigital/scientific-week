package br.com.romaninisistemas.repository;

import br.com.romaninisistemas.domain.Vacinacao;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Vacinacao entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VacinacaoRepository extends JpaRepository<Vacinacao,Long> {
    
}
