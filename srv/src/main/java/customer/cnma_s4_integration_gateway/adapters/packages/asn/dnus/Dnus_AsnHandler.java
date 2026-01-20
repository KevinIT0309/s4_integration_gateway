package customer.cnma_s4_integration_gateway.adapters.packages.asn.dnus;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import cds.gen.cnma.common.ActionMessage;
import cds.gen.cnma.common.AsnAction;
import cds.gen.cnma.common.IssuedFrom;
import cds.gen.cnma.common.ResponseContext;
import cds.gen.cnma.type.SubmitAsnPayload;
import cds.gen.cnma.type.AttachDocumentPayload;
import customer.cnma_s4_integration_gateway.adapters.PackageHandler;
import customer.cnma_s4_integration_gateway.adapters.context.AsnRequestContext;
import customer.cnma_s4_integration_gateway.adapters.context.RequestContext;
import customer.cnma_s4_integration_gateway.annotations.ProconarumLogExecutionTime;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("DNUS_ASN")
public class Dnus_AsnHandler implements PackageHandler {


    @Autowired
    private Dnus_AsnProcessor processor;
    

    @Override
    @Transactional
    @ProconarumLogExecutionTime
    public ResponseContext handle(RequestContext context) {


        log.info("::💡:: [Proconarum_AsnHandler] handle for context: {}", context.getAction());

        switch (context.getAction()) {

            case AsnAction.SUBMIT: {

                @SuppressWarnings("unchecked")
                AsnRequestContext<SubmitAsnPayload> asnRequestContext = (AsnRequestContext<SubmitAsnPayload>) context;
                return processor.createInboundDelivery(asnRequestContext.getData());

            }

            case AsnAction.UPDATE: {
                break;
            }

            case AsnAction.DELETE: {
                break;
            }


            case AsnAction.ATTACH_DOCUMENT: {

                @SuppressWarnings("unchecked")
                AsnRequestContext<AttachDocumentPayload> asnRequestContext = (AsnRequestContext<AttachDocumentPayload>) context;
                return processor.attachDocument(asnRequestContext.getData());
            }


            default: break;
        }

        ResponseContext responseContext = ResponseContext.create();
        responseContext.setIsSuccess(false);

        ActionMessage actionMessage = ActionMessage.create();
        actionMessage.setIssuedFrom(IssuedFrom.CAP);
        actionMessage.setContent("Nothing processed");

        responseContext.setMessages(Collections.singletonList(actionMessage));

        return responseContext;
        

    }




    


    
}
