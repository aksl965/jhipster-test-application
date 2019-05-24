package io.github.jhipster.application.web.rest;

import io.github.jhipster.application.JhipsterTestApplicationApp;
import io.github.jhipster.application.domain.Claim;
import io.github.jhipster.application.repository.ClaimRepository;
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
import org.springframework.util.Base64Utils;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static io.github.jhipster.application.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link ClaimResource} REST controller.
 */
@SpringBootTest(classes = JhipsterTestApplicationApp.class)
public class ClaimResourceIT {

    private static final Long DEFAULT_CLAIM_NO = 1L;
    private static final Long UPDATED_CLAIM_NO = 2L;

    private static final String DEFAULT_CLAIM_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_CLAIM_TYPE = "BBBBBBBBBB";

    private static final Long DEFAULT_CLAIM_AMOUNT = 1L;
    private static final Long UPDATED_CLAIM_AMOUNT = 2L;

    private static final Instant DEFAULT_CLAIM_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CLAIM_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    @Autowired
    private ClaimRepository claimRepository;

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

    private MockMvc restClaimMockMvc;

    private Claim claim;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ClaimResource claimResource = new ClaimResource(claimRepository);
        this.restClaimMockMvc = MockMvcBuilders.standaloneSetup(claimResource)
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
    public static Claim createEntity(EntityManager em) {
        Claim claim = new Claim()
            .claimNo(DEFAULT_CLAIM_NO)
            .claimType(DEFAULT_CLAIM_TYPE)
            .claimAmount(DEFAULT_CLAIM_AMOUNT)
            .claimDate(DEFAULT_CLAIM_DATE)
            .content(DEFAULT_CONTENT);
        return claim;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Claim createUpdatedEntity(EntityManager em) {
        Claim claim = new Claim()
            .claimNo(UPDATED_CLAIM_NO)
            .claimType(UPDATED_CLAIM_TYPE)
            .claimAmount(UPDATED_CLAIM_AMOUNT)
            .claimDate(UPDATED_CLAIM_DATE)
            .content(UPDATED_CONTENT);
        return claim;
    }

    @BeforeEach
    public void initTest() {
        claim = createEntity(em);
    }

    @Test
    @Transactional
    public void createClaim() throws Exception {
        int databaseSizeBeforeCreate = claimRepository.findAll().size();

        // Create the Claim
        restClaimMockMvc.perform(post("/api/claims")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(claim)))
            .andExpect(status().isCreated());

        // Validate the Claim in the database
        List<Claim> claimList = claimRepository.findAll();
        assertThat(claimList).hasSize(databaseSizeBeforeCreate + 1);
        Claim testClaim = claimList.get(claimList.size() - 1);
        assertThat(testClaim.getClaimNo()).isEqualTo(DEFAULT_CLAIM_NO);
        assertThat(testClaim.getClaimType()).isEqualTo(DEFAULT_CLAIM_TYPE);
        assertThat(testClaim.getClaimAmount()).isEqualTo(DEFAULT_CLAIM_AMOUNT);
        assertThat(testClaim.getClaimDate()).isEqualTo(DEFAULT_CLAIM_DATE);
        assertThat(testClaim.getContent()).isEqualTo(DEFAULT_CONTENT);
    }

    @Test
    @Transactional
    public void createClaimWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = claimRepository.findAll().size();

