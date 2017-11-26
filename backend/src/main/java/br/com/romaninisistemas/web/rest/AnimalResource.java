package br.com.romaninisistemas.web.rest;

import br.com.romaninisistemas.domain.TipoAnimal;
import br.com.romaninisistemas.repository.TipoAnimalRepository;
import br.com.romaninisistemas.web.rest.vm.AnimalVM;
import com.codahale.metrics.annotation.Timed;
import br.com.romaninisistemas.domain.Animal;

import br.com.romaninisistemas.repository.AnimalRepository;
import br.com.romaninisistemas.repository.search.AnimalSearchRepository;
import br.com.romaninisistemas.web.rest.util.HeaderUtil;
import br.com.romaninisistemas.web.rest.util.PaginationUtil;
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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Animal.
 */
@RestController
@RequestMapping("/api")
public class AnimalResource {

    private final Logger log = LoggerFactory.getLogger(AnimalResource.class);

    private static final String ENTITY_NAME = "animal";

    private final AnimalRepository animalRepository;

    private final TipoAnimalRepository tipoAnimalRepository;

    private final AnimalSearchRepository animalSearchRepository;

    public AnimalResource(AnimalRepository animalRepository, TipoAnimalRepository tipoAnimalRepository, AnimalSearchRepository animalSearchRepository) {
        this.animalRepository = animalRepository;
        this.tipoAnimalRepository = tipoAnimalRepository;
        this.animalSearchRepository = animalSearchRepository;
    }

    /**
     * POST  /animals : Create a new animal.
     *
     * @param vm the animal to create
     * @return the ResponseEntity with status 201 (Created) and with body the new animal, or with status 400 (Bad Request) if the animal has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/animals")
    @Timed
    public ResponseEntity<Animal> createAnimal(@Valid @RequestBody AnimalVM vm) throws URISyntaxException {
        log.debug("REST request to save Animal : {}", vm);
        Animal animal = convertToAnimal(vm);
        if (animal.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new animal cannot already have an ID")).body(null);
        }
        Animal result = animalRepository.save(animal);
        animalSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/animals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    private Animal convertToAnimal(@Valid @RequestBody AnimalVM vm) {
        TipoAnimal tipoAnimal = tipoAnimalRepository.findOne(Long.parseLong(vm.getTipoAnimal()));
        log.debug("REST request to save Animal, tipoAnimal : {}", tipoAnimal);
        Animal animal = new Animal();
        animal.setDescricao(vm.getDescricao());
        animal.setTipoAnimal(tipoAnimal);
        return animal;
    }

    private Page<AnimalVM> converterToVm(Page<Animal> all) {
        Page<AnimalVM> dtoPage = all.map(new Converter<Animal, AnimalVM>() {
            @Override
            public AnimalVM convert(Animal entity) {
                AnimalVM dto = new AnimalVM();
                dto.setId(entity.getId());
                dto.setDescricao(entity.getDescricao());
                dto.setTipoAnimal(entity.getTipoAnimal().getNome());
                return dto;
            }
        });
        return dtoPage;
    }

    /**
     * PUT  /animals : Updates an existing animal.
     *
     * @param vm the animal to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated animal,
     * or with status 400 (Bad Request) if the animal is not valid,
     * or with status 500 (Internal Server Error) if the animal couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/animals")
    @Timed
    public ResponseEntity<Animal> updateAnimal(@Valid @RequestBody AnimalVM vm) throws URISyntaxException {
        log.debug("REST request to update Animal : {}", vm);
        Animal animal = convertToAnimal(vm);
        Animal result = animalRepository.save(animal);
        animalSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, animal.getId().toString()))
            .body(result);
    }

    /**
     * GET  /animals : get all the animals.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of animals in body
     */
    @GetMapping("/animals")
    @Timed
    public ResponseEntity<List<AnimalVM>> getAllAnimals(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Animals");
        Page<AnimalVM> page = converterToVm(animalRepository.findAll(pageable));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/animals");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


    /**
     * GET  /animals/:id : get the "id" animal.
     *
     * @param id the id of the animal to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the animal, or with status 404 (Not Found)
     */
    @GetMapping("/animals/{id}")
    @Timed
    public ResponseEntity<Animal> getAnimal(@PathVariable Long id) {
        log.debug("REST request to get Animal : {}", id);
        Animal animal = animalRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(animal));
    }

    /**
     * DELETE  /animals/:id : delete the "id" animal.
     *
     * @param id the id of the animal to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/animals/{id}")
    @Timed
    public ResponseEntity<Void> deleteAnimal(@PathVariable Long id) {
        log.debug("REST request to delete Animal : {}", id);
        animalRepository.delete(id);
        animalSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/animals?query=:query : search for the animal corresponding
     * to the query.
     *
     * @param query    the query of the animal search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/animals")
    @Timed
    public ResponseEntity<List<Animal>> searchAnimals(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Animals for query {}", query);
        Page<Animal> page = animalSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/animals");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
