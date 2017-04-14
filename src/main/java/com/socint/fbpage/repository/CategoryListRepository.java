package com.socint.fbpage.repository;

import com.socint.fbpage.domain.CategoryList;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CategoryList entity.
 */
@SuppressWarnings("unused")
public interface CategoryListRepository extends JpaRepository<CategoryList,Long> {

}
