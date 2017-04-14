package com.socint.fbpage.web.rest;

import com.socint.fbpage.SocIntFacebookServiceApp;

import com.socint.fbpage.domain.Cover;
import com.socint.fbpage.repository.CoverRepository;
import com.socint.fbpage.service.CoverService;
import com.socint.fbpage.service.dto.CoverDTO;
import com.socint.fbpage.service.mapper.CoverMapper;
import com.socint.fbpage.web.rest.errors.ExceptionTranslator;

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
 * Test class for the CoverResource REST controller.
 *
 * @see CoverResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SocIntFacebookServiceApp.class)
public class CoverResourceIntTest {

    private static final String DEFAULT_FB_ID = "AAAAAAAAAA";
    private static final String UPDATED_FB_ID = "BBBBBBBBBB";

    private static final String DEFAULT_SOURCE = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE = "BBBBBBBBBB";

    @Autowired
    private CoverRepository coverRepository;

    @Autowired
    private CoverMapper coverMapper;

    @Autowired
    private CoverService coverService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCoverMockMvc;

    private Cover cover;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CoverResource coverResource = new CoverResource(coverService);
        this.restCoverMockMvc = MockMvcBuilders.standaloneSetup(coverResource)
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
    public static Cover createEntity(EntityManager em) {
        Cover cover = new Cover()
            .fbId(DEFAULT_FB_ID)
            .source(DEFAULT_SOURCE);
        return cover;
    }

    @Before
    public void initTest() {
        cover = createEntity(em);
    }

    @Test
    @Transactional
    public void createCover() throws Exception {
        int databaseSizeBeforeCreate = coverRepository.findAll().size();

        // Create the Cover
        CoverDTO coverDTO = coverMapper.coverToCoverDTO(cover);
        restCoverMockMvc.perform(post("/api/covers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coverDTO)))
            .andExpect(status().isCreated());

        // Validate the Cover in the database
        List<Cover> coverList = coverRepository.findAll();
        assertThat(coverList).hasSize(databaseSizeBeforeCreate + 1);
        Cover testCover = coverList.get(coverList.size() - 1);
        assertThat(testCover.getFbId()).isEqualTo(DEFAULT_FB_ID);
        assertThat(testCover.getSource()).isEqualTo(DEFAULT_SOURCE);
    }

    @Test
    @Transactional
    public void createCoverWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = coverRepository.findAll().size();

        // Create the Cover with an existing ID
        cover.setId(1L);
        CoverDTO coverDTO = coverMapper.coverToCoverDTO(cover);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCoverMockMvc.perform(post("/api/covers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coverDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Cover> coverList = coverRepository.findAll();
        assertThat(coverList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkFbIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = coverRepository.findAll().size();
        // set the field null
        cover.setFbId(null);

        // Create the Cover, which fails.
        CoverDTO coverDTO = coverMapper.coverToCoverDTO(cover);

        restCoverMockMvc.perform(post("/api/covers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coverDTO)))
            .andExpect(status().isBadRequest());

        List<Cover> coverList = coverRepository.findAll();
        assertThat(coverList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSourceIsRequired() throws Exception {
        int databaseSizeBeforeTest = coverRepository.findAll().size();
        // set the field null
        cover.setSource(null);

        // Create the Cover, which fails.
        CoverDTO coverDTO = coverMapper.coverToCoverDTO(cover);

        restCoverMockMvc.perform(post("/api/covers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coverDTO)))
            .andExpect(status().isBadRequest());

        List<Cover> coverList = coverRepository.findAll();
        assertThat(coverList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCovers() throws Exception {
        // Initialize the database
        coverRepository.saveAndFlush(cover);

        // Get all the coverList
        restCoverMockMvc.perform(get("/api/covers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cover.getId().intValue())))
            .andExpect(jsonPath("$.[*].fbId").value(hasItem(DEFAULT_FB_ID.toString())))
            .andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE.toString())));
    }

    @Test
    @Transactional
    public void getCover() throws Exception {
        // Initialize the database
        coverRepository.saveAndFlush(cover);

        // Get the cover
        restCoverMockMvc.perform(get("/api/covers/{id}", cover.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cover.getId().intValue()))
            .andExpect(jsonPath("$.fbId").value(DEFAULT_FB_ID.toString()))
            .andExpect(jsonPath("$.source").value(DEFAULT_SOURCE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCover() throws Exception {
        // Get the cover
        restCoverMockMvc.perform(get("/api/covers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCover() throws Exception {
        // Initialize the database
        coverRepository.saveAndFlush(cover);
        int databaseSizeBeforeUpdate = coverRepository.findAll().size();

        // Update the cover
        Cover updatedCover = coverRepository.findOne(cover.getId());
        updatedCover
            .fbId(UPDATED_FB_ID)
            .source(UPDATED_SOURCE);
        CoverDTO coverDTO = coverMapper.coverToCoverDTO(updatedCover);

        restCoverMockMvc.perform(put("/api/covers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coverDTO)))
            .andExpect(status().isOk());

        // Validate the Cover in the database
        List<Cover> coverList = coverRepository.findAll();
        assertThat(coverList).hasSize(databaseSizeBeforeUpdate);
        Cover testCover = coverList.get(coverList.size() - 1);
        assertThat(testCover.getFbId()).isEqualTo(UPDATED_FB_ID);
        assertThat(testCover.getSource()).isEqualTo(UPDATED_SOURCE);
    }

    @Test
    @Transactional
    public void updateNonExistingCover() throws Exception {
        int databaseSizeBeforeUpdate = coverRepository.findAll().size();

        // Create the Cover
        CoverDTO coverDTO = coverMapper.coverToCoverDTO(cover);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCoverMockMvc.perform(put("/api/covers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coverDTO)))
            .andExpect(status().isCreated());

        // Validate the Cover in the database
        List<Cover> coverList = coverRepository.findAll();
        assertThat(coverList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCover() throws Exception {
        // Initialize the database
        coverRepository.saveAndFlush(cover);
        int databaseSizeBeforeDelete = coverRepository.findAll().size();

        // Get the cover
        restCoverMockMvc.perform(delete("/api/covers/{id}", cover.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Cover> coverList = coverRepository.findAll();
        assertThat(coverList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cover.class);
    }
}
