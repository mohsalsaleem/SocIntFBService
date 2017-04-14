package com.socint.fbpage.web.rest;

import com.socint.fbpage.SocIntFacebookServiceApp;

import com.socint.fbpage.domain.CategoryList;
import com.socint.fbpage.repository.CategoryListRepository;
import com.socint.fbpage.service.CategoryListService;
import com.socint.fbpage.service.dto.CategoryListDTO;
import com.socint.fbpage.service.mapper.CategoryListMapper;
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
 * Test class for the CategoryListResource REST controller.
 *
 * @see CategoryListResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SocIntFacebookServiceApp.class)
public class CategoryListResourceIntTest {

    private static final String DEFAULT_FB_ID = "AAAAAAAAAA";
    private static final String UPDATED_FB_ID = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY = "BBBBBBBBBB";

    @Autowired
    private CategoryListRepository categoryListRepository;

    @Autowired
    private CategoryListMapper categoryListMapper;

    @Autowired
    private CategoryListService categoryListService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCategoryListMockMvc;

    private CategoryList categoryList;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CategoryListResource categoryListResource = new CategoryListResource(categoryListService);
        this.restCategoryListMockMvc = MockMvcBuilders.standaloneSetup(categoryListResource)
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
    public static CategoryList createEntity(EntityManager em) {
        CategoryList categoryList = new CategoryList()
            .fbId(DEFAULT_FB_ID)
            .category(DEFAULT_CATEGORY);
        return categoryList;
    }

    @Before
    public void initTest() {
        categoryList = createEntity(em);
    }

    @Test
    @Transactional
    public void createCategoryList() throws Exception {
        int databaseSizeBeforeCreate = categoryListRepository.findAll().size();

        // Create the CategoryList
        CategoryListDTO categoryListDTO = categoryListMapper.categoryListToCategoryListDTO(categoryList);
        restCategoryListMockMvc.perform(post("/api/category-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(categoryListDTO)))
            .andExpect(status().isCreated());

        // Validate the CategoryList in the database
        List<CategoryList> categoryListList = categoryListRepository.findAll();
        assertThat(categoryListList).hasSize(databaseSizeBeforeCreate + 1);
        CategoryList testCategoryList = categoryListList.get(categoryListList.size() - 1);
        assertThat(testCategoryList.getFbId()).isEqualTo(DEFAULT_FB_ID);
        assertThat(testCategoryList.getCategory()).isEqualTo(DEFAULT_CATEGORY);
    }

    @Test
    @Transactional
    public void createCategoryListWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = categoryListRepository.findAll().size();

        // Create the CategoryList with an existing ID
        categoryList.setId(1L);
        CategoryListDTO categoryListDTO = categoryListMapper.categoryListToCategoryListDTO(categoryList);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCategoryListMockMvc.perform(post("/api/category-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(categoryListDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<CategoryList> categoryListList = categoryListRepository.findAll();
        assertThat(categoryListList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkFbIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = categoryListRepository.findAll().size();
        // set the field null
        categoryList.setFbId(null);

        // Create the CategoryList, which fails.
        CategoryListDTO categoryListDTO = categoryListMapper.categoryListToCategoryListDTO(categoryList);

        restCategoryListMockMvc.perform(post("/api/category-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(categoryListDTO)))
            .andExpect(status().isBadRequest());

        List<CategoryList> categoryListList = categoryListRepository.findAll();
        assertThat(categoryListList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCategoryLists() throws Exception {
        // Initialize the database
        categoryListRepository.saveAndFlush(categoryList);

        // Get all the categoryListList
        restCategoryListMockMvc.perform(get("/api/category-lists?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categoryList.getId().intValue())))
            .andExpect(jsonPath("$.[*].fbId").value(hasItem(DEFAULT_FB_ID.toString())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())));
    }

    @Test
    @Transactional
    public void getCategoryList() throws Exception {
        // Initialize the database
        categoryListRepository.saveAndFlush(categoryList);

        // Get the categoryList
        restCategoryListMockMvc.perform(get("/api/category-lists/{id}", categoryList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(categoryList.getId().intValue()))
            .andExpect(jsonPath("$.fbId").value(DEFAULT_FB_ID.toString()))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCategoryList() throws Exception {
        // Get the categoryList
        restCategoryListMockMvc.perform(get("/api/category-lists/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCategoryList() throws Exception {
        // Initialize the database
        categoryListRepository.saveAndFlush(categoryList);
        int databaseSizeBeforeUpdate = categoryListRepository.findAll().size();

        // Update the categoryList
        CategoryList updatedCategoryList = categoryListRepository.findOne(categoryList.getId());
        updatedCategoryList
            .fbId(UPDATED_FB_ID)
            .category(UPDATED_CATEGORY);
        CategoryListDTO categoryListDTO = categoryListMapper.categoryListToCategoryListDTO(updatedCategoryList);

        restCategoryListMockMvc.perform(put("/api/category-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(categoryListDTO)))
            .andExpect(status().isOk());

        // Validate the CategoryList in the database
        List<CategoryList> categoryListList = categoryListRepository.findAll();
        assertThat(categoryListList).hasSize(databaseSizeBeforeUpdate);
        CategoryList testCategoryList = categoryListList.get(categoryListList.size() - 1);
        assertThat(testCategoryList.getFbId()).isEqualTo(UPDATED_FB_ID);
        assertThat(testCategoryList.getCategory()).isEqualTo(UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    public void updateNonExistingCategoryList() throws Exception {
        int databaseSizeBeforeUpdate = categoryListRepository.findAll().size();

        // Create the CategoryList
        CategoryListDTO categoryListDTO = categoryListMapper.categoryListToCategoryListDTO(categoryList);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCategoryListMockMvc.perform(put("/api/category-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(categoryListDTO)))
            .andExpect(status().isCreated());

        // Validate the CategoryList in the database
        List<CategoryList> categoryListList = categoryListRepository.findAll();
        assertThat(categoryListList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCategoryList() throws Exception {
        // Initialize the database
        categoryListRepository.saveAndFlush(categoryList);
        int databaseSizeBeforeDelete = categoryListRepository.findAll().size();

        // Get the categoryList
        restCategoryListMockMvc.perform(delete("/api/category-lists/{id}", categoryList.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CategoryList> categoryListList = categoryListRepository.findAll();
        assertThat(categoryListList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CategoryList.class);
    }
}
