package br.com.romaninisistemas.web.rest;

import br.com.romaninisistemas.RomaninisistemasApp;

import br.com.romaninisistemas.domain.VacinacaoAnimal;
import br.com.romaninisistemas.repository.AnimalRepository;
import br.com.romaninisistemas.repository.RemedioRepository;
import br.com.romaninisistemas.repository.VacinacaoAnimalRepository;
import br.com.romaninisistemas.repository.VacinacaoRepository;
import br.com.romaninisistemas.repository.search.VacinacaoAnimalSearchRepository;
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
 * Test class for the VacinacaoAnimalResource REST controller.
 *
 * @see VacinacaoAnimalResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RomaninisistemasApp.class)
public class VacinacaoAnimalResourceIntTest {

    private static final Integer DEFAULT_IDENTIFICACAO = 1;
    private static final Integer UPDATED_IDENTIFICACAO = 2;

    private static final Integer DEFAULT_QUANTIDADE = 1;
    private static final Integer UPDATED_QUANTIDADE = 2;

    @Autowired
    private VacinacaoAnimalRepository vacinacaoAnimalRepository;

    @Autowired
    private VacinacaoRepository vacinacaoRepository;

    @Autowired
    private RemedioRepository remedioRepository;

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private VacinacaoAnimalSearchRepository vacinacaoAnimalSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restVacinacaoAnimalMockMvc;

    private VacinacaoAnimal vacinacaoAnimal;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        VacinacaoAnimalResource vacinacaoAnimalResource = new VacinacaoAnimalResource(vacinacaoAnimalRepository, vacinacaoRepository, remedioRepository, animalRepository, vacinacaoAnimalSearchRepository);
        this.restVacinacaoAnimalMockMvc = MockMvcBuilders.standaloneSetup(vacinacaoAnimalResource)
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
    public static VacinacaoAnimal createEntity(EntityManager em) {
        VacinacaoAnimal vacinacaoAnimal = new VacinacaoAnimal()
            .identificacao(DEFAULT_IDENTIFICACAO)
            .quantidade(DEFAULT_QUANTIDADE);
        return vacinacaoAnimal;
    }

    @Before
    public void initTest() {
        vacinacaoAnimalSearchRepository.deleteAll();
        vacinacaoAnimal = createEntity(em);
    }

