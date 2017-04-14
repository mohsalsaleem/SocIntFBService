package com.socint.fbpage.service;

import com.socint.fbpage.domain.CategoryList;
import com.socint.fbpage.repository.CategoryListRepository;
import com.socint.fbpage.service.dto.CategoryListDTO;
import com.socint.fbpage.service.mapper.CategoryListMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing CategoryList.
 */
@Service
@Transactional
public class CategoryListService {

    private final Logger log = LoggerFactory.getLogger(CategoryListService.class);
    
    private final CategoryListRepository categoryListRepository;

    private final CategoryListMapper categoryListMapper;

    public CategoryListService(CategoryListRepository categoryListRepository, CategoryListMapper categoryListMapper) {
        this.categoryListRepository = categoryListRepository;
        this.categoryListMapper = categoryListMapper;
    }

    /**
     * Save a categoryList.
     *
     * @param categoryListDTO the entity to save
     * @return the persisted entity
     */
    public CategoryListDTO save(CategoryListDTO categoryListDTO) {
        log.debug("Request to save CategoryList : {}", categoryListDTO);
        CategoryList categoryList = categoryListMapper.categoryListDTOToCategoryList(categoryListDTO);
        categoryList = categoryListRepository.save(categoryList);
        CategoryListDTO result = categoryListMapper.categoryListToCategoryListDTO(categoryList);
        return result;
    }

    /**
     *  Get all the categoryLists.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CategoryListDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CategoryLists");
        Page<CategoryList> result = categoryListRepository.findAll(pageable);
        return result.map(categoryList -> categoryListMapper.categoryListToCategoryListDTO(categoryList));
    }

    /**
     *  Get one categoryList by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public CategoryListDTO findOne(Long id) {
        log.debug("Request to get CategoryList : {}", id);
        CategoryList categoryList = categoryListRepository.findOne(id);
        CategoryListDTO categoryListDTO = categoryListMapper.categoryListToCategoryListDTO(categoryList);
        return categoryListDTO;
    }

    /**
     *  Delete the  categoryList by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete CategoryList : {}", id);
        categoryListRepository.delete(id);
    }
}
