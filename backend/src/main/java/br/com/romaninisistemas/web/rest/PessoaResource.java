package br.com.romaninisistemas.web.rest;

import br.com.romaninisistemas.web.rest.vm.PessoaVM;
import com.codahale.metrics.annotation.Timed;
import br.com.romaninisistemas.domain.Pessoa;

import br.com.romaninisistemas.repository.PessoaRepository;
import br.com.romaninisistemas.repository.search.PessoaSearchRepository;
import br.com.romaninisistemas.web.rest.util.HeaderUtil;
import br.com.romaninisistemas.web.rest.util.PaginationUtil;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Pessoa.
 */
@RestController
@RequestMapping("/api")
public class PessoaResource {

    private final Logger log = LoggerFactory.getLogger(PessoaResource.class);

    private static final String ENTITY_NAME = "pessoa";

    private final PessoaRepository pessoaRepository;

    private final PessoaSearchRepository pessoaSearchRepository;

    public PessoaResource(PessoaRepository pessoaRepository, PessoaSearchRepository pessoaSearchRepository) {
        this.pessoaRepository = pessoaRepository;
        this.pessoaSearchRepository = pessoaSearchRepository;
    }

    /**
     * POST  /pessoas : Create a new pessoa.
     *
     * @param pessoa the pessoa to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pessoa, or with status 400 (Bad Request) if the pessoa has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pessoas")
    @Timed
    public ResponseEntity<Pessoa> createPessoa(@RequestBody Pessoa pessoa) throws URISyntaxException {
        log.debug("REST request to save Pessoa : {}", pessoa);
        if (pessoa.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new pessoa cannot already have an ID")).body(null);
        }
        Pessoa result = pessoaRepository.save(pessoa);
        pessoaSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/pessoas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pessoas : Updates an existing pessoa.
     *
     * @param pessoa the pessoa to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pessoa,
     * or with status 400 (Bad Request) if the pessoa is not valid,
     * or with status 500 (Internal Server Error) if the pessoa couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pessoas")
    @Timed
    public ResponseEntity<Pessoa> updatePessoa(@RequestBody Pessoa pessoa) throws URISyntaxException {
        log.debug("REST request to update Pessoa : {}", pessoa);
        if (pessoa.getId() == null) {
            return createPessoa(pessoa);
        }
        Pessoa result = pessoaRepository.save(pessoa);
        pessoaSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pessoa.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pessoas : get all the pessoas.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of pessoas in body
     */
    @GetMapping("/pessoas")
    @Timed
    public ResponseEntity<List<PessoaVM>> getAllPessoas(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Pessoas");
        Page<PessoaVM> page = converteDTO(pessoaRepository.findAll(pageable));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pessoas");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    private Page<PessoaVM> converteDTO(Page<Pessoa> all) {
        Page<PessoaVM> dtoPage = all.map(new Converter<Pessoa, PessoaVM>() {
            @Override
            public PessoaVM convert(Pessoa entity) {
                PessoaVM dto = new PessoaVM();
                dto.setId(entity.getId());
                dto.setDescricao(entity.getUsuario().getLogin());
                return dto;
            }
        });
        return dtoPage;
    }

    /**
     * GET  /pessoas/:id : get the "id" pessoa.
     *
     * @param id the id of the pessoa to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pessoa, or with status 404 (Not Found)
     */
    @GetMapping("/pessoas/{id}")
    @Timed
    public ResponseEntity<Pessoa> getPessoa(@PathVariable Long id) {
        log.debug("REST request to get Pessoa : {}", id);
        Pessoa pessoa = pessoaRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pessoa));
    }

    /**
     * DELETE  /pessoas/:id : delete the "id" pessoa.
     *
     * @param id the id of the pessoa to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pessoas/{id}")
    @Timed
    public ResponseEntity<Void> deletePessoa(@PathVariable Long id) {
        log.debug("REST request to delete Pessoa : {}", id);
        pessoaRepository.delete(id);
        pessoaSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/pessoas?query=:query : search for the pessoa corresponding
     * to the query.
     *
     * @param query    the query of the pessoa search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/pessoas")
    @Timed
    public ResponseEntity<List<Pessoa>> searchPessoas(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Pessoas for query {}", query);
        Page<Pessoa> page = pessoaSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/pessoas");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