    @Test
    @Transactional
    public void createVacinacaoAnimal() throws Exception {
        int databaseSizeBeforeCreate = vacinacaoAnimalRepository.findAll().size();

        // Create the VacinacaoAnimal
        restVacinacaoAnimalMockMvc.perform(post("/api/vacinacao-animals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vacinacaoAnimal)))
            .andExpect(status().isCreated());

        // Validate the VacinacaoAnimal in the database
        List<VacinacaoAnimal> vacinacaoAnimalList = vacinacaoAnimalRepository.findAll();
        assertThat(vacinacaoAnimalList).hasSize(databaseSizeBeforeCreate + 1);
        VacinacaoAnimal testVacinacaoAnimal = vacinacaoAnimalList.get(vacinacaoAnimalList.size() - 1);
        assertThat(testVacinacaoAnimal.getIdentificacao()).isEqualTo(DEFAULT_IDENTIFICACAO);
        assertThat(testVacinacaoAnimal.getQuantidade()).isEqualTo(DEFAULT_QUANTIDADE);

        // Validate the VacinacaoAnimal in Elasticsearch
        VacinacaoAnimal vacinacaoAnimalEs = vacinacaoAnimalSearchRepository.findOne(testVacinacaoAnimal.getId());
        assertThat(vacinacaoAnimalEs).isEqualToComparingFieldByField(testVacinacaoAnimal);
    }

    @Test
    @Transactional
    public void createVacinacaoAnimalWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = vacinacaoAnimalRepository.findAll().size();

        // Create the VacinacaoAnimal with an existing ID
        vacinacaoAnimal.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVacinacaoAnimalMockMvc.perform(post("/api/vacinacao-animals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vacinacaoAnimal)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<VacinacaoAnimal> vacinacaoAnimalList = vacinacaoAnimalRepository.findAll();
        assertThat(vacinacaoAnimalList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkIdentificacaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = vacinacaoAnimalRepository.findAll().size();
        // set the field null
        vacinacaoAnimal.setIdentificacao(null);

        // Create the VacinacaoAnimal, which fails.

        restVacinacaoAnimalMockMvc.perform(post("/api/vacinacao-animals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vacinacaoAnimal)))
            .andExpect(status().isBadRequest());

        List<VacinacaoAnimal> vacinacaoAnimalList = vacinacaoAnimalRepository.findAll();
        assertThat(vacinacaoAnimalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuantidadeIsRequired() throws Exception {
        int databaseSizeBeforeTest = vacinacaoAnimalRepository.findAll().size();
        // set the field null
        vacinacaoAnimal.setQuantidade(null);

        // Create the VacinacaoAnimal, which fails.

        restVacinacaoAnimalMockMvc.perform(post("/api/vacinacao-animals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vacinacaoAnimal)))
            .andExpect(status().isBadRequest());

        List<VacinacaoAnimal> vacinacaoAnimalList = vacinacaoAnimalRepository.findAll();
        assertThat(vacinacaoAnimalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVacinacaoAnimals() throws Exception {
        // Initialize the database
        vacinacaoAnimalRepository.saveAndFlush(vacinacaoAnimal);

        // Get all the vacinacaoAnimalList
        restVacinacaoAnimalMockMvc.perform(get("/api/vacinacao-animals?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vacinacaoAnimal.getId().intValue())))
            .andExpect(jsonPath("$.[*].identificacao").value(hasItem(DEFAULT_IDENTIFICACAO)))
            .andExpect(jsonPath("$.[*].quantidade").value(hasItem(DEFAULT_QUANTIDADE)));
    }

    @Test
    @Transactional
    public void getVacinacaoAnimal() throws Exception {
        // Initialize the database
        vacinacaoAnimalRepository.saveAndFlush(vacinacaoAnimal);

        // Get the vacinacaoAnimal
        restVacinacaoAnimalMockMvc.perform(get("/api/vacinacao-animals/{id}", vacinacaoAnimal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(vacinacaoAnimal.getId().intValue()))
            .andExpect(jsonPath("$.identificacao").value(DEFAULT_IDENTIFICACAO))
            .andExpect(jsonPath("$.quantidade").value(DEFAULT_QUANTIDADE));
    }

    @Test
    @Transactional
    public void getNonExistingVacinacaoAnimal() throws Exception {
        // Get the vacinacaoAnimal
        restVacinacaoAnimalMockMvc.perform(get("/api/vacinacao-animals/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVacinacaoAnimal() throws Exception {
        // Initialize the database
        vacinacaoAnimalRepository.saveAndFlush(vacinacaoAnimal);
        vacinacaoAnimalSearchRepository.save(vacinacaoAnimal);
        int databaseSizeBeforeUpdate = vacinacaoAnimalRepository.findAll().size();

        // Update the vacinacaoAnimal
        VacinacaoAnimal updatedVacinacaoAnimal = vacinacaoAnimalRepository.findOne(vacinacaoAnimal.getId());
        updatedVacinacaoAnimal
            .identificacao(UPDATED_IDENTIFICACAO)
            .quantidade(UPDATED_QUANTIDADE);

        restVacinacaoAnimalMockMvc.perform(put("/api/vacinacao-animals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedVacinacaoAnimal)))
            .andExpect(status().isOk());

        // Validate the VacinacaoAnimal in the database
        List<VacinacaoAnimal> vacinacaoAnimalList = vacinacaoAnimalRepository.findAll();
        assertThat(vacinacaoAnimalList).hasSize(databaseSizeBeforeUpdate);
        VacinacaoAnimal testVacinacaoAnimal = vacinacaoAnimalList.get(vacinacaoAnimalList.size() - 1);
        assertThat(testVacinacaoAnimal.getIdentificacao()).isEqualTo(UPDATED_IDENTIFICACAO);
        assertThat(testVacinacaoAnimal.getQuantidade()).isEqualTo(UPDATED_QUANTIDADE);

        // Validate the VacinacaoAnimal in Elasticsearch
        VacinacaoAnimal vacinacaoAnimalEs = vacinacaoAnimalSearchRepository.findOne(testVacinacaoAnimal.getId());
        assertThat(vacinacaoAnimalEs).isEqualToComparingFieldByField(testVacinacaoAnimal);
    }

    @Test
    @Transactional
    public void updateNonExistingVacinacaoAnimal() throws Exception {
        int databaseSizeBeforeUpdate = vacinacaoAnimalRepository.findAll().size();

        // Create the VacinacaoAnimal

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restVacinacaoAnimalMockMvc.perform(put("/api/vacinacao-animals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vacinacaoAnimal)))
            .andExpect(status().isCreated());

        // Validate the VacinacaoAnimal in the database
        List<VacinacaoAnimal> vacinacaoAnimalList = vacinacaoAnimalRepository.findAll();
        assertThat(vacinacaoAnimalList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteVacinacaoAnimal() throws Exception {
        // Initialize the database
        vacinacaoAnimalRepository.saveAndFlush(vacinacaoAnimal);
        vacinacaoAnimalSearchRepository.save(vacinacaoAnimal);
        int databaseSizeBeforeDelete = vacinacaoAnimalRepository.findAll().size();

        // Get the vacinacaoAnimal
        restVacinacaoAnimalMockMvc.perform(delete("/api/vacinacao-animals/{id}", vacinacaoAnimal.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean vacinacaoAnimalExistsInEs = vacinacaoAnimalSearchRepository.exists(vacinacaoAnimal.getId());
        assertThat(vacinacaoAnimalExistsInEs).isFalse();

        // Validate the database is empty
        List<VacinacaoAnimal> vacinacaoAnimalList = vacinacaoAnimalRepository.findAll();
        assertThat(vacinacaoAnimalList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchVacinacaoAnimal() throws Exception {
        // Initialize the database
        vacinacaoAnimalRepository.saveAndFlush(vacinacaoAnimal);
        vacinacaoAnimalSearchRepository.save(vacinacaoAnimal);

        // Search the vacinacaoAnimal
        restVacinacaoAnimalMockMvc.perform(get("/api/_search/vacinacao-animals?query=id:" + vacinacaoAnimal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vacinacaoAnimal.getId().intValue())))
            .andExpect(jsonPath("$.[*].identificacao").value(hasItem(DEFAULT_IDENTIFICACAO)))
            .andExpect(jsonPath("$.[*].quantidade").value(hasItem(DEFAULT_QUANTIDADE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VacinacaoAnimal.class);
        VacinacaoAnimal vacinacaoAnimal1 = new VacinacaoAnimal();
        vacinacaoAnimal1.setId(1L);
        VacinacaoAnimal vacinacaoAnimal2 = new VacinacaoAnimal();
        vacinacaoAnimal2.setId(vacinacaoAnimal1.getId());
        assertThat(vacinacaoAnimal1).isEqualTo(vacinacaoAnimal2);
        vacinacaoAnimal2.setId(2L);
        assertThat(vacinacaoAnimal1).isNotEqualTo(vacinacaoAnimal2);
        vacinacaoAnimal1.setId(null);
        assertThat(vacinacaoAnimal1).isNotEqualTo(vacinacaoAnimal2);
    }
}
