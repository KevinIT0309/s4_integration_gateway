package customer.cnma_s4_integration_gateway.adapters.context;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.AllArgsConstructor;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class RequestContext {

    private String clientId;
    private String packageName;
    private String action;

    

}