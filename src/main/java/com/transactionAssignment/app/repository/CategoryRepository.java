package com.transactionAssignment.app.repository;

import com.transactionAssignment.app.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {
}
