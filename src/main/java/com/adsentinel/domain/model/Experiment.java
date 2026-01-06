package com.adsentinel.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Experiment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    
    private String variableTested;

    private String results;

    private LocalDateTime startDate;
    
    private LocalDateTime endDate;
}
