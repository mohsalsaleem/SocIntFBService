package com.socint.fbpage.repository;

import com.socint.fbpage.domain.Cover;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Cover entity.
 */
@SuppressWarnings("unused")
public interface CoverRepository extends JpaRepository<Cover,Long> {

}
