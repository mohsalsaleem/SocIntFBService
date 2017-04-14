package com.socint.fbpage.web.rest;

import com.socint.fbpage.SocIntFacebookServiceApp;

import com.socint.fbpage.domain.FbPage;
import com.socint.fbpage.repository.FbPageRepository;
import com.socint.fbpage.service.FbPageService;
import com.socint.fbpage.service.dto.FbPageDTO;
import com.socint.fbpage.service.mapper.FbPageMapper;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the FbPageResource REST controller.
 *
 * @see FbPageResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SocIntFacebookServiceApp.class)
public class FbPageResourceIntTest {

    private static final String DEFAULT_FB_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FB_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FB_ID = "AAAAAAAAAA";
    private static final String UPDATED_FB_ID = "BBBBBBBBBB";

    private static final String DEFAULT_ACCESS_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_ACCESS_TOKEN = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_PUBLISHED = false;
    private static final Boolean UPDATED_IS_PUBLISHED = true;

    private static final String DEFAULT_LINK = "AAAAAAAAAA";
    private static final String UPDATED_LINK = "BBBBBBBBBB";

    private static final String DEFAULT_USER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_USER_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_UPDATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_AT = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private FbPageRepository fbPageRepository;

    @Autowired
    private FbPageMapper fbPageMapper;

    @Autowired
    private FbPageService fbPageService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFbPageMockMvc;

    private FbPage fbPage;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FbPageResource fbPageResource = new FbPageResource(fbPageService);
        this.restFbPageMockMvc = MockMvcBuilders.standaloneSetup(fbPageResource)
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
    public static FbPage createEntity(EntityManager em) {
        FbPage fbPage = new FbPage()
            .fbName(DEFAULT_FB_NAME)
            .fbID(DEFAULT_FB_ID)
            .accessToken(DEFAULT_ACCESS_TOKEN)
            .category(DEFAULT_CATEGORY)
            .description(DEFAULT_DESCRIPTION)
            .isPublished(DEFAULT_IS_PUBLISHED)
            .link(DEFAULT_LINK)
            .userName(DEFAULT_USER_NAME)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return fbPage;
    }

    @Before
    public void initTest() {
        fbPage = createEntity(em);
    }

