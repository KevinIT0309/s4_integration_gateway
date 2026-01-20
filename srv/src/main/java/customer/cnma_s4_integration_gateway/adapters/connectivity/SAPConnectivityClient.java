package customer.cnma_s4_integration_gateway.adapters.connectivity;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.sap.cloud.sdk.cloudplatform.connectivity.DefaultCsrfTokenRetriever;
import com.sap.cloud.sdk.cloudplatform.connectivity.DefaultDestinationLoader;
import com.sap.cloud.sdk.cloudplatform.connectivity.DestinationAccessor;
import com.sap.cloud.sdk.cloudplatform.connectivity.DestinationLoader;
import com.sap.cloud.sdk.cloudplatform.connectivity.HttpDestination;

import cds.gen.cnma.adminconfiguration.DefaultValues;
import customer.cnma_s4_integration_gateway.constants.AppConstant;
import customer.cnma_s4_integration_gateway.repositories.AdminConfigurationRepository;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Component
public class SAPConnectivityClient {

    
    private String ERP_DESTINATION;

    private HttpDestination destination;
    @Autowired
    private AdminConfigurationRepository adminConfigurationRepository;

    @PostConstruct
    public void init(){
        configureErpBackendDestination();
    }

    
    public void configureErpBackendDestination() {

        try {
            if (this.ERP_DESTINATION == null || this.ERP_DESTINATION.isEmpty()) {

                log.info("::🛠:: ERP_DESTINATION environment variable not set, retrieving from Admin Configuration");

                DefaultValues defaultValue_ERP_DESTINATION = adminConfigurationRepository.retrieveDefaultValueByFieldValue(DefaultValues.PARAM_NAME, AppConstant.ERP_DESTINATION)
                .stream().findFirst().orElse(null);

                this.ERP_DESTINATION = defaultValue_ERP_DESTINATION.getParamValue();

                log.info("::🛠:: Connecting to destination: {}", ERP_DESTINATION);
                
            } else {
                log.error("::❌:: ERP_DESTINATION not configured in Admin Configuration");
                return;
            }

            HttpDestination destination = DestinationAccessor.getDestination(this.ERP_DESTINATION).asHttp();
            DestinationLoader destinationLoader = new DefaultDestinationLoader().registerDestination(destination);
            DestinationAccessor.prependDestinationLoader(destinationLoader);

            log.info("::🗳:: Destination connected data: {}", destination.toString());
            this.destination = destination;
            log.info("::🚀:: Destination: |{}| is initialized", this.ERP_DESTINATION);

        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            log.error("::❌:: Error at {}.{}(line {}): {}",
                element.getClassName(), element.getMethodName(), element.getLineNumber(), e.getMessage());
        }
    }




    public SapCsrfToken retrieveCsrfToken(HttpClient httpClient, String servicePath) {
        
        try {

            CookieStore cookieStore = new BasicCookieStore();
            HttpClientContext context = HttpClientContext.create();
            context.setCookieStore(cookieStore);
            
            HttpGet getRequest = new HttpGet(servicePath);
            getRequest.setHeader(DefaultCsrfTokenRetriever.X_CSRF_TOKEN_HEADER_KEY, "fetch");

            HttpResponse response = httpClient.execute(getRequest, context);

            String token = response.getFirstHeader(DefaultCsrfTokenRetriever.X_CSRF_TOKEN_HEADER_KEY).getValue();

            String cookie = cookieStore.getCookies().stream()
            .filter(c -> c.getName().startsWith("SAP_SESSIONID"))
            .map(c -> c.getName() + "=" + c.getValue())
            .findFirst()
            .orElse(null);

            return new SapCsrfToken(token, cookie);

        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch CSRF token", e);
        }
    }

}
