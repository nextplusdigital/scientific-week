package br.com.romaninisistemas.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.romaninisistemas.domain.TipoAnimal;

import br.com.romaninisistemas.repository.TipoAnimalRepository;
import br.com.romaninisistemas.repository.search.TipoAnimalSearchRepository;
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
 * REST controller for managing TipoAnimal.
 */
@RestController
@RequestMapping("/api")
public class TipoAnimalResource {

    private final Logger log = LoggerFactory.getLogger(TipoAnimalResource.class);

    private static final String ENTITY_NAME = "tipoAnimal";

    private final TipoAnimalRepository tipoAnimalRepository;

    private final TipoAnimalSearchRepository tipoAnimalSearchRepository;

    public TipoAnimalResource(TipoAnimalRepository tipoAnimalRepository, TipoAnimalSearchRepository tipoAnimalSearchRepository) {
        this.tipoAnimalRepository = tipoAnimalRepository;
        this.tipoAnimalSearchRepository = tipoAnimalSearchRepository;
    }

    /**
     * POST  /tipo-animals : Create a new tipoAnimal.
     *
     * @param tipoAnimal the tipoAnimal to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tipoAnimal, or with status 400 (Bad Request) if the tipoAnimal has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tipo-animals")
    @Timed
    public ResponseEntity<TipoAnimal> createTipoAnimal(@Valid @RequestBody TipoAnimal tipoAnimal) throws URISyntaxException {
        log.debug("REST request to save TipoAnimal : {}", tipoAnimal);
        if (tipoAnimal.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new tipoAnimal cannot already have an ID")).body(null);
        }
        TipoAnimal result = tipoAnimalRepository.save(tipoAnimal);
        tipoAnimalSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/tipo-animals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tipo-animals : Updates an existing tipoAnimal.
     *
     * @param tipoAnimal the tipoAnimal to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tipoAnimal,
     * or with status 400 (Bad Request) if the tipoAnimal is not valid,
     * or with status 500 (Internal Server Error) if the tipoAnimal couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tipo-animals")
    @Timed
    public ResponseEntity<TipoAnimal> updateTipoAnimal(@Valid @RequestBody TipoAnimal tipoAnimal) throws URISyntaxException {
        log.debug("REST request to update TipoAnimal : {}", tipoAnimal);
        if (tipoAnimal.getId() == null) {
            return createTipoAnimal(tipoAnimal);
        }
        TipoAnimal result = tipoAnimalRepository.save(tipoAnimal);
        tipoAnimalSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tipoAnimal.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tipo-animals : get all the tipoAnimals.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tipoAnimals in body
     */
    @GetMapping("/tipo-animals")
    @Timed
    public ResponseEntity<List<TipoAnimal>> getAllTipoAnimals(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of TipoAnimals");
        Page<TipoAnimal> page = tipoAnimalRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tipo-animals");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/tipo-animals", method = RequestMethod.OPTIONS)
    @Timed
    public ResponseEntity<List<TipoAnimal>> getOptionsTipoAnimals(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of TipoAnimals");
        Page<TipoAnimal> page = tipoAnimalRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tipo-animals");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /tipo-animals/:id : get the "id" tipoAnimal.
     *
     * @param id the id of the tipoAnimal to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tipoAnimal, or with status 404 (Not Found)
     */
    @GetMapping("/tipo-animals/{id}")
    @Timed
    public ResponseEntity<TipoAnimal> getTipoAnimal(@PathVariable Long id) {
        log.debug("REST request to get TipoAnimal : {}", id);
        TipoAnimal tipoAnimal = tipoAnimalRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(tipoAnimal));
    }

    /**
     * DELETE  /tipo-animals/:id : delete the "id" tipoAnimal.
     *
     * @param id the id of the tipoAnimal to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tipo-animals/{id}")
    @Timed
    public ResponseEntity<Void> deleteTipoAnimal(@PathVariable Long id) {
        log.debug("REST request to delete TipoAnimal : {}", id);
        tipoAnimalRepository.delete(id);
        tipoAnimalSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/tipo-animals?query=:query : search for the tipoAnimal corresponding
     * to the query.
     *
     * @param query    the query of the tipoAnimal search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/tipo-animals")
    @Timed
    public ResponseEntity<List<TipoAnimal>> searchTipoAnimals(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of TipoAnimals for query {}", query);
        Page<TipoAnimal> page = tipoAnimalSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/tipo-animals");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
