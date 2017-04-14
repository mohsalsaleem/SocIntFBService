package com.socint.fbpage.service;

import com.socint.fbpage.domain.Cover;
import com.socint.fbpage.repository.CoverRepository;
import com.socint.fbpage.service.dto.CoverDTO;
import com.socint.fbpage.service.mapper.CoverMapper;
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
 * Service Implementation for managing Cover.
 */
@Service
@Transactional
public class CoverService {

    private final Logger log = LoggerFactory.getLogger(CoverService.class);
    
    private final CoverRepository coverRepository;

    private final CoverMapper coverMapper;

    public CoverService(CoverRepository coverRepository, CoverMapper coverMapper) {
        this.coverRepository = coverRepository;
        this.coverMapper = coverMapper;
    }

    /**
     * Save a cover.
     *
     * @param coverDTO the entity to save
     * @return the persisted entity
     */
    public CoverDTO save(CoverDTO coverDTO) {
        log.debug("Request to save Cover : {}", coverDTO);
        Cover cover = coverMapper.coverDTOToCover(coverDTO);
        cover = coverRepository.save(cover);
        CoverDTO result = coverMapper.coverToCoverDTO(cover);
        return result;
    }

    /**
     *  Get all the covers.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CoverDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Covers");
        Page<Cover> result = coverRepository.findAll(pageable);
        return result.map(cover -> coverMapper.coverToCoverDTO(cover));
    }

    /**
     *  Get one cover by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public CoverDTO findOne(Long id) {
        log.debug("Request to get Cover : {}", id);
        Cover cover = coverRepository.findOne(id);
        CoverDTO coverDTO = coverMapper.coverToCoverDTO(cover);
        return coverDTO;
    }

    /**
     *  Delete the  cover by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Cover : {}", id);
        coverRepository.delete(id);
    }
}
