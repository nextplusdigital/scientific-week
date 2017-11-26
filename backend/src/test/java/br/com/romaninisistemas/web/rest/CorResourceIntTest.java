package br.com.romaninisistemas.web.rest;

import br.com.romaninisistemas.RomaninisistemasApp;

import br.com.romaninisistemas.domain.Cor;
import br.com.romaninisistemas.repository.CorRepository;
import br.com.romaninisistemas.repository.search.CorSearchRepository;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CorResource REST controller.
 *
 * @see CorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RomaninisistemasApp.class)
public class CorResourceIntTest {

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    @Autowired
    private CorRepository corRepository;

    @Autowired
    private CorSearchRepository corSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCorMockMvc;

    private Cor cor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CorResource corResource = new CorResource(corRepository, corSearchRepository);
        this.restCorMockMvc = MockMvcBuilders.standaloneSetup(corResource)
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
    public static Cor createEntity(EntityManager em) {
        Cor cor = new Cor()
            .descricao(DEFAULT_DESCRICAO);
        return cor;
    }

    @Before
    public void initTest() {
        corSearchRepository.deleteAll();
        cor = createEntity(em);
    }

    @Test
    @Transactional
    public void createCor() throws Exception {
        int databaseSizeBeforeCreate = corRepository.findAll().size();

        // Create the Cor
        restCorMockMvc.perform(post("/api/cors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cor)))
            .andExpect(status().isCreated());

        // Validate the Cor in the database
        List<Cor> corList = corRepository.findAll();
        assertThat(corList).hasSize(databaseSizeBeforeCreate + 1);
        Cor testCor = corList.get(corList.size() - 1);
        assertThat(testCor.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);

        // Validate the Cor in Elasticsearch
        Cor corEs = corSearchRepository.findOne(testCor.getId());
        assertThat(corEs).isEqualToComparingFieldByField(testCor);
    }

    @Test
    @Transactional
    public void createCorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = corRepository.findAll().size();

        // Create the Cor with an existing ID
        cor.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCorMockMvc.perform(post("/api/cors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cor)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Cor> corList = corRepository.findAll();
        assertThat(corList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDescricaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = corRepository.findAll().size();
        // set the field null
        cor.setDescricao(null);

        // Create the Cor, which fails.

        restCorMockMvc.perform(post("/api/cors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cor)))
            .andExpect(status().isBadRequest());

        List<Cor> corList = corRepository.findAll();
        assertThat(corList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCors() throws Exception {
        // Initialize the database
        corRepository.saveAndFlush(cor);

        // Get all the corList
        restCorMockMvc.perform(get("/api/cors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cor.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())));
    }

    @Test
    @Transactional
    public void getCor() throws Exception {
        // Initialize the database
        corRepository.saveAndFlush(cor);

        // Get the cor
        restCorMockMvc.perform(get("/api/cors/{id}", cor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cor.getId().intValue()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCor() throws Exception {
        // Get the cor
        restCorMockMvc.perform(get("/api/cors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCor() throws Exception {
        // Initialize the database
        corRepository.saveAndFlush(cor);
        corSearchRepository.save(cor);
        int databaseSizeBeforeUpdate = corRepository.findAll().size();

        // Update the cor
        Cor updatedCor = corRepository.findOne(cor.getId());
        updatedCor
            .descricao(UPDATED_DESCRICAO);

        restCorMockMvc.perform(put("/api/cors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCor)))
            .andExpect(status().isOk());

        // Validate the Cor in the database
        List<Cor> corList = corRepository.findAll();
        assertThat(corList).hasSize(databaseSizeBeforeUpdate);
        Cor testCor = corList.get(corList.size() - 1);
        assertThat(testCor.getDescricao()).isEqualTo(UPDATED_DESCRICAO);

        // Validate the Cor in Elasticsearch
        Cor corEs = corSearchRepository.findOne(testCor.getId());
        assertThat(corEs).isEqualToComparingFieldByField(testCor);
    }

    @Test
    @Transactional
    public void updateNonExistingCor() throws Exception {
        int databaseSizeBeforeUpdate = corRepository.findAll().size();

        // Create the Cor

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCorMockMvc.perform(put("/api/cors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cor)))
            .andExpect(status().isCreated());

        // Validate the Cor in the database
        List<Cor> corList = corRepository.findAll();
        assertThat(corList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCor() throws Exception {
        // Initialize the database
        corRepository.saveAndFlush(cor);
        corSearchRepository.save(cor);
        int databaseSizeBeforeDelete = corRepository.findAll().size();

        // Get the cor
        restCorMockMvc.perform(delete("/api/cors/{id}", cor.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean corExistsInEs = corSearchRepository.exists(cor.getId());
        assertThat(corExistsInEs).isFalse();

        // Validate the database is empty
        List<Cor> corList = corRepository.findAll();
        assertThat(corList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCor() throws Exception {
        // Initialize the database
        corRepository.saveAndFlush(cor);
        corSearchRepository.save(cor);

        // Search the cor
        restCorMockMvc.perform(get("/api/_search/cors?query=id:" + cor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cor.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cor.class);
        Cor cor1 = new Cor();
        cor1.setId(1L);
        Cor cor2 = new Cor();
        cor2.setId(cor1.getId());
        assertThat(cor1).isEqualTo(cor2);
        cor2.setId(2L);
        assertThat(cor1).isNotEqualTo(cor2);
        cor1.setId(null);
        assertThat(cor1).isNotEqualTo(cor2);
    }
}
