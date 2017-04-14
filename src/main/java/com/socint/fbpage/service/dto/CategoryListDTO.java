package com.socint.fbpage.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the CategoryList entity.
 */
public class CategoryListDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 1)
    private String fbId;

    private String category;

    private Long fbPageId;

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
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getFbPageId() {
        return fbPageId;
    }

    public void setFbPageId(Long fbPageId) {
        this.fbPageId = fbPageId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CategoryListDTO categoryListDTO = (CategoryListDTO) o;

        if ( ! Objects.equals(id, categoryListDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CategoryListDTO{" +
            "id=" + id +
            ", fbId='" + fbId + "'" +
            ", category='" + category + "'" +
            '}';
    }
}
