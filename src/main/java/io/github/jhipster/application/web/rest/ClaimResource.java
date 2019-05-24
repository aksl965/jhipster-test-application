package io.github.jhipster.application.web.rest;

import io.github.jhipster.application.domain.Claim;
import io.github.jhipster.application.repository.ClaimRepository;
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
 * REST controller for managing {@link io.github.jhipster.application.domain.Claim}.
 */
@RestController
@RequestMapping("/api")
public class ClaimResource {

    private final Logger log = LoggerFactory.getLogger(ClaimResource.class);

    private static final String ENTITY_NAME = "claim";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClaimRepository claimRepository;

    public ClaimResource(ClaimRepository claimRepository) {
        this.claimRepository = claimRepository;
    }

    /**
     * {@code POST  /claims} : Create a new claim.
     *
     * @param claim the claim to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new claim, or with status {@code 400 (Bad Request)} if the claim has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/claims")
    public ResponseEntity<Claim> createClaim(@RequestBody Claim claim) throws URISyntaxException {
        log.debug("REST request to save Claim : {}", claim);
        if (claim.getId() != null) {
            throw new BadRequestAlertException("A new claim cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Claim result = claimRepository.save(claim);
        return ResponseEntity.created(new URI("/api/claims/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /claims} : Updates an existing claim.
     *
     * @param claim the claim to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated claim,
     * or with status {@code 400 (Bad Request)} if the claim is not valid,
     * or with status {@code 500 (Internal Server Error)} if the claim couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/claims")
    public ResponseEntity<Claim> updateClaim(@RequestBody Claim claim) throws URISyntaxException {
        log.debug("REST request to update Claim : {}", claim);
        if (claim.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Claim result = claimRepository.save(claim);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, claim.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /claims} : get all the claims.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of claims in body.
     */
    @GetMapping("/claims")
    public ResponseEntity<List<Claim>> getAllClaims(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of Claims");
        Page<Claim> page = claimRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /claims/:id} : get the "id" claim.
     *
     * @param id the id of the claim to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the claim, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/claims/{id}")
    public ResponseEntity<Claim> getClaim(@PathVariable Long id) {
        log.debug("REST request to get Claim : {}", id);
        Optional<Claim> claim = claimRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(claim);
    }

    /**
     * {@code DELETE  /claims/:id} : delete the "id" claim.
     *
     * @param id the id of the claim to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/claims/{id}")
    public ResponseEntity<Void> deleteClaim(@PathVariable Long id) {
        log.debug("REST request to delete Claim : {}", id);
        claimRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
