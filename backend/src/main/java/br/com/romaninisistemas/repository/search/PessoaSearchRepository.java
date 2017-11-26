package br.com.romaninisistemas.repository.search;

import br.com.romaninisistemas.domain.Pessoa;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Pessoa entity.
 */
public interface PessoaSearchRepository extends ElasticsearchRepository<Pessoa, Long> {
}
