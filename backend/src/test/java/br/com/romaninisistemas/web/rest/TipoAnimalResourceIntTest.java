package br.com.romaninisistemas.web.rest;

import br.com.romaninisistemas.RomaninisistemasApp;

import br.com.romaninisistemas.domain.TipoAnimal;
import br.com.romaninisistemas.repository.TipoAnimalRepository;
import br.com.romaninisistemas.repository.search.TipoAnimalSearchRepository;
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
 * Test class for the TipoAnimalResource REST controller.
 *
 * @see TipoAnimalResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RomaninisistemasApp.class)
public class TipoAnimalResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    @Autowired
    private TipoAnimalRepository tipoAnimalRepository;

    @Autowired
    private TipoAnimalSearchRepository tipoAnimalSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTipoAnimalMockMvc;

    private TipoAnimal tipoAnimal;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TipoAnimalResource tipoAnimalResource = new TipoAnimalResource(tipoAnimalRepository, tipoAnimalSearchRepository);
        this.restTipoAnimalMockMvc = MockMvcBuilders.standaloneSetup(tipoAnimalResource)
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
    public static TipoAnimal createEntity(EntityManager em) {
        TipoAnimal tipoAnimal = new TipoAnimal()
            .nome(DEFAULT_NOME);
        return tipoAnimal;
    }

    @Before
    public void initTest() {
        tipoAnimalSearchRepository.deleteAll();
        tipoAnimal = createEntity(em);
    }

    @Test
    @Transactional
    public void createTipoAnimal() throws Exception {
        int databaseSizeBeforeCreate = tipoAnimalRepository.findAll().size();

        // Create the TipoAnimal
        restTipoAnimalMockMvc.perform(post("/api/tipo-animals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoAnimal)))
            .andExpect(status().isCreated());

        // Validate the TipoAnimal in the database
        List<TipoAnimal> tipoAnimalList = tipoAnimalRepository.findAll();
        assertThat(tipoAnimalList).hasSize(databaseSizeBeforeCreate + 1);
        TipoAnimal testTipoAnimal = tipoAnimalList.get(tipoAnimalList.size() - 1);
        assertThat(testTipoAnimal.getNome()).isEqualTo(DEFAULT_NOME);

        // Validate the TipoAnimal in Elasticsearch
        TipoAnimal tipoAnimalEs = tipoAnimalSearchRepository.findOne(testTipoAnimal.getId());
        assertThat(tipoAnimalEs).isEqualToComparingFieldByField(testTipoAnimal);
    }

    @Test
    @Transactional
    public void createTipoAnimalWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tipoAnimalRepository.findAll().size();

        // Create the TipoAnimal with an existing ID
        tipoAnimal.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipoAnimalMockMvc.perform(post("/api/tipo-animals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoAnimal)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<TipoAnimal> tipoAnimalList = tipoAnimalRepository.findAll();
        assertThat(tipoAnimalList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = tipoAnimalRepository.findAll().size();
        // set the field null
        tipoAnimal.setNome(null);

        // Create the TipoAnimal, which fails.

        restTipoAnimalMockMvc.perform(post("/api/tipo-animals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoAnimal)))
            .andExpect(status().isBadRequest());

        List<TipoAnimal> tipoAnimalList = tipoAnimalRepository.findAll();
        assertThat(tipoAnimalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTipoAnimals() throws Exception {
        // Initialize the database
        tipoAnimalRepository.saveAndFlush(tipoAnimal);

        // Get all the tipoAnimalList
        restTipoAnimalMockMvc.perform(get("/api/tipo-animals?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoAnimal.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())));
    }

    @Test
    @Transactional
    public void getTipoAnimal() throws Exception {
        // Initialize the database
        tipoAnimalRepository.saveAndFlush(tipoAnimal);

        // Get the tipoAnimal
        restTipoAnimalMockMvc.perform(get("/api/tipo-animals/{id}", tipoAnimal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tipoAnimal.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTipoAnimal() throws Exception {
        // Get the tipoAnimal
        restTipoAnimalMockMvc.perform(get("/api/tipo-animals/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTipoAnimal() throws Exception {
        // Initialize the database
        tipoAnimalRepository.saveAndFlush(tipoAnimal);
        tipoAnimalSearchRepository.save(tipoAnimal);
        int databaseSizeBeforeUpdate = tipoAnimalRepository.findAll().size();

        // Update the tipoAnimal
        TipoAnimal updatedTipoAnimal = tipoAnimalRepository.findOne(tipoAnimal.getId());
        updatedTipoAnimal
            .nome(UPDATED_NOME);

        restTipoAnimalMockMvc.perform(put("/api/tipo-animals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTipoAnimal)))
            .andExpect(status().isOk());

        // Validate the TipoAnimal in the database
        List<TipoAnimal> tipoAnimalList = tipoAnimalRepository.findAll();
        assertThat(tipoAnimalList).hasSize(databaseSizeBeforeUpdate);
        TipoAnimal testTipoAnimal = tipoAnimalList.get(tipoAnimalList.size() - 1);
        assertThat(testTipoAnimal.getNome()).isEqualTo(UPDATED_NOME);

        // Validate the TipoAnimal in Elasticsearch
        TipoAnimal tipoAnimalEs = tipoAnimalSearchRepository.findOne(testTipoAnimal.getId());
        assertThat(tipoAnimalEs).isEqualToComparingFieldByField(testTipoAnimal);
    }

    @Test
    @Transactional
    public void updateNonExistingTipoAnimal() throws Exception {
        int databaseSizeBeforeUpdate = tipoAnimalRepository.findAll().size();

        // Create the TipoAnimal

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTipoAnimalMockMvc.perform(put("/api/tipo-animals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tipoAnimal)))
            .andExpect(status().isCreated());

        // Validate the TipoAnimal in the database
        List<TipoAnimal> tipoAnimalList = tipoAnimalRepository.findAll();
        assertThat(tipoAnimalList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTipoAnimal() throws Exception {
        // Initialize the database
        tipoAnimalRepository.saveAndFlush(tipoAnimal);
        tipoAnimalSearchRepository.save(tipoAnimal);
        int databaseSizeBeforeDelete = tipoAnimalRepository.findAll().size();

        // Get the tipoAnimal
        restTipoAnimalMockMvc.perform(delete("/api/tipo-animals/{id}", tipoAnimal.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean tipoAnimalExistsInEs = tipoAnimalSearchRepository.exists(tipoAnimal.getId());
        assertThat(tipoAnimalExistsInEs).isFalse();

        // Validate the database is empty
        List<TipoAnimal> tipoAnimalList = tipoAnimalRepository.findAll();
        assertThat(tipoAnimalList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTipoAnimal() throws Exception {
        // Initialize the database
        tipoAnimalRepository.saveAndFlush(tipoAnimal);
        tipoAnimalSearchRepository.save(tipoAnimal);

        // Search the tipoAnimal
        restTipoAnimalMockMvc.perform(get("/api/_search/tipo-animals?query=id:" + tipoAnimal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoAnimal.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TipoAnimal.class);
        TipoAnimal tipoAnimal1 = new TipoAnimal();
        tipoAnimal1.setId(1L);
        TipoAnimal tipoAnimal2 = new TipoAnimal();
        tipoAnimal2.setId(tipoAnimal1.getId());
        assertThat(tipoAnimal1).isEqualTo(tipoAnimal2);
        tipoAnimal2.setId(2L);
        assertThat(tipoAnimal1).isNotEqualTo(tipoAnimal2);
        tipoAnimal1.setId(null);
        assertThat(tipoAnimal1).isNotEqualTo(tipoAnimal2);
    }
}
