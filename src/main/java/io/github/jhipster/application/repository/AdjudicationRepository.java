package io.github.jhipster.application.repository;

import io.github.jhipster.application.domain.Adjudication;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Adjudication entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AdjudicationRepository extends JpaRepository<Adjudication, Long> {

}
