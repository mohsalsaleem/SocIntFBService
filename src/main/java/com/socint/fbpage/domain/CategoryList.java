package com.socint.fbpage.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A CategoryList.
 */
@Entity
@Table(name = "category_list")
public class CategoryList implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1)
    @Column(name = "fb_id", nullable = false)
    private String fbId;

    @Column(name = "category")
    private String category;

    @ManyToOne
    private FbPage fbPage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFbId() {
        return fbId;
    }

    public CategoryList fbId(String fbId) {
        this.fbId = fbId;
        return this;
    }

    public void setFbId(String fbId) {
        this.fbId = fbId;
    }

    public String getCategory() {
        return category;
    }

    public CategoryList category(String category) {
        this.category = category;
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public FbPage getFbPage() {
        return fbPage;
    }

    public CategoryList fbPage(FbPage fbPage) {
        this.fbPage = fbPage;
        return this;
    }

    public void setFbPage(FbPage fbPage) {
        this.fbPage = fbPage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CategoryList categoryList = (CategoryList) o;
        if (categoryList.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, categoryList.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CategoryList{" +
            "id=" + id +
            ", fbId='" + fbId + "'" +
            ", category='" + category + "'" +
            '}';
    }
}
