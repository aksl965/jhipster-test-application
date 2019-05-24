package io.github.jhipster.application.web.rest;

import io.github.jhipster.application.domain.Adjudication;
import io.github.jhipster.application.repository.AdjudicationRepository;
import io.github.jhipster.application.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link io.github.jhipster.application.domain.Adjudication}.
 */
@RestController
@RequestMapping("/api")
public class AdjudicationResource {

    private final Logger log = LoggerFactory.getLogger(AdjudicationResource.class);

    private static final String ENTITY_NAME = "adjudication";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AdjudicationRepository adjudicationRepository;

    public AdjudicationResource(AdjudicationRepository adjudicationRepository) {
        this.adjudicationRepository = adjudicationRepository;
    }

    /**
     * {@code POST  /adjudications} : Create a new adjudication.
     *
     * @param adjudication the adjudication to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new adjudication, or with status {@code 400 (Bad Request)} if the adjudication has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/adjudications")
    public ResponseEntity<Adjudication> createAdjudication(@RequestBody Adjudication adjudication) throws URISyntaxException {
        log.debug("REST request to save Adjudication : {}", adjudication);
        if (adjudication.getId() != null) {
            throw new BadRequestAlertException("A new adjudication cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Adjudication result = adjudicationRepository.save(adjudication);
        return ResponseEntity.created(new URI("/api/adjudications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /adjudications} : Updates an existing adjudication.
     *
     * @param adjudication the adjudication to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated adjudication,
     * or with status {@code 400 (Bad Request)} if the adjudication is not valid,
     * or with status {@code 500 (Internal Server Error)} if the adjudication couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/adjudications")
    public ResponseEntity<Adjudication> updateAdjudication(@RequestBody Adjudication adjudication) throws URISyntaxException {
        log.debug("REST request to update Adjudication : {}", adjudication);
        if (adjudication.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Adjudication result = adjudicationRepository.save(adjudication);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, adjudication.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /adjudications} : get all the adjudications.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of adjudications in body.
     */
    @GetMapping("/adjudications")
    public ResponseEntity<List<Adjudication>> getAllAdjudications(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of Adjudications");
        Page<Adjudication> page = adjudicationRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /adjudications/:id} : get the "id" adjudication.
     *
     * @param id the id of the adjudication to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the adjudication, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/adjudications/{id}")
    public ResponseEntity<Adjudication> getAdjudication(@PathVariable Long id) {
        log.debug("REST request to get Adjudication : {}", id);
        Optional<Adjudication> adjudication = adjudicationRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(adjudication);
    }

    /**
     * {@code DELETE  /adjudications/:id} : delete the "id" adjudication.
     *
     * @param id the id of the adjudication to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/adjudications/{id}")
    public ResponseEntity<Void> deleteAdjudication(@PathVariable Long id) {
        log.debug("REST request to delete Adjudication : {}", id);
        adjudicationRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
