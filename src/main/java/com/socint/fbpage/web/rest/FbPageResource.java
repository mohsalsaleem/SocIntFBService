package com.socint.fbpage.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.socint.fbpage.service.FbPageService;
import com.socint.fbpage.web.rest.util.HeaderUtil;
import com.socint.fbpage.web.rest.util.PaginationUtil;
import com.socint.fbpage.service.dto.FbPageDTO;
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
 * REST controller for managing FbPage.
 */
@RestController
@RequestMapping("/api")
public class FbPageResource {

    private final Logger log = LoggerFactory.getLogger(FbPageResource.class);

    private static final String ENTITY_NAME = "fbPage";
        
    private final FbPageService fbPageService;

    public FbPageResource(FbPageService fbPageService) {
        this.fbPageService = fbPageService;
    }

    /**
     * POST  /fb-pages : Create a new fbPage.
     *
     * @param fbPageDTO the fbPageDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new fbPageDTO, or with status 400 (Bad Request) if the fbPage has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/fb-pages")
    @Timed
    public ResponseEntity<FbPageDTO> createFbPage(@Valid @RequestBody FbPageDTO fbPageDTO) throws URISyntaxException {
        log.debug("REST request to save FbPage : {}", fbPageDTO);
        if (fbPageDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new fbPage cannot already have an ID")).body(null);
        }
        FbPageDTO result = fbPageService.save(fbPageDTO);
        return ResponseEntity.created(new URI("/api/fb-pages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /fb-pages : Updates an existing fbPage.
     *
     * @param fbPageDTO the fbPageDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated fbPageDTO,
     * or with status 400 (Bad Request) if the fbPageDTO is not valid,
     * or with status 500 (Internal Server Error) if the fbPageDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/fb-pages")
    @Timed
    public ResponseEntity<FbPageDTO> updateFbPage(@Valid @RequestBody FbPageDTO fbPageDTO) throws URISyntaxException {
        log.debug("REST request to update FbPage : {}", fbPageDTO);
        if (fbPageDTO.getId() == null) {
            return createFbPage(fbPageDTO);
        }
        FbPageDTO result = fbPageService.save(fbPageDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, fbPageDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /fb-pages : get all the fbPages.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of fbPages in body
     */
    @GetMapping("/fb-pages")
    @Timed
    public ResponseEntity<List<FbPageDTO>> getAllFbPages(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of FbPages");
        Page<FbPageDTO> page = fbPageService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/fb-pages");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /fb-pages/:id : get the "id" fbPage.
     *
     * @param id the id of the fbPageDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the fbPageDTO, or with status 404 (Not Found)
     */
    @GetMapping("/fb-pages/{id}")
    @Timed
    public ResponseEntity<FbPageDTO> getFbPage(@PathVariable Long id) {
        log.debug("REST request to get FbPage : {}", id);
        FbPageDTO fbPageDTO = fbPageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(fbPageDTO));
    }

    /**
     * DELETE  /fb-pages/:id : delete the "id" fbPage.
     *
     * @param id the id of the fbPageDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/fb-pages/{id}")
    @Timed
    public ResponseEntity<Void> deleteFbPage(@PathVariable Long id) {
        log.debug("REST request to delete FbPage : {}", id);
        fbPageService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
