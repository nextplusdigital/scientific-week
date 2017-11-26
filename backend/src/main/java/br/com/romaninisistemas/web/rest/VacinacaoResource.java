package br.com.romaninisistemas.web.rest;

import br.com.romaninisistemas.domain.Pessoa;
import br.com.romaninisistemas.repository.PessoaRepository;
import br.com.romaninisistemas.web.rest.vm.VacinacaoVM;
import com.codahale.metrics.annotation.Timed;
import br.com.romaninisistemas.domain.Vacinacao;

import br.com.romaninisistemas.repository.VacinacaoRepository;
import br.com.romaninisistemas.repository.search.VacinacaoSearchRepository;
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

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Vacinacao.
 */
@RestController
@RequestMapping("/api")
public class VacinacaoResource {

    private final Logger log = LoggerFactory.getLogger(VacinacaoResource.class);

    private static final String ENTITY_NAME = "vacinacao";

    private final VacinacaoRepository vacinacaoRepository;

    private final PessoaRepository pessoaRepository;

    private final VacinacaoSearchRepository vacinacaoSearchRepository;

    public VacinacaoResource(VacinacaoRepository vacinacaoRepository, PessoaRepository pessoaRepository, VacinacaoSearchRepository vacinacaoSearchRepository) {
        this.vacinacaoRepository = vacinacaoRepository;
        this.pessoaRepository = pessoaRepository;
        this.vacinacaoSearchRepository = vacinacaoSearchRepository;
    }

    /**
     * POST  /vacinacaos : Create a new vacinacao.
     *
     * @param vm the vacinacao to create
     * @return the ResponseEntity with status 201 (Created) and with body the new vacinacao, or with status 400 (Bad Request) if the vacinacao has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/vacinacaos")
    @Timed
    public ResponseEntity<Vacinacao> createVacinacao(@Valid @RequestBody VacinacaoVM vm) throws URISyntaxException {
        log.debug("REST request to save Vacinacao : {}", vm);
        Vacinacao vacinacao = convertToVacinacao(vm);
        if (vacinacao.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new vacinacao cannot already have an ID")).body(null);
        }
        Vacinacao result = vacinacaoRepository.save(vacinacao);
        vacinacaoSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/vacinacaos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    private Vacinacao convertToVacinacao(@Valid @RequestBody VacinacaoVM vm) {
        Pessoa pessoa = pessoaRepository.findOne(Long.parseLong(vm.getLogin()));
        log.debug("REST request to save Vacinacao, pessoa : {}", pessoa);
        Vacinacao vacinacao = new Vacinacao();
        vacinacao.setData(vm.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        vacinacao.setNome(vm.getNome());
        vacinacao.setResponsavel(pessoa);
        return vacinacao;
    }

    private Page<VacinacaoVM> converterToVm(Page<Vacinacao> all) {
        Page<VacinacaoVM> dtoPage = all.map(new Converter<Vacinacao, VacinacaoVM>() {
            @Override
            public VacinacaoVM convert(Vacinacao entity) {
                VacinacaoVM dto = new VacinacaoVM();
                dto.setId(entity.getId());
                dto.setDate(Date.from(entity.getData().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                dto.setNome(entity.getNome());
                dto.setLogin(entity.getResponsavel().getUsuario().getLogin());
                return dto;
            }
        });
        return dtoPage;
    }

    /**
     * PUT  /vacinacaos : Updates an existing vacinacao.
     *
     * @param vm the vacinacao to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated vacinacao,
     * or with status 400 (Bad Request) if the vacinacao is not valid,
     * or with status 500 (Internal Server Error) if the vacinacao couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/vacinacaos")
    @Timed
    public ResponseEntity<Vacinacao> updateVacinacao(@Valid @RequestBody VacinacaoVM vm) throws URISyntaxException {
        log.debug("REST request to update Vacinacao : {}", vm);
        Vacinacao vacinacao = convertToVacinacao(vm);
        Vacinacao result = vacinacaoRepository.save(vacinacao);
        vacinacaoSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, vacinacao.getId().toString()))
            .body(result);
    }

    /**
     * GET  /vacinacaos : get all the vacinacaos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of vacinacaos in body
     */
    @GetMapping("/vacinacaos")
    @Timed
    public ResponseEntity<List<VacinacaoVM>> getAllVacinacaos(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Vacinacaos");
        Page<VacinacaoVM> page = converterToVm(vacinacaoRepository.findAll(pageable));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/vacinacaos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /vacinacaos/:id : get the "id" vacinacao.
     *
     * @param id the id of the vacinacao to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the vacinacao, or with status 404 (Not Found)
     */
    @GetMapping("/vacinacaos/{id}")
    @Timed
    public ResponseEntity<Vacinacao> getVacinacao(@PathVariable Long id) {
        log.debug("REST request to get Vacinacao : {}", id);
        Vacinacao vacinacao = vacinacaoRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(vacinacao));
    }

    /**
     * DELETE  /vacinacaos/:id : delete the "id" vacinacao.
     *
     * @param id the id of the vacinacao to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/vacinacaos/{id}")
    @Timed
    public ResponseEntity<Void> deleteVacinacao(@PathVariable Long id) {
        log.debug("REST request to delete Vacinacao : {}", id);
        vacinacaoRepository.delete(id);
        vacinacaoSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/vacinacaos?query=:query : search for the vacinacao corresponding
     * to the query.
     *
     * @param query    the query of the vacinacao search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/vacinacaos")
    @Timed
    public ResponseEntity<List<Vacinacao>> searchVacinacaos(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Vacinacaos for query {}", query);
        Page<Vacinacao> page = vacinacaoSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/vacinacaos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
