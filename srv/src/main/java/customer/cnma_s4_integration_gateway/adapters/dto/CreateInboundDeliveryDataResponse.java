package customer.cnma_s4_integration_gateway.adapters.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateInboundDeliveryDataResponse {
    
    private String delivery;
    private String materialDocument;

}
