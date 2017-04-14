package com.socint.fbpage.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Cover.
 */
@Entity
@Table(name = "cover")
public class Cover implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1)
    @Column(name = "fb_id", nullable = false)
    private String fbId;

    @NotNull
    @Size(min = 1)
    @Column(name = "source", nullable = false)
    private String source;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFbId() {
        return fbId;
    }

    public Cover fbId(String fbId) {
        this.fbId = fbId;
        return this;
    }

    public void setFbId(String fbId) {
        this.fbId = fbId;
    }

    public String getSource() {
        return source;
    }

    public Cover source(String source) {
        this.source = source;
        return this;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Cover cover = (Cover) o;
        if (cover.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, cover.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Cover{" +
            "id=" + id +
            ", fbId='" + fbId + "'" +
            ", source='" + source + "'" +
            '}';
    }
}
