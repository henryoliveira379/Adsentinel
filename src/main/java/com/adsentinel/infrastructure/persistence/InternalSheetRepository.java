package com.adsentinel.infrastructure.persistence;

import com.adsentinel.domain.model.InternalSheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InternalSheetRepository extends JpaRepository<InternalSheet, Long> {
}
