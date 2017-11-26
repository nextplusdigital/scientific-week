package br.com.romaninisistemas.repository.search;

import br.com.romaninisistemas.domain.VacinacaoAnimal;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the VacinacaoAnimal entity.
 */
public interface VacinacaoAnimalSearchRepository extends ElasticsearchRepository<VacinacaoAnimal, Long> {
}
