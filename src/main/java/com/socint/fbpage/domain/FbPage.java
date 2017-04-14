package com.socint.fbpage.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A FbPage.
 */
@Entity
@Table(name = "fb_page")
public class FbPage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1)
    @Column(name = "fb_name", nullable = false)
    private String fbName;

    @NotNull
    @Size(min = 1)
    @Column(name = "fb_id", nullable = false)
    private String fbID;

    @NotNull
    @Size(min = 1)
    @Column(name = "access_token", nullable = false)
    private String accessToken;

    @Column(name = "category")
    private String category;

    @Column(name = "description")
    private String description;

    @Column(name = "is_published")
    private Boolean isPublished;

    @Column(name = "jhi_link")
    private String link;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @OneToOne
    @JoinColumn(unique = true)
    private Cover cover;

    @OneToMany(mappedBy = "fbPage")
    @JsonIgnore
    private Set<CategoryList> categoryLists = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFbName() {
        return fbName;
    }

    public FbPage fbName(String fbName) {
        this.fbName = fbName;
        return this;
    }

    public void setFbName(String fbName) {
        this.fbName = fbName;
    }

    public String getFbID() {
        return fbID;
    }

    public FbPage fbID(String fbID) {
        this.fbID = fbID;
        return this;
    }

    public void setFbID(String fbID) {
        this.fbID = fbID;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public FbPage accessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getCategory() {
        return category;
    }

    public FbPage category(String category) {
        this.category = category;
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public FbPage description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isIsPublished() {
        return isPublished;
    }

    public FbPage isPublished(Boolean isPublished) {
        this.isPublished = isPublished;
        return this;
    }

    public void setIsPublished(Boolean isPublished) {
        this.isPublished = isPublished;
    }

    public String getLink() {
        return link;
    }

    public FbPage link(String link) {
        this.link = link;
        return this;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getUserName() {
        return userName;
    }

    public FbPage userName(String userName) {
        this.userName = userName;
        return this;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public FbPage createdAt(LocalDate createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public FbPage updatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Cover getCover() {
        return cover;
    }

    public FbPage cover(Cover cover) {
        this.cover = cover;
        return this;
    }

    public void setCover(Cover cover) {
        this.cover = cover;
    }

    public Set<CategoryList> getCategoryLists() {
        return categoryLists;
    }

    public FbPage categoryLists(Set<CategoryList> categoryLists) {
        this.categoryLists = categoryLists;
        return this;
    }

    public FbPage addCategoryList(CategoryList categoryList) {
        this.categoryLists.add(categoryList);
        categoryList.setFbPage(this);
        return this;
    }

    public FbPage removeCategoryList(CategoryList categoryList) {
        this.categoryLists.remove(categoryList);
        categoryList.setFbPage(null);
        return this;
    }

    public void setCategoryLists(Set<CategoryList> categoryLists) {
        this.categoryLists = categoryLists;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FbPage fbPage = (FbPage) o;
        if (fbPage.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, fbPage.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "FbPage{" +
            "id=" + id +
            ", fbName='" + fbName + "'" +
            ", fbID='" + fbID + "'" +
            ", accessToken='" + accessToken + "'" +
            ", category='" + category + "'" +
            ", description='" + description + "'" +
            ", isPublished='" + isPublished + "'" +
            ", link='" + link + "'" +
            ", userName='" + userName + "'" +
            ", createdAt='" + createdAt + "'" +
            ", updatedAt='" + updatedAt + "'" +
            '}';
    }
}
