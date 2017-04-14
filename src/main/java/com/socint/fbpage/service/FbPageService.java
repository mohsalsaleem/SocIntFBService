package com.socint.fbpage.service;

import com.socint.fbpage.domain.FbPage;
import com.socint.fbpage.repository.FbPageRepository;
import com.socint.fbpage.service.dto.FbPageDTO;
import com.socint.fbpage.service.mapper.FbPageMapper;
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
 * Service Implementation for managing FbPage.
 */
@Service
@Transactional
public class FbPageService {

    private final Logger log = LoggerFactory.getLogger(FbPageService.class);
    
    private final FbPageRepository fbPageRepository;

    private final FbPageMapper fbPageMapper;

    public FbPageService(FbPageRepository fbPageRepository, FbPageMapper fbPageMapper) {
        this.fbPageRepository = fbPageRepository;
        this.fbPageMapper = fbPageMapper;
    }

    /**
     * Save a fbPage.
     *
     * @param fbPageDTO the entity to save
     * @return the persisted entity
     */
    public FbPageDTO save(FbPageDTO fbPageDTO) {
        log.debug("Request to save FbPage : {}", fbPageDTO);
        FbPage fbPage = fbPageMapper.fbPageDTOToFbPage(fbPageDTO);
        fbPage = fbPageRepository.save(fbPage);
        FbPageDTO result = fbPageMapper.fbPageToFbPageDTO(fbPage);
        return result;
    }

    /**
     *  Get all the fbPages.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<FbPageDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FbPages");
        Page<FbPage> result = fbPageRepository.findAll(pageable);
        return result.map(fbPage -> fbPageMapper.fbPageToFbPageDTO(fbPage));
    }

    /**
     *  Get one fbPage by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public FbPageDTO findOne(Long id) {
        log.debug("Request to get FbPage : {}", id);
        FbPage fbPage = fbPageRepository.findOne(id);
        FbPageDTO fbPageDTO = fbPageMapper.fbPageToFbPageDTO(fbPage);
        return fbPageDTO;
    }

    /**
     *  Delete the  fbPage by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete FbPage : {}", id);
        fbPageRepository.delete(id);
    }
}
