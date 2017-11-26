package br.com.romaninisistemas.web.rest;

import br.com.romaninisistemas.domain.Animal;
import br.com.romaninisistemas.domain.Remedio;
import br.com.romaninisistemas.domain.Vacinacao;
import br.com.romaninisistemas.repository.AnimalRepository;
import br.com.romaninisistemas.repository.RemedioRepository;
import br.com.romaninisistemas.repository.VacinacaoRepository;
import br.com.romaninisistemas.web.rest.vm.VacinacaoAnimalVM;
import com.codahale.metrics.annotation.Timed;
import br.com.romaninisistemas.domain.VacinacaoAnimal;

import br.com.romaninisistemas.repository.VacinacaoAnimalRepository;
import br.com.romaninisistemas.repository.search.VacinacaoAnimalSearchRepository;
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
 * REST controller for managing VacinacaoAnimal.
 */
@RestController
@RequestMapping("/api")
public class VacinacaoAnimalResource {

    private final Logger log = LoggerFactory.getLogger(VacinacaoAnimalResource.class);

    private static final String ENTITY_NAME = "vacinacaoAnimal";

    private final VacinacaoAnimalRepository vacinacaoAnimalRepository;

    private final VacinacaoRepository vacinacaoRepository;

    private final RemedioRepository remedioRepository;

    private final AnimalRepository animalRepository;

    private final VacinacaoAnimalSearchRepository vacinacaoAnimalSearchRepository;

    public VacinacaoAnimalResource(VacinacaoAnimalRepository vacinacaoAnimalRepository, VacinacaoRepository vacinacaoRepository, RemedioRepository remedioRepository, AnimalRepository animalRepository, VacinacaoAnimalSearchRepository vacinacaoAnimalSearchRepository) {
        this.vacinacaoAnimalRepository = vacinacaoAnimalRepository;
        this.vacinacaoRepository = vacinacaoRepository;
        this.remedioRepository = remedioRepository;
        this.animalRepository = animalRepository;
        this.vacinacaoAnimalSearchRepository = vacinacaoAnimalSearchRepository;
    }

