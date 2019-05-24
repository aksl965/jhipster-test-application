package io.github.jhipster.application.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A Enrollment.
 */
@Entity
@Table(name = "enrollment")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Enrollment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "plan_type")
    private String planType;

    @Column(name = "plant_name")
    private String plantName;

    @Column(name = "premium_value")
    private Long premiumValue;

    @Column(name = "from_date")
    private Instant fromDate;

    @Column(name = "to_date")
    private Instant toDate;

    @Lob
    @Column(name = "content")
    private String content;

    @ManyToOne
    @JsonIgnoreProperties("enrollments")
    private Patient patient;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlanType() {
        return planType;
    }

    public Enrollment planType(String planType) {
        this.planType = planType;
        return this;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public String getPlantName() {
        return plantName;
    }

    public Enrollment plantName(String plantName) {
        this.plantName = plantName;
        return this;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public Long getPremiumValue() {
        return premiumValue;
    }

    public Enrollment premiumValue(Long premiumValue) {
        this.premiumValue = premiumValue;
        return this;
    }

    public void setPremiumValue(Long premiumValue) {
        this.premiumValue = premiumValue;
    }

    public Instant getFromDate() {
        return fromDate;
    }

    public Enrollment fromDate(Instant fromDate) {
        this.fromDate = fromDate;
        return this;
    }

    public void setFromDate(Instant fromDate) {
        this.fromDate = fromDate;
    }

    public Instant getToDate() {
        return toDate;
    }

    public Enrollment toDate(Instant toDate) {
        this.toDate = toDate;
        return this;
    }

    public void setToDate(Instant toDate) {
        this.toDate = toDate;
    }

    public String getContent() {
        return content;
    }

    public Enrollment content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Patient getPatient() {
        return patient;
    }

    public Enrollment patient(Patient patient) {
        this.patient = patient;
        return this;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Enrollment)) {
            return false;
        }
        return id != null && id.equals(((Enrollment) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Enrollment{" +
            "id=" + getId() +
            ", planType='" + getPlanType() + "'" +
            ", plantName='" + getPlantName() + "'" +
            ", premiumValue=" + getPremiumValue() +
            ", fromDate='" + getFromDate() + "'" +
            ", toDate='" + getToDate() + "'" +
            ", content='" + getContent() + "'" +
            "}";
    }
}
