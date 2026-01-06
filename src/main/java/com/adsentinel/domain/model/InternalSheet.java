package com.adsentinel.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class InternalSheet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Lob
    private String contentJson; // Storing table data as JSON for simplicity

    private LocalDateTime lastModified;
    
    private String lastModifiedBy;
}
