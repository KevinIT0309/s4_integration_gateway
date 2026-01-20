package customer.cnma_s4_integration_gateway.adapters;

import cds.gen.cnma.common.ResponseContext;
import customer.cnma_s4_integration_gateway.adapters.context.RequestContext;

public interface PackageHandler {
    
    ResponseContext handle(RequestContext context);
    
}
