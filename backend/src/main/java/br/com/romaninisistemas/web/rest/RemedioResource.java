package br.com.romaninisistemas.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.romaninisistemas.domain.Remedio;

import br.com.romaninisistemas.repository.RemedioRepository;
import br.com.romaninisistemas.repository.search.RemedioSearchRepository;
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
 * REST controller for managing Remedio.
 */
@RestController
@RequestMapping("/api")
public class RemedioResource {

    private final Logger log = LoggerFactory.getLogger(RemedioResource.class);

    private static final String ENTITY_NAME = "remedio";

    private final RemedioRepository remedioRepository;

    private final RemedioSearchRepository remedioSearchRepository;

    public RemedioResource(RemedioRepository remedioRepository, RemedioSearchRepository remedioSearchRepository) {
        this.remedioRepository = remedioRepository;
        this.remedioSearchRepository = remedioSearchRepository;
    }

    /**
     * POST  /remedios : Create a new remedio.
     *
     * @param remedio the remedio to create
     * @return the ResponseEntity with status 201 (Created) and with body the new remedio, or with status 400 (Bad Request) if the remedio has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/remedios")
    @Timed
    public ResponseEntity<Remedio> createRemedio(@Valid @RequestBody Remedio remedio) throws URISyntaxException {
        log.debug("REST request to save Remedio : {}", remedio);
        if (remedio.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new remedio cannot already have an ID")).body(null);
        }
        Remedio result = remedioRepository.save(remedio);
        remedioSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/remedios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /remedios : Updates an existing remedio.
     *
     * @param remedio the remedio to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated remedio,
     * or with status 400 (Bad Request) if the remedio is not valid,
     * or with status 500 (Internal Server Error) if the remedio couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/remedios")
    @Timed
    public ResponseEntity<Remedio> updateRemedio(@Valid @RequestBody Remedio remedio) throws URISyntaxException {
        log.debug("REST request to update Remedio : {}", remedio);
        if (remedio.getId() == null) {
            return createRemedio(remedio);
        }
        Remedio result = remedioRepository.save(remedio);
        remedioSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, remedio.getId().toString()))
            .body(result);
    }

    /**
     * GET  /remedios : get all the remedios.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of remedios in body
     */
    @GetMapping("/remedios")
    @Timed
    public ResponseEntity<List<Remedio>> getAllRemedios(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Remedios");
        Page<Remedio> page = remedioRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/remedios");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /remedios/:id : get the "id" remedio.
     *
     * @param id the id of the remedio to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the remedio, or with status 404 (Not Found)
     */
    @GetMapping("/remedios/{id}")
    @Timed
    public ResponseEntity<Remedio> getRemedio(@PathVariable Long id) {
        log.debug("REST request to get Remedio : {}", id);
        Remedio remedio = remedioRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(remedio));
    }

    /**
     * DELETE  /remedios/:id : delete the "id" remedio.
     *
     * @param id the id of the remedio to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/remedios/{id}")
    @Timed
    public ResponseEntity<Void> deleteRemedio(@PathVariable Long id) {
        log.debug("REST request to delete Remedio : {}", id);
        remedioRepository.delete(id);
        remedioSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/remedios?query=:query : search for the remedio corresponding
     * to the query.
     *
     * @param query the query of the remedio search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/remedios")
    @Timed
    public ResponseEntity<List<Remedio>> searchRemedios(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Remedios for query {}", query);
        Page<Remedio> page = remedioSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/remedios");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
