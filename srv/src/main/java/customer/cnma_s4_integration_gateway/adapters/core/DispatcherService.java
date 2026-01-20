package customer.cnma_s4_integration_gateway.adapters.core;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import cds.gen.cnma.common.ResponseContext;
import customer.cnma_s4_integration_gateway.adapters.PackageHandler;
import customer.cnma_s4_integration_gateway.adapters.config.AdapterConfig;
import customer.cnma_s4_integration_gateway.adapters.config.ClientConfig;
import customer.cnma_s4_integration_gateway.adapters.context.RequestContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DispatcherService {
    
    
    private final AdapterConfig adapterConfig;
    private final Map<String, PackageHandler> handlers = new HashMap<>();
    
    public DispatcherService(AdapterConfig adapterConfig, List<PackageHandler> handlerList, ApplicationContext applicationContext) {

        log.info("::🚀:: DispatcherService initial ...");

        this.adapterConfig = adapterConfig;
        
        for (PackageHandler handler : handlerList) {
            String[] beanNames = applicationContext.getBeanNamesForType(handler.getClass());
            if (beanNames.length > 0) {
                handlers.put(beanNames[0], handler);
            }
        }
        
        log.info("::🔧:: adapterConfig: ➡️ {}", adapterConfig.getClients().toString());
        log.info("::🔧:: handlers: ➡️ {}",handlers.keySet());
    }



    public ResponseContext dispatch(RequestContext ctx) {

        String clientId = ctx.getClientId();
        String packageName = ctx.getPackageName();
        String action = ctx.getAction();

        log.info("::🔎:: [clientId, packageName, action]: [{},{},{}]", clientId, packageName, action);

        /* -------------------------------------- */
        /* ::::: Searching Responsible Bean ::::: */
        /* -------------------------------------- */
        String handlerBean = adapterConfig.getClients()
            .getOrDefault(clientId, new ClientConfig())
            .getPackages()
            .getOrDefault(packageName, packageName + "_Default");

        log.info("::🔮:: Handler Bean: {}", handlerBean);
        PackageHandler handler = handlers.get(handlerBean);

        if (handler == null) {
            throw new IllegalArgumentException("::⭕:: No handler bean found for: " + handlerBean);
        }
        
        /* ------------------------------ */
        /* ::::: Handler Processing ::::: */
        /* ------------------------------ */
        log.info("::🌀:: Handler Class: {}", handler.getClass().getTypeName());
        return handler.handle(ctx);
    }

}
