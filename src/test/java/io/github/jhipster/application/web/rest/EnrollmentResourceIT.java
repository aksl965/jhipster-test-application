package io.github.jhipster.application.web.rest;

import io.github.jhipster.application.JhipsterTestApplicationApp;
import io.github.jhipster.application.domain.Enrollment;
import io.github.jhipster.application.repository.EnrollmentRepository;
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
 * Integration tests for the {@Link EnrollmentResource} REST controller.
 */
@SpringBootTest(classes = JhipsterTestApplicationApp.class)
public class EnrollmentResourceIT {

    private static final String DEFAULT_PLAN_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_PLAN_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_PLANT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PLANT_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_PREMIUM_VALUE = 1L;
    private static final Long UPDATED_PREMIUM_VALUE = 2L;

    private static final Instant DEFAULT_FROM_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FROM_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_TO_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TO_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    @Autowired
    private EnrollmentRepository enrollmentRepository;

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

    private MockMvc restEnrollmentMockMvc;

    private Enrollment enrollment;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EnrollmentResource enrollmentResource = new EnrollmentResource(enrollmentRepository);
        this.restEnrollmentMockMvc = MockMvcBuilders.standaloneSetup(enrollmentResource)
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
    public static Enrollment createEntity(EntityManager em) {
        Enrollment enrollment = new Enrollment()
            .planType(DEFAULT_PLAN_TYPE)
            .plantName(DEFAULT_PLANT_NAME)
            .premiumValue(DEFAULT_PREMIUM_VALUE)
            .fromDate(DEFAULT_FROM_DATE)
            .toDate(DEFAULT_TO_DATE)
            .content(DEFAULT_CONTENT);
        return enrollment;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Enrollment createUpdatedEntity(EntityManager em) {
        Enrollment enrollment = new Enrollment()
            .planType(UPDATED_PLAN_TYPE)
            .plantName(UPDATED_PLANT_NAME)
            .premiumValue(UPDATED_PREMIUM_VALUE)
            .fromDate(UPDATED_FROM_DATE)
            .toDate(UPDATED_TO_DATE)
            .content(UPDATED_CONTENT);
        return enrollment;
    }

    @BeforeEach
    public void initTest() {
        enrollment = createEntity(em);
    }

    @Test
    @Transactional
    public void createEnrollment() throws Exception {
        int databaseSizeBeforeCreate = enrollmentRepository.findAll().size();

        // Create the Enrollment
        restEnrollmentMockMvc.perform(post("/api/enrollments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(enrollment)))
            .andExpect(status().isCreated());

        // Validate the Enrollment in the database
        List<Enrollment> enrollmentList = enrollmentRepository.findAll();
        assertThat(enrollmentList).hasSize(databaseSizeBeforeCreate + 1);
        Enrollment testEnrollment = enrollmentList.get(enrollmentList.size() - 1);
        assertThat(testEnrollment.getPlanType()).isEqualTo(DEFAULT_PLAN_TYPE);
        assertThat(testEnrollment.getPlantName()).isEqualTo(DEFAULT_PLANT_NAME);
        assertThat(testEnrollment.getPremiumValue()).isEqualTo(DEFAULT_PREMIUM_VALUE);
        assertThat(testEnrollment.getFromDate()).isEqualTo(DEFAULT_FROM_DATE);
        assertThat(testEnrollment.getToDate()).isEqualTo(DEFAULT_TO_DATE);
        assertThat(testEnrollment.getContent()).isEqualTo(DEFAULT_CONTENT);
    }

    @Test
    @Transactional
    public void createEnrollmentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = enrollmentRepository.findAll().size();

        // Create the Enrollment with an existing ID
        enrollment.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEnrollmentMockMvc.perform(post("/api/enrollments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(enrollment)))
            .andExpect(status().isBadRequest());

        // Validate the Enrollment in the database
        List<Enrollment> enrollmentList = enrollmentRepository.findAll();
        assertThat(enrollmentList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllEnrollments() throws Exception {
        // Initialize the database
        enrollmentRepository.saveAndFlush(enrollment);

        // Get all the enrollmentList
        restEnrollmentMockMvc.perform(get("/api/enrollments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(enrollment.getId().intValue())))
            .andExpect(jsonPath("$.[*].planType").value(hasItem(DEFAULT_PLAN_TYPE.toString())))
            .andExpect(jsonPath("$.[*].plantName").value(hasItem(DEFAULT_PLANT_NAME.toString())))
            .andExpect(jsonPath("$.[*].premiumValue").value(hasItem(DEFAULT_PREMIUM_VALUE.intValue())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].toDate").value(hasItem(DEFAULT_TO_DATE.toString())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())));
    }
    
    @Test
    @Transactional
    public void getEnrollment() throws Exception {
        // Initialize the database
        enrollmentRepository.saveAndFlush(enrollment);

        // Get the enrollment
        restEnrollmentMockMvc.perform(get("/api/enrollments/{id}", enrollment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(enrollment.getId().intValue()))
            .andExpect(jsonPath("$.planType").value(DEFAULT_PLAN_TYPE.toString()))
            .andExpect(jsonPath("$.plantName").value(DEFAULT_PLANT_NAME.toString()))
            .andExpect(jsonPath("$.premiumValue").value(DEFAULT_PREMIUM_VALUE.intValue()))
            .andExpect(jsonPath("$.fromDate").value(DEFAULT_FROM_DATE.toString()))
            .andExpect(jsonPath("$.toDate").value(DEFAULT_TO_DATE.toString()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEnrollment() throws Exception {
        // Get the enrollment
        restEnrollmentMockMvc.perform(get("/api/enrollments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEnrollment() throws Exception {
        // Initialize the database
        enrollmentRepository.saveAndFlush(enrollment);

        int databaseSizeBeforeUpdate = enrollmentRepository.findAll().size();

        // Update the enrollment
        Enrollment updatedEnrollment = enrollmentRepository.findById(enrollment.getId()).get();
        // Disconnect from session so that the updates on updatedEnrollment are not directly saved in db
        em.detach(updatedEnrollment);
        updatedEnrollment
            .planType(UPDATED_PLAN_TYPE)
            .plantName(UPDATED_PLANT_NAME)
            .premiumValue(UPDATED_PREMIUM_VALUE)
            .fromDate(UPDATED_FROM_DATE)
            .toDate(UPDATED_TO_DATE)
            .content(UPDATED_CONTENT);

        restEnrollmentMockMvc.perform(put("/api/enrollments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEnrollment)))
            .andExpect(status().isOk());

        // Validate the Enrollment in the database
        List<Enrollment> enrollmentList = enrollmentRepository.findAll();
        assertThat(enrollmentList).hasSize(databaseSizeBeforeUpdate);
        Enrollment testEnrollment = enrollmentList.get(enrollmentList.size() - 1);
        assertThat(testEnrollment.getPlanType()).isEqualTo(UPDATED_PLAN_TYPE);
        assertThat(testEnrollment.getPlantName()).isEqualTo(UPDATED_PLANT_NAME);
        assertThat(testEnrollment.getPremiumValue()).isEqualTo(UPDATED_PREMIUM_VALUE);
        assertThat(testEnrollment.getFromDate()).isEqualTo(UPDATED_FROM_DATE);
        assertThat(testEnrollment.getToDate()).isEqualTo(UPDATED_TO_DATE);
        assertThat(testEnrollment.getContent()).isEqualTo(UPDATED_CONTENT);
    }

    @Test
    @Transactional
    public void updateNonExistingEnrollment() throws Exception {
        int databaseSizeBeforeUpdate = enrollmentRepository.findAll().size();

        // Create the Enrollment

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnrollmentMockMvc.perform(put("/api/enrollments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(enrollment)))
            .andExpect(status().isBadRequest());

        // Validate the Enrollment in the database
        List<Enrollment> enrollmentList = enrollmentRepository.findAll();
        assertThat(enrollmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEnrollment() throws Exception {
        // Initialize the database
        enrollmentRepository.saveAndFlush(enrollment);

        int databaseSizeBeforeDelete = enrollmentRepository.findAll().size();

        // Delete the enrollment
        restEnrollmentMockMvc.perform(delete("/api/enrollments/{id}", enrollment.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<Enrollment> enrollmentList = enrollmentRepository.findAll();
        assertThat(enrollmentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Enrollment.class);
        Enrollment enrollment1 = new Enrollment();
        enrollment1.setId(1L);
        Enrollment enrollment2 = new Enrollment();
        enrollment2.setId(enrollment1.getId());
        assertThat(enrollment1).isEqualTo(enrollment2);
        enrollment2.setId(2L);
        assertThat(enrollment1).isNotEqualTo(enrollment2);
        enrollment1.setId(null);
        assertThat(enrollment1).isNotEqualTo(enrollment2);
    }
}
