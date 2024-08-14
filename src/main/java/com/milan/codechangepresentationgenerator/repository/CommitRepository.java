package com.milan.codechangepresentationgenerator.repository;

import com.milan.codechangepresentationgenerator.model.Commit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommitRepository extends JpaRepository<Commit, Long> {
}
