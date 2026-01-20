package customer.cnma_s4_integration_gateway.adapters.packages.asn.proconarum;

import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sap.cloud.sdk.cloudplatform.connectivity.DefaultCsrfTokenRetriever;
import com.sap.cloud.sdk.cloudplatform.connectivity.HttpClientAccessor;
import com.sap.cloud.sdk.datamodel.odata.helper.ModificationResponse;

import customer.cnma_s4_integration_gateway.adapters.connectivity.SAPConnectivityClient;
import customer.cnma_s4_integration_gateway.adapters.connectivity.SapCsrfToken;
import customer.cnma_s4_integration_gateway.vdm.proconarum.namespaces.asnsrv.InboundDelivery;
import customer.cnma_s4_integration_gateway.vdm.proconarum.namespaces.asnsrv.InboundDeliveryDocument;
import customer.cnma_s4_integration_gateway.vdm.proconarum.services.DefaultASNSRVService;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class Proconarum_AsnExecutor {
    

    @Autowired
    private SAPConnectivityClient sapConnectivityClient;


    protected ModificationResponse<InboundDelivery> createInboundDeliveryToSapS4(InboundDelivery inboundDelivery, String language){
        
        log.info("::🔘:: [createInboundDeliveryToSapS4] ⚪ endpoint service: {}", DefaultASNSRVService.DEFAULT_SERVICE_PATH);

        HttpClient httpClient = HttpClientAccessor.getHttpClient(sapConnectivityClient.getDestination());
        // quick method retrieve csrf token
        // CsrfToken csrfToken = new DefaultCsrfTokenRetriever().retrieveCsrfToken(httpClient, DefaultASNSRVService.DEFAULT_SERVICE_PATH);

        SapCsrfToken csrfToken = sapConnectivityClient.retrieveCsrfToken(httpClient, DefaultASNSRVService.DEFAULT_SERVICE_PATH);
        log.info("::🖇:: CSRF (token,cookie): ({},{})", csrfToken.getToken(), csrfToken.getCookie());

        return new DefaultASNSRVService()
        .createInboundDeliverySet(inboundDelivery)

        .withHeader("sap-language", language)
        .withHeader("Accept", "application/json")
        .withHeader("Content-Type", "application/json")

        .withHeader(DefaultCsrfTokenRetriever.X_CSRF_TOKEN_HEADER_KEY, csrfToken.getToken())
        .withHeader("Cookie", csrfToken.getCookie())
        
        .executeRequest(sapConnectivityClient.getDestination());

    }



    protected ModificationResponse<InboundDeliveryDocument> attachDocumentInboundDeliveryToSapS4(InboundDeliveryDocument inboundDeliveryDocument, String language){
        
        log.info("::🔘:: [attachDocumentInboundDeliveryToSapS4] ⚪ endpoint service: {}", DefaultASNSRVService.DEFAULT_SERVICE_PATH);

        HttpClient httpClient = HttpClientAccessor.getHttpClient(sapConnectivityClient.getDestination());
        // quick method retrieve csrf token
        // CsrfToken csrfToken = new DefaultCsrfTokenRetriever().retrieveCsrfToken(httpClient, DefaultASNSRVService.DEFAULT_SERVICE_PATH);

        SapCsrfToken csrfToken = sapConnectivityClient.retrieveCsrfToken(httpClient, DefaultASNSRVService.DEFAULT_SERVICE_PATH);
        log.info("::🖇:: CSRF (token,cookie): ({},{})", csrfToken.getToken(), csrfToken.getCookie());

        return new DefaultASNSRVService()
        .createInboundDeliveryDocumentSet(inboundDeliveryDocument)

        .withHeader("sap-language", language)
        .withHeader("Accept", "application/json")
        .withHeader("Content-Type", "application/json")

        .withHeader(DefaultCsrfTokenRetriever.X_CSRF_TOKEN_HEADER_KEY, csrfToken.getToken())
        .withHeader("Cookie", csrfToken.getCookie())
        
        .executeRequest(sapConnectivityClient.getDestination());

    }
}