        // Create the Claim with an existing ID
        claim.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restClaimMockMvc.perform(post("/api/claims")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(claim)))
            .andExpect(status().isBadRequest());

        // Validate the Claim in the database
        List<Claim> claimList = claimRepository.findAll();
        assertThat(claimList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllClaims() throws Exception {
        // Initialize the database
        claimRepository.saveAndFlush(claim);

        // Get all the claimList
        restClaimMockMvc.perform(get("/api/claims?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(claim.getId().intValue())))
            .andExpect(jsonPath("$.[*].claimNo").value(hasItem(DEFAULT_CLAIM_NO.intValue())))
            .andExpect(jsonPath("$.[*].claimType").value(hasItem(DEFAULT_CLAIM_TYPE.toString())))
            .andExpect(jsonPath("$.[*].claimAmount").value(hasItem(DEFAULT_CLAIM_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].claimDate").value(hasItem(DEFAULT_CLAIM_DATE.toString())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())));
    }
    
    @Test
    @Transactional
    public void getClaim() throws Exception {
        // Initialize the database
        claimRepository.saveAndFlush(claim);

        // Get the claim
        restClaimMockMvc.perform(get("/api/claims/{id}", claim.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(claim.getId().intValue()))
            .andExpect(jsonPath("$.claimNo").value(DEFAULT_CLAIM_NO.intValue()))
            .andExpect(jsonPath("$.claimType").value(DEFAULT_CLAIM_TYPE.toString()))
            .andExpect(jsonPath("$.claimAmount").value(DEFAULT_CLAIM_AMOUNT.intValue()))
            .andExpect(jsonPath("$.claimDate").value(DEFAULT_CLAIM_DATE.toString()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingClaim() throws Exception {
        // Get the claim
        restClaimMockMvc.perform(get("/api/claims/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClaim() throws Exception {
        // Initialize the database
        claimRepository.saveAndFlush(claim);

        int databaseSizeBeforeUpdate = claimRepository.findAll().size();

        // Update the claim
        Claim updatedClaim = claimRepository.findById(claim.getId()).get();
        // Disconnect from session so that the updates on updatedClaim are not directly saved in db
        em.detach(updatedClaim);
        updatedClaim
            .claimNo(UPDATED_CLAIM_NO)
            .claimType(UPDATED_CLAIM_TYPE)
            .claimAmount(UPDATED_CLAIM_AMOUNT)
            .claimDate(UPDATED_CLAIM_DATE)
            .content(UPDATED_CONTENT);

        restClaimMockMvc.perform(put("/api/claims")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedClaim)))
            .andExpect(status().isOk());

        // Validate the Claim in the database
        List<Claim> claimList = claimRepository.findAll();
        assertThat(claimList).hasSize(databaseSizeBeforeUpdate);
        Claim testClaim = claimList.get(claimList.size() - 1);
        assertThat(testClaim.getClaimNo()).isEqualTo(UPDATED_CLAIM_NO);
        assertThat(testClaim.getClaimType()).isEqualTo(UPDATED_CLAIM_TYPE);
        assertThat(testClaim.getClaimAmount()).isEqualTo(UPDATED_CLAIM_AMOUNT);
        assertThat(testClaim.getClaimDate()).isEqualTo(UPDATED_CLAIM_DATE);
        assertThat(testClaim.getContent()).isEqualTo(UPDATED_CONTENT);
    }

    @Test
    @Transactional
    public void updateNonExistingClaim() throws Exception {
        int databaseSizeBeforeUpdate = claimRepository.findAll().size();

        // Create the Claim

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClaimMockMvc.perform(put("/api/claims")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(claim)))
            .andExpect(status().isBadRequest());

        // Validate the Claim in the database
        List<Claim> claimList = claimRepository.findAll();
        assertThat(claimList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteClaim() throws Exception {
        // Initialize the database
        claimRepository.saveAndFlush(claim);

        int databaseSizeBeforeDelete = claimRepository.findAll().size();

        // Delete the claim
        restClaimMockMvc.perform(delete("/api/claims/{id}", claim.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<Claim> claimList = claimRepository.findAll();
        assertThat(claimList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Claim.class);
        Claim claim1 = new Claim();
        claim1.setId(1L);
        Claim claim2 = new Claim();
        claim2.setId(claim1.getId());
        assertThat(claim1).isEqualTo(claim2);
        claim2.setId(2L);
        assertThat(claim1).isNotEqualTo(claim2);
        claim1.setId(null);
        assertThat(claim1).isNotEqualTo(claim2);
    }
}
