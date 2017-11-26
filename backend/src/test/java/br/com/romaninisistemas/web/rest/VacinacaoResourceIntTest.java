package br.com.romaninisistemas.web.rest;

import br.com.romaninisistemas.RomaninisistemasApp;

import br.com.romaninisistemas.domain.Vacinacao;
import br.com.romaninisistemas.repository.PessoaRepository;
import br.com.romaninisistemas.repository.VacinacaoRepository;
import br.com.romaninisistemas.repository.search.VacinacaoSearchRepository;
import br.com.romaninisistemas.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the VacinacaoResource REST controller.
 *
 * @see VacinacaoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RomaninisistemasApp.class)
public class VacinacaoResourceIntTest {

    private static final LocalDate DEFAULT_DATA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private VacinacaoRepository vacinacaoRepository;

    @Autowired
    private VacinacaoSearchRepository vacinacaoSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restVacinacaoMockMvc;

    private Vacinacao vacinacao;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        VacinacaoResource vacinacaoResource = new VacinacaoResource(vacinacaoRepository, pessoaRepository, vacinacaoSearchRepository);
        this.restVacinacaoMockMvc = MockMvcBuilders.standaloneSetup(vacinacaoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vacinacao createEntity(EntityManager em) {
        Vacinacao vacinacao = new Vacinacao()
            .data(DEFAULT_DATA);
        return vacinacao;
    }

    @Before
    public void initTest() {
        vacinacaoSearchRepository.deleteAll();
        vacinacao = createEntity(em);
    }

    @Test
    @Transactional
    public void createVacinacao() throws Exception {
        int databaseSizeBeforeCreate = vacinacaoRepository.findAll().size();

        // Create the Vacinacao
        restVacinacaoMockMvc.perform(post("/api/vacinacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vacinacao)))
            .andExpect(status().isCreated());

        // Validate the Vacinacao in the database
        List<Vacinacao> vacinacaoList = vacinacaoRepository.findAll();
        assertThat(vacinacaoList).hasSize(databaseSizeBeforeCreate + 1);
        Vacinacao testVacinacao = vacinacaoList.get(vacinacaoList.size() - 1);
        assertThat(testVacinacao.getData()).isEqualTo(DEFAULT_DATA);

        // Validate the Vacinacao in Elasticsearch
        Vacinacao vacinacaoEs = vacinacaoSearchRepository.findOne(testVacinacao.getId());
        assertThat(vacinacaoEs).isEqualToComparingFieldByField(testVacinacao);
    }

    @Test
    @Transactional
    public void createVacinacaoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = vacinacaoRepository.findAll().size();

        // Create the Vacinacao with an existing ID
        vacinacao.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVacinacaoMockMvc.perform(post("/api/vacinacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vacinacao)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Vacinacao> vacinacaoList = vacinacaoRepository.findAll();
        assertThat(vacinacaoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDataIsRequired() throws Exception {
        int databaseSizeBeforeTest = vacinacaoRepository.findAll().size();
        // set the field null
        vacinacao.setData(null);

        // Create the Vacinacao, which fails.

        restVacinacaoMockMvc.perform(post("/api/vacinacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vacinacao)))
            .andExpect(status().isBadRequest());

        List<Vacinacao> vacinacaoList = vacinacaoRepository.findAll();
        assertThat(vacinacaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVacinacaos() throws Exception {
        // Initialize the database
        vacinacaoRepository.saveAndFlush(vacinacao);

        // Get all the vacinacaoList
        restVacinacaoMockMvc.perform(get("/api/vacinacaos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vacinacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA.toString())));
    }

    @Test
    @Transactional
    public void getVacinacao() throws Exception {
        // Initialize the database
        vacinacaoRepository.saveAndFlush(vacinacao);

        // Get the vacinacao
        restVacinacaoMockMvc.perform(get("/api/vacinacaos/{id}", vacinacao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(vacinacao.getId().intValue()))
            .andExpect(jsonPath("$.data").value(DEFAULT_DATA.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingVacinacao() throws Exception {
        // Get the vacinacao
        restVacinacaoMockMvc.perform(get("/api/vacinacaos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVacinacao() throws Exception {
        // Initialize the database
        vacinacaoRepository.saveAndFlush(vacinacao);
        vacinacaoSearchRepository.save(vacinacao);
        int databaseSizeBeforeUpdate = vacinacaoRepository.findAll().size();

        // Update the vacinacao
        Vacinacao updatedVacinacao = vacinacaoRepository.findOne(vacinacao.getId());
        updatedVacinacao
            .data(UPDATED_DATA);

        restVacinacaoMockMvc.perform(put("/api/vacinacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedVacinacao)))
            .andExpect(status().isOk());

        // Validate the Vacinacao in the database
        List<Vacinacao> vacinacaoList = vacinacaoRepository.findAll();
        assertThat(vacinacaoList).hasSize(databaseSizeBeforeUpdate);
        Vacinacao testVacinacao = vacinacaoList.get(vacinacaoList.size() - 1);
        assertThat(testVacinacao.getData()).isEqualTo(UPDATED_DATA);

        // Validate the Vacinacao in Elasticsearch
        Vacinacao vacinacaoEs = vacinacaoSearchRepository.findOne(testVacinacao.getId());
        assertThat(vacinacaoEs).isEqualToComparingFieldByField(testVacinacao);
    }

    @Test
    @Transactional
    public void updateNonExistingVacinacao() throws Exception {
        int databaseSizeBeforeUpdate = vacinacaoRepository.findAll().size();

        // Create the Vacinacao

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restVacinacaoMockMvc.perform(put("/api/vacinacaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vacinacao)))
            .andExpect(status().isCreated());

        // Validate the Vacinacao in the database
        List<Vacinacao> vacinacaoList = vacinacaoRepository.findAll();
        assertThat(vacinacaoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteVacinacao() throws Exception {
        // Initialize the database
        vacinacaoRepository.saveAndFlush(vacinacao);
        vacinacaoSearchRepository.save(vacinacao);
        int databaseSizeBeforeDelete = vacinacaoRepository.findAll().size();

        // Get the vacinacao
        restVacinacaoMockMvc.perform(delete("/api/vacinacaos/{id}", vacinacao.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean vacinacaoExistsInEs = vacinacaoSearchRepository.exists(vacinacao.getId());
        assertThat(vacinacaoExistsInEs).isFalse();

        // Validate the database is empty
        List<Vacinacao> vacinacaoList = vacinacaoRepository.findAll();
        assertThat(vacinacaoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchVacinacao() throws Exception {
        // Initialize the database
        vacinacaoRepository.saveAndFlush(vacinacao);
        vacinacaoSearchRepository.save(vacinacao);

        // Search the vacinacao
        restVacinacaoMockMvc.perform(get("/api/_search/vacinacaos?query=id:" + vacinacao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vacinacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vacinacao.class);
        Vacinacao vacinacao1 = new Vacinacao();
        vacinacao1.setId(1L);
        Vacinacao vacinacao2 = new Vacinacao();
        vacinacao2.setId(vacinacao1.getId());
        assertThat(vacinacao1).isEqualTo(vacinacao2);
        vacinacao2.setId(2L);
        assertThat(vacinacao1).isNotEqualTo(vacinacao2);
        vacinacao1.setId(null);
        assertThat(vacinacao1).isNotEqualTo(vacinacao2);
    }
}
