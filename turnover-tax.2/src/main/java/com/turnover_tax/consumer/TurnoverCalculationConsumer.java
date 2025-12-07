package main.java.com.turnover_tax.consumer;

import com.turnover_tax.config.RabbitMQConfig;
import com.turnover_tax.dto.TurnoverCalculationTrigger;
import com.turnover_tax.service.TurnoverTaxCalculationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class TurnoverCalculationConsumer {

    private final TurnoverTaxCalculationService calculationService;

    public TurnoverCalculationConsumer(TurnoverTaxCalculationService calculationService) {
        this.calculationService = calculationService;
    }

    /**
     * CRITICAL: This method listens to the RabbitMQ queue
     * Flow: Doc Wrapper confirms extraction → Publishes event → This consumes → Calculates tax
     */
    @RabbitListener(queues = RabbitMQConfig.TURNOVER_CALCULATION_QUEUE)
    public void processCalculationRequest(TurnoverCalculationTrigger trigger) {
        System.out.println("\n\n");
        System.out.println("╔" + "═".repeat(58) + "╗");
        System.out.println("║" + " ".repeat(10) + "📊 CALCULATION REQUEST RECEIVED" + " ".repeat(16) + "║");
        System.out.println("╚" + "═".repeat(58) + "╝");
        System.out.println("📥 Message from RabbitMQ:");
        System.out.println("   Queue: " + RabbitMQConfig.TURNOVER_CALCULATION_QUEUE);
        System.out.println("   Extraction ID: " + trigger.getExtractionId());
        System.out.println("   User ID: " + trigger.getUserId());
        System.out.println("   Gross Turnover: " + trigger.getGrossTurnover());
        System.out.println("");
        
        try {
            // Process the calculation
            calculationService.calculateTurnoverTax(trigger);
            
            System.out.println("✅ Calculation request processed successfully");
            System.out.println("\n\n");
            
        } catch (Exception e) {
            System.err.println("❌ Failed to process calculation request: " + e.getMessage());
            e.printStackTrace();
            System.err.println("\n\n");
        }
    }
}