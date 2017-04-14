package com.socint.fbpage.repository;

import com.socint.fbpage.domain.FbPage;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the FbPage entity.
 */
@SuppressWarnings("unused")
public interface FbPageRepository extends JpaRepository<FbPage,Long> {

}
