package io.github.jhipster.application.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Adjudication.
 */
@Entity
@Table(name = "adjudication")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Adjudication implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "claimnumber")
    private Long claimnumber;

    @Column(name = "casenumber")
    private Long casenumber;

    @Column(name = "eob")
    private Long eob;

    @OneToMany(mappedBy = "adjudication")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Claim> claims = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClaimnumber() {
        return claimnumber;
    }

    public Adjudication claimnumber(Long claimnumber) {
        this.claimnumber = claimnumber;
        return this;
    }

    public void setClaimnumber(Long claimnumber) {
        this.claimnumber = claimnumber;
    }

    public Long getCasenumber() {
        return casenumber;
    }

    public Adjudication casenumber(Long casenumber) {
        this.casenumber = casenumber;
        return this;
    }

    public void setCasenumber(Long casenumber) {
        this.casenumber = casenumber;
    }

    public Long getEob() {
        return eob;
    }

    public Adjudication eob(Long eob) {
        this.eob = eob;
        return this;
    }

    public void setEob(Long eob) {
        this.eob = eob;
    }

    public Set<Claim> getClaims() {
        return claims;
    }

    public Adjudication claims(Set<Claim> claims) {
        this.claims = claims;
        return this;
    }

    public Adjudication addClaim(Claim claim) {
        this.claims.add(claim);
        claim.setAdjudication(this);
        return this;
    }

    public Adjudication removeClaim(Claim claim) {
        this.claims.remove(claim);
        claim.setAdjudication(null);
        return this;
    }

    public void setClaims(Set<Claim> claims) {
        this.claims = claims;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Adjudication)) {
            return false;
        }
        return id != null && id.equals(((Adjudication) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Adjudication{" +
            "id=" + getId() +
            ", claimnumber=" + getClaimnumber() +
            ", casenumber=" + getCasenumber() +
            ", eob=" + getEob() +
            "}";
    }
}
