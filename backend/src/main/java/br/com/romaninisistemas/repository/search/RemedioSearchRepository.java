package br.com.romaninisistemas.repository.search;

import br.com.romaninisistemas.domain.Remedio;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Remedio entity.
 */
public interface RemedioSearchRepository extends ElasticsearchRepository<Remedio, Long> {
}
