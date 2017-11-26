package br.com.romaninisistemas.repository.search;

import br.com.romaninisistemas.domain.TipoAnimal;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the TipoAnimal entity.
 */
public interface TipoAnimalSearchRepository extends ElasticsearchRepository<TipoAnimal, Long> {
}
