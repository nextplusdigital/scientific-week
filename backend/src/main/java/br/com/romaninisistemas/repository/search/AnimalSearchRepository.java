package br.com.romaninisistemas.repository.search;

import br.com.romaninisistemas.domain.Animal;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Animal entity.
 */
public interface AnimalSearchRepository extends ElasticsearchRepository<Animal, Long> {
}
