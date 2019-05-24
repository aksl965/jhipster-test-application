package io.github.jhipster.application.repository;

import io.github.jhipster.application.domain.Claim;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Claim entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClaimRepository extends JpaRepository<Claim, Long> {

}
