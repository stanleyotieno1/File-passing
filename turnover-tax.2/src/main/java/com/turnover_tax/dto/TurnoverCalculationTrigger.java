package com.turnover_tax.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class TurnoverCalculationTrigger implements Serializable {
    private Long extractionId;
    private String userId;
    private String taxType;
    private Double grossTurnover;
    private Double invoiceSales;
    private Double cashSales;
    private Double bankTransfers;
}