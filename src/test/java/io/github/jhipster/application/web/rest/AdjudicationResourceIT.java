package io.github.jhipster.application.web.rest;

import io.github.jhipster.application.JhipsterTestApplicationApp;
import io.github.jhipster.application.domain.Adjudication;
import io.github.jhipster.application.repository.AdjudicationRepository;
import io.github.jhipster.application.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static io.github.jhipster.application.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link AdjudicationResource} REST controller.
 */
@SpringBootTest(classes = JhipsterTestApplicationApp.class)
public class AdjudicationResourceIT {

    private static final Long DEFAULT_CLAIMNUMBER = 1L;
    private static final Long UPDATED_CLAIMNUMBER = 2L;

    private static final Long DEFAULT_CASENUMBER = 1L;
    private static final Long UPDATED_CASENUMBER = 2L;

    private static final Long DEFAULT_EOB = 1L;
    private static final Long UPDATED_EOB = 2L;

    @Autowired
    private AdjudicationRepository adjudicationRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restAdjudicationMockMvc;

    private Adjudication adjudication;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AdjudicationResource adjudicationResource = new AdjudicationResource(adjudicationRepository);
        this.restAdjudicationMockMvc = MockMvcBuilders.standaloneSetup(adjudicationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Adjudication createEntity(EntityManager em) {
        Adjudication adjudication = new Adjudication()
            .claimnumber(DEFAULT_CLAIMNUMBER)
            .casenumber(DEFAULT_CASENUMBER)
            .eob(DEFAULT_EOB);
        return adjudication;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Adjudication createUpdatedEntity(EntityManager em) {
        Adjudication adjudication = new Adjudication()
            .claimnumber(UPDATED_CLAIMNUMBER)
            .casenumber(UPDATED_CASENUMBER)
            .eob(UPDATED_EOB);
        return adjudication;
    }

    @BeforeEach
    public void initTest() {
        adjudication = createEntity(em);
    }

    @Test
    @Transactional
    public void createAdjudication() throws Exception {
        int databaseSizeBeforeCreate = adjudicationRepository.findAll().size();

        // Create the Adjudication
        restAdjudicationMockMvc.perform(post("/api/adjudications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(adjudication)))
            .andExpect(status().isCreated());

        // Validate the Adjudication in the database
        List<Adjudication> adjudicationList = adjudicationRepository.findAll();
        assertThat(adjudicationList).hasSize(databaseSizeBeforeCreate + 1);
        Adjudication testAdjudication = adjudicationList.get(adjudicationList.size() - 1);
        assertThat(testAdjudication.getClaimnumber()).isEqualTo(DEFAULT_CLAIMNUMBER);
        assertThat(testAdjudication.getCasenumber()).isEqualTo(DEFAULT_CASENUMBER);
        assertThat(testAdjudication.getEob()).isEqualTo(DEFAULT_EOB);
    }

    @Test
    @Transactional
    public void createAdjudicationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = adjudicationRepository.findAll().size();

        // Create the Adjudication with an existing ID
        adjudication.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdjudicationMockMvc.perform(post("/api/adjudications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(adjudication)))
            .andExpect(status().isBadRequest());

        // Validate the Adjudication in the database
        List<Adjudication> adjudicationList = adjudicationRepository.findAll();
        assertThat(adjudicationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllAdjudications() throws Exception {
        // Initialize the database
        adjudicationRepository.saveAndFlush(adjudication);

        // Get all the adjudicationList
        restAdjudicationMockMvc.perform(get("/api/adjudications?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(adjudication.getId().intValue())))
            .andExpect(jsonPath("$.[*].claimnumber").value(hasItem(DEFAULT_CLAIMNUMBER.intValue())))
            .andExpect(jsonPath("$.[*].casenumber").value(hasItem(DEFAULT_CASENUMBER.intValue())))
            .andExpect(jsonPath("$.[*].eob").value(hasItem(DEFAULT_EOB.intValue())));
    }
    
    @Test
    @Transactional
    public void getAdjudication() throws Exception {
        // Initialize the database
        adjudicationRepository.saveAndFlush(adjudication);

        // Get the adjudication
        restAdjudicationMockMvc.perform(get("/api/adjudications/{id}", adjudication.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(adjudication.getId().intValue()))
            .andExpect(jsonPath("$.claimnumber").value(DEFAULT_CLAIMNUMBER.intValue()))
            .andExpect(jsonPath("$.casenumber").value(DEFAULT_CASENUMBER.intValue()))
            .andExpect(jsonPath("$.eob").value(DEFAULT_EOB.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingAdjudication() throws Exception {
        // Get the adjudication
        restAdjudicationMockMvc.perform(get("/api/adjudications/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAdjudication() throws Exception {
        // Initialize the database
        adjudicationRepository.saveAndFlush(adjudication);

        int databaseSizeBeforeUpdate = adjudicationRepository.findAll().size();

        // Update the adjudication
        Adjudication updatedAdjudication = adjudicationRepository.findById(adjudication.getId()).get();
        // Disconnect from session so that the updates on updatedAdjudication are not directly saved in db
        em.detach(updatedAdjudication);
        updatedAdjudication
            .claimnumber(UPDATED_CLAIMNUMBER)
            .casenumber(UPDATED_CASENUMBER)
            .eob(UPDATED_EOB);

        restAdjudicationMockMvc.perform(put("/api/adjudications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAdjudication)))
            .andExpect(status().isOk());

        // Validate the Adjudication in the database
        List<Adjudication> adjudicationList = adjudicationRepository.findAll();
        assertThat(adjudicationList).hasSize(databaseSizeBeforeUpdate);
        Adjudication testAdjudication = adjudicationList.get(adjudicationList.size() - 1);
        assertThat(testAdjudication.getClaimnumber()).isEqualTo(UPDATED_CLAIMNUMBER);
        assertThat(testAdjudication.getCasenumber()).isEqualTo(UPDATED_CASENUMBER);
        assertThat(testAdjudication.getEob()).isEqualTo(UPDATED_EOB);
    }

    @Test
    @Transactional
    public void updateNonExistingAdjudication() throws Exception {
        int databaseSizeBeforeUpdate = adjudicationRepository.findAll().size();

        // Create the Adjudication

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdjudicationMockMvc.perform(put("/api/adjudications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(adjudication)))
            .andExpect(status().isBadRequest());

        // Validate the Adjudication in the database
        List<Adjudication> adjudicationList = adjudicationRepository.findAll();
        assertThat(adjudicationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAdjudication() throws Exception {
        // Initialize the database
        adjudicationRepository.saveAndFlush(adjudication);

        int databaseSizeBeforeDelete = adjudicationRepository.findAll().size();

        // Delete the adjudication
        restAdjudicationMockMvc.perform(delete("/api/adjudications/{id}", adjudication.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<Adjudication> adjudicationList = adjudicationRepository.findAll();
        assertThat(adjudicationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Adjudication.class);
        Adjudication adjudication1 = new Adjudication();
        adjudication1.setId(1L);
        Adjudication adjudication2 = new Adjudication();
        adjudication2.setId(adjudication1.getId());
        assertThat(adjudication1).isEqualTo(adjudication2);
        adjudication2.setId(2L);
        assertThat(adjudication1).isNotEqualTo(adjudication2);
        adjudication1.setId(null);
        assertThat(adjudication1).isNotEqualTo(adjudication2);
    }
}
