package com.turnover_tax.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.turnover_tax.dto.TurnoverCalculationResult;
import com.turnover_tax.dto.TurnoverCalculationTrigger;
import com.turnover_tax.repository.TurnoverCalculationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class TurnoverTaxCalculationService {

    private final TurnoverCalculationRepository repository;
    private final ObjectMapper mapper = new ObjectMapper();
    
    // Kenya Turnover Tax Rate: 3% (for businesses with annual turnover < KES 50M)
    private static final double TAX_RATE = 0.03;

    public TurnoverTaxCalculationService(TurnoverCalculationRepository repository) {
        this.repository = repository;
    }

    public TurnoverCalculationResult calculateTurnoverTax(TurnoverCalculationTrigger trigger) {
        System.out.println("=".repeat(60));
        System.out.println("💰 STARTING TURNOVER TAX CALCULATION");
        System.out.println("=".repeat(60));
        System.out.println("📋 Extraction ID: " + trigger.getExtractionId());
        System.out.println("👤 User ID: " + trigger.getUserId());
        System.out.println("💵 Gross Turnover: KES " + String.format("%,.2f", trigger.getGrossTurnover()));
        System.out.println("=".repeat(60));

        try {
            // Step 1: Calculate tax
            double grossTurnover = trigger.getGrossTurnover();
            double calculatedTax = grossTurnover * TAX_RATE;

            System.out.println("📊 Tax Calculation:");
            System.out.println("   Invoice Sales: KES " + String.format("%,.2f", trigger.getInvoiceSales()));
            System.out.println("   Cash Sales: KES " + String.format("%,.2f", trigger.getCashSales()));
            System.out.println("   Bank Transfers: KES " + String.format("%,.2f", trigger.getBankTransfers()));
            System.out.println("   ─────────────────────────────");
            System.out.println("   Gross Turnover: KES " + String.format("%,.2f", grossTurnover));
            System.out.println("   Tax Rate: " + (TAX_RATE * 100) + "%");
            System.out.println("   ─────────────────────────────");
            System.out.println("   💰 CALCULATED TAX: KES " + String.format("%,.2f", calculatedTax));

            // Step 2: Create detailed breakdown (JSON)
            Map<String, Object> breakdown = new HashMap<>();
            breakdown.put("extraction_id", trigger.getExtractionId());
            breakdown.put("gross_turnover", grossTurnover);
            breakdown.put("invoice_sales", trigger.getInvoiceSales());
            breakdown.put("cash_sales", trigger.getCashSales());
            breakdown.put("bank_transfers", trigger.getBankTransfers());
            breakdown.put("tax_rate", TAX_RATE);
            breakdown.put("calculated_tax", calculatedTax);
            breakdown.put("calculation_method", "Simple Turnover Tax (3%)");
            breakdown.put("calculation_date", LocalDateTime.now().toString());

            // Step 3: Create result entity
            TurnoverCalculationResult result = new TurnoverCalculationResult();
            result.setExtractionId(trigger.getExtractionId());
            result.setUserId(trigger.getUserId());
            result.setGrossTurnover(grossTurnover);
            result.setTaxRate(TAX_RATE);
            result.setCalculatedTax(calculatedTax);
            result.setStatus("COMPLETED");
            result.setCalculatedAt(LocalDateTime.now());

            try {
                result.setCalculationDetails(mapper.writeValueAsString(breakdown));
            } catch (Exception e) {
                System.err.println("⚠️ Could not serialize calculation details: " + e.getMessage());
                result.setCalculationDetails("{}");
            }

            // Step 4: Save to database
            System.out.println("💾 Saving calculation to database...");
            TurnoverCalculationResult saved = repository.save(result);
            
            System.out.println("✅✅✅ CALCULATION COMPLETED SUCCESSFULLY ✅✅✅");
            System.out.println("💾 Saved with ID: " + saved.getId());
            System.out.println("📊 Summary:");
            System.out.println("   Extraction ID: " + saved.getExtractionId());
            System.out.println("   User ID: " + saved.getUserId());
            System.out.println("   Tax Amount: KES " + String.format("%,.2f", saved.getCalculatedTax()));
            System.out.println("   Status: " + saved.getStatus());
            System.out.println("=".repeat(60));
            
            return saved;

        } catch (Exception e) {
            System.err.println("❌ CALCULATION FAILED: " + e.getMessage());
            e.printStackTrace();
            
            // Save failed calculation
            TurnoverCalculationResult failedResult = new TurnoverCalculationResult();
            failedResult.setExtractionId(trigger.getExtractionId());
            failedResult.setUserId(trigger.getUserId());
            failedResult.setGrossTurnover(trigger.getGrossTurnover());
            failedResult.setStatus("FAILED");
            failedResult.setCalculatedAt(LocalDateTime.now());
            failedResult.setCalculationDetails("{\"error\": \"" + e.getMessage() + "\"}");
            
            return repository.save(failedResult);
        }
    }

    // Additional method to get calculation by extraction ID
    public TurnoverCalculationResult getByExtractionId(Long extractionId) {
        return repository.findByExtractionId(extractionId)
                .orElseThrow(() -> new RuntimeException("Calculation not found for extraction: " + extractionId));
    }

    // Additional method to get all calculations for a user
    public List<TurnoverCalculationResult> getByUserId(String userId) {
        return repository.findByUserId(userId);
    }
}