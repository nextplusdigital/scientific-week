package br.com.romaninisistemas.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.romaninisistemas.domain.Cor;

import br.com.romaninisistemas.repository.CorRepository;
import br.com.romaninisistemas.repository.search.CorSearchRepository;
import br.com.romaninisistemas.web.rest.util.HeaderUtil;
import br.com.romaninisistemas.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Cor.
 */
@RestController
@RequestMapping("/api")
public class CorResource {

    private final Logger log = LoggerFactory.getLogger(CorResource.class);

    private static final String ENTITY_NAME = "cor";

    private final CorRepository corRepository;

    private final CorSearchRepository corSearchRepository;

    public CorResource(CorRepository corRepository, CorSearchRepository corSearchRepository) {
        this.corRepository = corRepository;
        this.corSearchRepository = corSearchRepository;
    }

    /**
     * POST  /cors : Create a new cor.
     *
     * @param cor the cor to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cor, or with status 400 (Bad Request) if the cor has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cors")
    @Timed
    public ResponseEntity<Cor> createCor(@Valid @RequestBody Cor cor) throws URISyntaxException {
        log.debug("REST request to save Cor : {}", cor);
        if (cor.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new cor cannot already have an ID")).body(null);
        }
        Cor result = corRepository.save(cor);
        corSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/cors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cors : Updates an existing cor.
     *
     * @param cor the cor to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cor,
     * or with status 400 (Bad Request) if the cor is not valid,
     * or with status 500 (Internal Server Error) if the cor couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cors")
    @Timed
    public ResponseEntity<Cor> updateCor(@Valid @RequestBody Cor cor) throws URISyntaxException {
        log.debug("REST request to update Cor : {}", cor);
        if (cor.getId() == null) {
            return createCor(cor);
        }
        Cor result = corRepository.save(cor);
        corSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cor.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cors : get all the cors.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cors in body
     */
    @GetMapping("/cors")
    @Timed
    public ResponseEntity<List<Cor>> getAllCors(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Cors");
        Page<Cor> page = corRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cors");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /cors/:id : get the "id" cor.
     *
     * @param id the id of the cor to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cor, or with status 404 (Not Found)
     */
    @GetMapping("/cors/{id}")
    @Timed
    public ResponseEntity<Cor> getCor(@PathVariable Long id) {
        log.debug("REST request to get Cor : {}", id);
        Cor cor = corRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cor));
    }

    /**
     * DELETE  /cors/:id : delete the "id" cor.
     *
     * @param id the id of the cor to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cors/{id}")
    @Timed
    public ResponseEntity<Void> deleteCor(@PathVariable Long id) {
        log.debug("REST request to delete Cor : {}", id);
        corRepository.delete(id);
        corSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/cors?query=:query : search for the cor corresponding
     * to the query.
     *
     * @param query the query of the cor search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/cors")
    @Timed
    public ResponseEntity<List<Cor>> searchCors(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Cors for query {}", query);
        Page<Cor> page = corSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/cors");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
