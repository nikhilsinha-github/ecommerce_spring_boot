package com.whitelabel.ecommerce.repository;

import com.whitelabel.ecommerce.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Page<Category>> findByTitleContainingIgnoreCase(Pageable pageable, String title);
}