    /**
     * POST  /vacinacao-animals : Create a new vacinacaoAnimal.
     *
     * @param vacinacaoAnimal the vacinacaoAnimal to create
     * @return the ResponseEntity with status 201 (Created) and with body the new vacinacaoAnimal, or with status 400 (Bad Request) if the vacinacaoAnimal has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/vacinacao-animals")
    @Timed
    public ResponseEntity<VacinacaoAnimal> createVacinacaoAnimal(@Valid @RequestBody VacinacaoAnimalVM vm) throws URISyntaxException {
        log.debug("REST request to save VacinacaoAnimal : {}", vm);
        VacinacaoAnimal vacinacaoAnimal = convertToVacinacao(vm);
        if (vacinacaoAnimal.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new vacinacaoAnimal cannot already have an ID")).body(null);
        }
        VacinacaoAnimal result = vacinacaoAnimalRepository.save(vacinacaoAnimal);
        vacinacaoAnimalSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/vacinacao-animals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    private VacinacaoAnimal convertToVacinacao(@Valid @RequestBody VacinacaoAnimalVM vm) {
        Remedio remedio = remedioRepository.findOne(Long.parseLong(vm.getNomeRemedio()));
        log.debug("REST request to save Vacinacao Animal, remedio : {}", remedio);
        Animal animal = animalRepository.findOne(Long.parseLong(vm.getDescricaoAnimal()));
        log.debug("REST request to save Vacinacao Animal, animal: {}", animal);
        Vacinacao vacinacao = vacinacaoRepository.findOne(Long.parseLong(vm.getDescricaoVacinacao()));
        log.debug("REST request to save Vacinacao Animal, vacinacao: {}", vacinacao);

        VacinacaoAnimal vacinacaoAnimal = new VacinacaoAnimal();
        vacinacaoAnimal.setAnimal(animal);
        vacinacaoAnimal.setVacinacao(vacinacao);
        vacinacaoAnimal.setRemedio(remedio);
        vacinacaoAnimal.setIdentificacao(vm.getIdentificacao());
        vacinacaoAnimal.setQuantidade(vm.getQuantidade());
        return vacinacaoAnimal;
    }

    private Page<VacinacaoAnimalVM> converterToVm(Page<VacinacaoAnimal> all) {
        Page<VacinacaoAnimalVM> dtoPage = all.map(new Converter<VacinacaoAnimal, VacinacaoAnimalVM>() {
            @Override
            public VacinacaoAnimalVM convert(VacinacaoAnimal entity) {
                VacinacaoAnimalVM dto = new VacinacaoAnimalVM();
                dto.setId(entity.getId());
                dto.setDescricaoAnimal(entity.getAnimal().getDescricao());
                dto.setDescricaoVacinacao(entity.getVacinacao().getNome());
                dto.setNomeRemedio(entity.getRemedio().getNome());
                dto.setIdentificacao(entity.getIdentificacao());
                dto.setQuantidade(entity.getQuantidade());
                return dto;
            }
        });
        return dtoPage;
    }

    /**
     * PUT  /vacinacao-animals : Updates an existing vacinacaoAnimal.
     *
     * @param vm the vacinacaoAnimal to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated vacinacaoAnimal,
     * or with status 400 (Bad Request) if the vacinacaoAnimal is not valid,
     * or with status 500 (Internal Server Error) if the vacinacaoAnimal couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/vacinacao-animals")
    @Timed
    public ResponseEntity<VacinacaoAnimal> updateVacinacaoAnimal(@Valid @RequestBody VacinacaoAnimalVM vm) throws URISyntaxException {
        log.debug("REST request to update VacinacaoAnimal : {}", vm);
        VacinacaoAnimal vacinacaoAnimal = convertToVacinacao(vm);
        VacinacaoAnimal result = vacinacaoAnimalRepository.save(vacinacaoAnimal);
        vacinacaoAnimalSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, vacinacaoAnimal.getId().toString()))
            .body(result);
    }

    /**
     * GET  /vacinacao-animals : get all the vacinacaoAnimals.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of vacinacaoAnimals in body
     */
    @GetMapping("/vacinacao-animals")
    @Timed
    public ResponseEntity<List<VacinacaoAnimalVM>> getAllVacinacaoAnimals(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of VacinacaoAnimals");
        Page<VacinacaoAnimalVM> page = converterToVm(vacinacaoAnimalRepository.findAll(pageable));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/vacinacao-animals");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /vacinacao-animals/:id : get the "id" vacinacaoAnimal.
     *
     * @param id the id of the vacinacaoAnimal to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the vacinacaoAnimal, or with status 404 (Not Found)
     */
    @GetMapping("/vacinacao-animals/{id}")
    @Timed
    public ResponseEntity<VacinacaoAnimal> getVacinacaoAnimal(@PathVariable Long id) {
        log.debug("REST request to get VacinacaoAnimal : {}", id);
        VacinacaoAnimal vacinacaoAnimal = vacinacaoAnimalRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(vacinacaoAnimal));
    }

    /**
     * DELETE  /vacinacao-animals/:id : delete the "id" vacinacaoAnimal.
     *
     * @param id the id of the vacinacaoAnimal to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/vacinacao-animals/{id}")
    @Timed
    public ResponseEntity<Void> deleteVacinacaoAnimal(@PathVariable Long id) {
        log.debug("REST request to delete VacinacaoAnimal : {}", id);
        vacinacaoAnimalRepository.delete(id);
        vacinacaoAnimalSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/vacinacao-animals?query=:query : search for the vacinacaoAnimal corresponding
     * to the query.
     *
     * @param query    the query of the vacinacaoAnimal search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/vacinacao-animals")
    @Timed
    public ResponseEntity<List<VacinacaoAnimal>> searchVacinacaoAnimals(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of VacinacaoAnimals for query {}", query);
        Page<VacinacaoAnimal> page = vacinacaoAnimalSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/vacinacao-animals");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
