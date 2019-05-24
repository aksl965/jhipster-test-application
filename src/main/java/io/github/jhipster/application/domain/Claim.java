package io.github.jhipster.application.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A Claim.
 */
@Entity
@Table(name = "claim")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Claim implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "claim_no")
    private Long claimNo;

    @Column(name = "claim_type")
    private String claimType;

    @Column(name = "claim_amount")
    private Long claimAmount;

    @Column(name = "claim_date")
    private Instant claimDate;

    @Lob
    @Column(name = "content")
    private String content;

    @ManyToOne
    @JsonIgnoreProperties("claims")
    private Patient patient;

    @ManyToOne
    @JsonIgnoreProperties("claims")
    private Adjudication adjudication;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClaimNo() {
        return claimNo;
    }

    public Claim claimNo(Long claimNo) {
        this.claimNo = claimNo;
        return this;
    }

    public void setClaimNo(Long claimNo) {
        this.claimNo = claimNo;
    }

    public String getClaimType() {
        return claimType;
    }

    public Claim claimType(String claimType) {
        this.claimType = claimType;
        return this;
    }

    public void setClaimType(String claimType) {
        this.claimType = claimType;
    }

    public Long getClaimAmount() {
        return claimAmount;
    }

    public Claim claimAmount(Long claimAmount) {
        this.claimAmount = claimAmount;
        return this;
    }

    public void setClaimAmount(Long claimAmount) {
        this.claimAmount = claimAmount;
    }

    public Instant getClaimDate() {
        return claimDate;
    }

    public Claim claimDate(Instant claimDate) {
        this.claimDate = claimDate;
        return this;
    }

    public void setClaimDate(Instant claimDate) {
        this.claimDate = claimDate;
    }

    public String getContent() {
        return content;
    }

    public Claim content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Patient getPatient() {
        return patient;
    }

    public Claim patient(Patient patient) {
        this.patient = patient;
        return this;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Adjudication getAdjudication() {
        return adjudication;
    }

    public Claim adjudication(Adjudication adjudication) {
        this.adjudication = adjudication;
        return this;
    }

    public void setAdjudication(Adjudication adjudication) {
        this.adjudication = adjudication;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Claim)) {
            return false;
        }
        return id != null && id.equals(((Claim) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Claim{" +
            "id=" + getId() +
            ", claimNo=" + getClaimNo() +
            ", claimType='" + getClaimType() + "'" +
            ", claimAmount=" + getClaimAmount() +
            ", claimDate='" + getClaimDate() + "'" +
            ", content='" + getContent() + "'" +
            "}";
    }
}
