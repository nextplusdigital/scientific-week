package br.com.romaninisistemas.repository.search;

import br.com.romaninisistemas.domain.Vacinacao;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Vacinacao entity.
 */
public interface VacinacaoSearchRepository extends ElasticsearchRepository<Vacinacao, Long> {
}
