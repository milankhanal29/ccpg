package com.milan.codechangepresentationgenerator.repository;

import com.milan.codechangepresentationgenerator.model.Presentation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PresentationRepository extends JpaRepository<Presentation, String> {
}
