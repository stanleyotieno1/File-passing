package com.turnover_tax.dto;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "turnover_calculation")
public class TurnoverCalculationResult {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long extractionId; // Links back to turnover_extraction table
    private String userId;
    
    private Double grossTurnover;
    private Double taxRate;
    private Double calculatedTax;
    
    private String status; // COMPLETED, FAILED
    
    private LocalDateTime calculatedAt;
    
    @Column(columnDefinition = "TEXT")
    private String calculationDetails; // JSON with breakdown
}