package com.milan.codechangepresentationgenerator.repository;

import com.milan.codechangepresentationgenerator.model.Presentation;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PresentationRepository extends JpaRepository<Presentation, String> {
    List<Presentation> findByUserEmail(String userEmail, Sort sort);
}
