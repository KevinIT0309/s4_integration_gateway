package customer.cnma_s4_integration_gateway.adapters.config;

import lombok.Data;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "adapter")
public class AdapterConfig {
    
    private Map<String, ClientConfig> clients;

}