    @Test
    @Transactional
    public void createFbPage() throws Exception {
        int databaseSizeBeforeCreate = fbPageRepository.findAll().size();

        // Create the FbPage
        FbPageDTO fbPageDTO = fbPageMapper.fbPageToFbPageDTO(fbPage);
        restFbPageMockMvc.perform(post("/api/fb-pages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fbPageDTO)))
            .andExpect(status().isCreated());

        // Validate the FbPage in the database
        List<FbPage> fbPageList = fbPageRepository.findAll();
        assertThat(fbPageList).hasSize(databaseSizeBeforeCreate + 1);
        FbPage testFbPage = fbPageList.get(fbPageList.size() - 1);
        assertThat(testFbPage.getFbName()).isEqualTo(DEFAULT_FB_NAME);
        assertThat(testFbPage.getFbID()).isEqualTo(DEFAULT_FB_ID);
        assertThat(testFbPage.getAccessToken()).isEqualTo(DEFAULT_ACCESS_TOKEN);
        assertThat(testFbPage.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testFbPage.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testFbPage.isIsPublished()).isEqualTo(DEFAULT_IS_PUBLISHED);
        assertThat(testFbPage.getLink()).isEqualTo(DEFAULT_LINK);
        assertThat(testFbPage.getUserName()).isEqualTo(DEFAULT_USER_NAME);
        assertThat(testFbPage.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testFbPage.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    public void createFbPageWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = fbPageRepository.findAll().size();

        // Create the FbPage with an existing ID
        fbPage.setId(1L);
        FbPageDTO fbPageDTO = fbPageMapper.fbPageToFbPageDTO(fbPage);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFbPageMockMvc.perform(post("/api/fb-pages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fbPageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<FbPage> fbPageList = fbPageRepository.findAll();
        assertThat(fbPageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkFbNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = fbPageRepository.findAll().size();
        // set the field null
        fbPage.setFbName(null);

        // Create the FbPage, which fails.
        FbPageDTO fbPageDTO = fbPageMapper.fbPageToFbPageDTO(fbPage);

        restFbPageMockMvc.perform(post("/api/fb-pages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fbPageDTO)))
            .andExpect(status().isBadRequest());

        List<FbPage> fbPageList = fbPageRepository.findAll();
        assertThat(fbPageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFbIDIsRequired() throws Exception {
        int databaseSizeBeforeTest = fbPageRepository.findAll().size();
        // set the field null
        fbPage.setFbID(null);

        // Create the FbPage, which fails.
        FbPageDTO fbPageDTO = fbPageMapper.fbPageToFbPageDTO(fbPage);

        restFbPageMockMvc.perform(post("/api/fb-pages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fbPageDTO)))
            .andExpect(status().isBadRequest());

        List<FbPage> fbPageList = fbPageRepository.findAll();
        assertThat(fbPageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAccessTokenIsRequired() throws Exception {
        int databaseSizeBeforeTest = fbPageRepository.findAll().size();
        // set the field null
        fbPage.setAccessToken(null);

        // Create the FbPage, which fails.
        FbPageDTO fbPageDTO = fbPageMapper.fbPageToFbPageDTO(fbPage);

        restFbPageMockMvc.perform(post("/api/fb-pages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fbPageDTO)))
            .andExpect(status().isBadRequest());

        List<FbPage> fbPageList = fbPageRepository.findAll();
        assertThat(fbPageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFbPages() throws Exception {
        // Initialize the database
        fbPageRepository.saveAndFlush(fbPage);

        // Get all the fbPageList
        restFbPageMockMvc.perform(get("/api/fb-pages?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fbPage.getId().intValue())))
            .andExpect(jsonPath("$.[*].fbName").value(hasItem(DEFAULT_FB_NAME.toString())))
            .andExpect(jsonPath("$.[*].fbID").value(hasItem(DEFAULT_FB_ID.toString())))
            .andExpect(jsonPath("$.[*].accessToken").value(hasItem(DEFAULT_ACCESS_TOKEN.toString())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].isPublished").value(hasItem(DEFAULT_IS_PUBLISHED.booleanValue())))
            .andExpect(jsonPath("$.[*].link").value(hasItem(DEFAULT_LINK.toString())))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    public void getFbPage() throws Exception {
        // Initialize the database
        fbPageRepository.saveAndFlush(fbPage);

        // Get the fbPage
        restFbPageMockMvc.perform(get("/api/fb-pages/{id}", fbPage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(fbPage.getId().intValue()))
            .andExpect(jsonPath("$.fbName").value(DEFAULT_FB_NAME.toString()))
            .andExpect(jsonPath("$.fbID").value(DEFAULT_FB_ID.toString()))
            .andExpect(jsonPath("$.accessToken").value(DEFAULT_ACCESS_TOKEN.toString()))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.isPublished").value(DEFAULT_IS_PUBLISHED.booleanValue()))
            .andExpect(jsonPath("$.link").value(DEFAULT_LINK.toString()))
            .andExpect(jsonPath("$.userName").value(DEFAULT_USER_NAME.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFbPage() throws Exception {
        // Get the fbPage
        restFbPageMockMvc.perform(get("/api/fb-pages/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFbPage() throws Exception {
        // Initialize the database
        fbPageRepository.saveAndFlush(fbPage);
        int databaseSizeBeforeUpdate = fbPageRepository.findAll().size();

        // Update the fbPage
        FbPage updatedFbPage = fbPageRepository.findOne(fbPage.getId());
        updatedFbPage
            .fbName(UPDATED_FB_NAME)
            .fbID(UPDATED_FB_ID)
            .accessToken(UPDATED_ACCESS_TOKEN)
            .category(UPDATED_CATEGORY)
            .description(UPDATED_DESCRIPTION)
            .isPublished(UPDATED_IS_PUBLISHED)
            .link(UPDATED_LINK)
            .userName(UPDATED_USER_NAME)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        FbPageDTO fbPageDTO = fbPageMapper.fbPageToFbPageDTO(updatedFbPage);

        restFbPageMockMvc.perform(put("/api/fb-pages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fbPageDTO)))
            .andExpect(status().isOk());

        // Validate the FbPage in the database
        List<FbPage> fbPageList = fbPageRepository.findAll();
        assertThat(fbPageList).hasSize(databaseSizeBeforeUpdate);
        FbPage testFbPage = fbPageList.get(fbPageList.size() - 1);
        assertThat(testFbPage.getFbName()).isEqualTo(UPDATED_FB_NAME);
        assertThat(testFbPage.getFbID()).isEqualTo(UPDATED_FB_ID);
        assertThat(testFbPage.getAccessToken()).isEqualTo(UPDATED_ACCESS_TOKEN);
        assertThat(testFbPage.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testFbPage.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFbPage.isIsPublished()).isEqualTo(UPDATED_IS_PUBLISHED);
        assertThat(testFbPage.getLink()).isEqualTo(UPDATED_LINK);
        assertThat(testFbPage.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testFbPage.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testFbPage.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void updateNonExistingFbPage() throws Exception {
        int databaseSizeBeforeUpdate = fbPageRepository.findAll().size();

        // Create the FbPage
        FbPageDTO fbPageDTO = fbPageMapper.fbPageToFbPageDTO(fbPage);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFbPageMockMvc.perform(put("/api/fb-pages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fbPageDTO)))
            .andExpect(status().isCreated());

        // Validate the FbPage in the database
        List<FbPage> fbPageList = fbPageRepository.findAll();
        assertThat(fbPageList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteFbPage() throws Exception {
        // Initialize the database
        fbPageRepository.saveAndFlush(fbPage);
        int databaseSizeBeforeDelete = fbPageRepository.findAll().size();

        // Get the fbPage
        restFbPageMockMvc.perform(delete("/api/fb-pages/{id}", fbPage.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<FbPage> fbPageList = fbPageRepository.findAll();
        assertThat(fbPageList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FbPage.class);
    }
}
