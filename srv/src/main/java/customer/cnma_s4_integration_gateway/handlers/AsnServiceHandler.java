package customer.cnma_s4_integration_gateway.handlers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.sap.cds.services.handler.EventHandler;
import com.sap.cds.services.handler.annotations.On;
import com.sap.cds.services.handler.annotations.ServiceName;

import cds.gen.asn_cloud_srv.AsnCloudSrv_;
import cds.gen.asn_cloud_srv.AttachDocumentContext;
import cds.gen.asn_cloud_srv.SubmitAsnContext;
import cds.gen.cnma.adminconfiguration.DefaultValues;
import cds.gen.cnma.common.AsnAction;
import cds.gen.cnma.common.ResponseContext;
import customer.cnma_s4_integration_gateway.adapters.core.DispatcherService;
import customer.cnma_s4_integration_gateway.constants.AppConstant;
import customer.cnma_s4_integration_gateway.adapters.context.AsnRequestContext;
import customer.cnma_s4_integration_gateway.models.enumerations.Package;
import customer.cnma_s4_integration_gateway.repositories.AdminConfigurationRepository;
import customer.cnma_s4_integration_gateway.utils.DataUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Qualifier(AsnCloudSrv_.CDS_NAME)
@ServiceName(AsnCloudSrv_.CDS_NAME)
public class AsnServiceHandler implements EventHandler {


    /* ------------------------------------------------------ */
    /* :::::::::::::::::: INJECTIONS SPACE :::::::::::::::::: */
    /* ------------------------------------------------------ */
    private String CLIENT_ID;

    @Autowired
    private DispatcherService dispatcherService;
    @Autowired
    private AdminConfigurationRepository adminConfigurationRepository;



    void setupClientId(){
        if (this.CLIENT_ID == null || this.CLIENT_ID.isEmpty()){

            DefaultValues defaultValue_CLIENT_ID = adminConfigurationRepository.retrieveDefaultValueByFieldValue(DefaultValues.PARAM_NAME, AppConstant.CLIENT_ID)
            .stream().findFirst().orElse(null);

            if (defaultValue_CLIENT_ID == null) {
                log.error("::❌:: CLIENT_ID not configured in Admin Configuration");
                return;
            }

            this.CLIENT_ID = defaultValue_CLIENT_ID.getParamValue();
        }

    }



    /* --------------------------------------------------- */
    /* :::::::::::::::::: ACTIONS SPACE :::::::::::::::::: */
    /* --------------------------------------------------- */
    @On(event = SubmitAsnContext.CDS_NAME)
    public void submitAsn(SubmitAsnContext context){

        log.info("::🔵:: ACTION: submitAsn");

        if (DataUtil.isNotNull(context.getData())){

            // String language = (DataUtil.isNotNull(context.getParameterInfo().getLocale())) ? context.getParameterInfo().getLocale().getLanguage() : "en";
            /* ------------------------- */
            /* ::::: Build Context ::::: */
            /* ------------------------- */
            AsnRequestContext<?> ctx = AsnRequestContext.builder()
            .clientId(CLIENT_ID)
            .packageName(Package.AdvanceShipmentNotification.getValue())
            .action(AsnAction.SUBMIT)
            .data(context.getData()).build();

            /* --------------------------------- */
            /* ::::: Dispatcher Processing ::::: */
            /* --------------------------------- */
            ResponseContext responseContext = dispatcherService.dispatch(ctx);
            context.setResult(responseContext);
            context.setCompleted();
        }
        
    }


    @On(event = AttachDocumentContext.CDS_NAME)
    public void attachDocument(AttachDocumentContext context){

        log.info("::🔵:: ACTION: attachDocument");

        if (DataUtil.isNotNull(context.getData())){
            
            /* ------------------------- */
            /* ::::: Build Context ::::: */
            /* ------------------------- */
            AsnRequestContext<?> ctx = AsnRequestContext.builder()
            .clientId(CLIENT_ID)
            .packageName(Package.AdvanceShipmentNotification.getValue())
            .action(AsnAction.ATTACH_DOCUMENT)
            .data(context.getData()).build();

            /* --------------------------------- */
            /* ::::: Dispatcher Processing ::::: */
            /* --------------------------------- */
            ResponseContext responseContext = dispatcherService.dispatch(ctx);
            context.setResult(responseContext);
            context.setCompleted();
        }
        
    }


    
}
