package br.com.romaninisistemas.repository.search;

import br.com.romaninisistemas.domain.Cor;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Cor entity.
 */
public interface CorSearchRepository extends ElasticsearchRepository<Cor, Long> {
}
