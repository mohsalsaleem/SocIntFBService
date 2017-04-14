package com.socint.fbpage.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.socint.fbpage.service.CoverService;
import com.socint.fbpage.web.rest.util.HeaderUtil;
import com.socint.fbpage.web.rest.util.PaginationUtil;
import com.socint.fbpage.service.dto.CoverDTO;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Cover.
 */
@RestController
@RequestMapping("/api")
public class CoverResource {

    private final Logger log = LoggerFactory.getLogger(CoverResource.class);

    private static final String ENTITY_NAME = "cover";
        
    private final CoverService coverService;

    public CoverResource(CoverService coverService) {
        this.coverService = coverService;
    }

    /**
     * POST  /covers : Create a new cover.
     *
     * @param coverDTO the coverDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new coverDTO, or with status 400 (Bad Request) if the cover has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/covers")
    @Timed
    public ResponseEntity<CoverDTO> createCover(@Valid @RequestBody CoverDTO coverDTO) throws URISyntaxException {
        log.debug("REST request to save Cover : {}", coverDTO);
        if (coverDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new cover cannot already have an ID")).body(null);
        }
        CoverDTO result = coverService.save(coverDTO);
        return ResponseEntity.created(new URI("/api/covers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /covers : Updates an existing cover.
     *
     * @param coverDTO the coverDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated coverDTO,
     * or with status 400 (Bad Request) if the coverDTO is not valid,
     * or with status 500 (Internal Server Error) if the coverDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/covers")
    @Timed
    public ResponseEntity<CoverDTO> updateCover(@Valid @RequestBody CoverDTO coverDTO) throws URISyntaxException {
        log.debug("REST request to update Cover : {}", coverDTO);
        if (coverDTO.getId() == null) {
            return createCover(coverDTO);
        }
        CoverDTO result = coverService.save(coverDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, coverDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /covers : get all the covers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of covers in body
     */
    @GetMapping("/covers")
    @Timed
    public ResponseEntity<List<CoverDTO>> getAllCovers(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Covers");
        Page<CoverDTO> page = coverService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/covers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /covers/:id : get the "id" cover.
     *
     * @param id the id of the coverDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the coverDTO, or with status 404 (Not Found)
     */
    @GetMapping("/covers/{id}")
    @Timed
    public ResponseEntity<CoverDTO> getCover(@PathVariable Long id) {
        log.debug("REST request to get Cover : {}", id);
        CoverDTO coverDTO = coverService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(coverDTO));
    }

    /**
     * DELETE  /covers/:id : delete the "id" cover.
     *
     * @param id the id of the coverDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/covers/{id}")
    @Timed
    public ResponseEntity<Void> deleteCover(@PathVariable Long id) {
        log.debug("REST request to delete Cover : {}", id);
        coverService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
