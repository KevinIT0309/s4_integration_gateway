package customer.cnma_s4_integration_gateway.adapters.connectivity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SapCsrfToken {

    private String token;
    private String cookie;
    
}
