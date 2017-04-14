package com.socint.fbpage.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Cover entity.
 */
public class CoverDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 1)
    private String fbId;

    @NotNull
    @Size(min = 1)
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

    public void setFbId(String fbId) {
        this.fbId = fbId;
    }
    public String getSource() {
        return source;
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

        CoverDTO coverDTO = (CoverDTO) o;

        if ( ! Objects.equals(id, coverDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CoverDTO{" +
            "id=" + id +
            ", fbId='" + fbId + "'" +
            ", source='" + source + "'" +
            '}';
    }
}
