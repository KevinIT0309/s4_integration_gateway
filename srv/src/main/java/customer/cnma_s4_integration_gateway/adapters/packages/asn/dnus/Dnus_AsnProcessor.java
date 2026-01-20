package customer.cnma_s4_integration_gateway.adapters.packages.asn.dnus;


import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.sap.cloud.sdk.datamodel.odata.helper.ModificationResponse;

import cds.gen.cnma.common.ActionMessage;
import cds.gen.cnma.common.IssuedFrom;
import cds.gen.cnma.common.ResponseContext;
import cds.gen.cnma.type.AttachDocumentPayload;
import cds.gen.cnma.type.SubmitAsnPayload;
import customer.cnma_s4_integration_gateway.adapters.dto.CreateInboundDeliveryDataResponse;
import customer.cnma_s4_integration_gateway.utils.CollectionUtil;
import customer.cnma_s4_integration_gateway.vdm.dnus.namespaces.asnsrv.InboundDelivery;
import customer.cnma_s4_integration_gateway.vdm.dnus.namespaces.asnsrv.InboundDeliveryDocument;
import customer.cnma_s4_integration_gateway.vdm.dnus.namespaces.asnsrv.MessageResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class Dnus_AsnProcessor {

    @Autowired
    private Dnus_AsnExecutor executor;
    @Autowired
    private Dnus_PayloadMapper payloadMapper;
    @Autowired
    private ObjectMapper objectMapper;

    

    public ResponseContext createInboundDelivery(SubmitAsnPayload payload) {
        
        ResponseContext responseContext = null;
        
        try {

            responseContext = ResponseContext.create();

            /* ::::: Mapping Data ::::: */
            InboundDelivery inboundDelivery = payloadMapper.buildPayloadCreateInboundDelivery(payload);
            log.info("::📦:: inboundDelivery: {}", new Gson().toJson(inboundDelivery));
            
            ModificationResponse<InboundDelivery> result = executor.createInboundDeliveryToSapS4(inboundDelivery, "en");

            if (result.getModifiedEntity() != null){

                log.info("::🔵:: ModifiedEntity data response: {}", new Gson().toJson(result.getResponseEntity().get()));

                if (CollectionUtil.isCollectionContainingElements(result.getResponseEntity().get().getInboundDelivery2HeaderIfPresent().get())){

                    String delivery = result.getResponseEntity().get().getInboundDelivery2HeaderIfPresent().get().getFirst().getDelivery();
                    String materialDocument = result.getResponseEntity().get().getInboundDelivery2HeaderIfPresent().get().getFirst().getMaterialDoc();

                    CreateInboundDeliveryDataResponse dataResponse = CreateInboundDeliveryDataResponse.builder()
                        .delivery(delivery)
                        .materialDocument(materialDocument)
                    .build();

                    log.info("::🔵:: CreateInboundDeliveryDataResponse: {}", dataResponse.toString());

                    responseContext.setIsSuccess(true);
                    responseContext.setResult(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(dataResponse));
                }

                else {
                    if (CollectionUtil.isCollectionContainingElements(result.getModifiedEntity().getInboundDelivery2MessageResponseOrFetch())){
                        
                        List<MessageResponse> messageResponses = result.getModifiedEntity().getInboundDelivery2MessageResponseOrFetch().stream()
                        .filter(CollectionUtil.distinctByKey(MessageResponse::getMessage))
                        .collect(Collectors.toList());

                        log.info("::🔵:: message response: {}", messageResponses.getFirst().getMessage());

                        responseContext.setIsSuccess(false);
                        List<ActionMessage> actionMessages = messageResponses.stream()
                            .map(sapMessage -> {
                                ActionMessage actionMessage = ActionMessage.create();
                                actionMessage.setIssuedFrom(IssuedFrom.SAP_BACKEND);
                                actionMessage.setContent(sapMessage.getMessage());
                                return actionMessage;
                            })
                            .collect(Collectors.toList());
                        
                        responseContext.setMessages(actionMessages);
                    }
                }
                
            }

            else {
                
                ActionMessage actionMessage = ActionMessage.create();
                actionMessage.setIssuedFrom(IssuedFrom.SAP_BACKEND);
                actionMessage.setContent("Failed when submit ASN");

                responseContext.setIsSuccess(false);
                responseContext.setMessages(Collections.singletonList(actionMessage));
            }

            return responseContext;

        } catch (Exception e){

            StackTraceElement element = e.getStackTrace()[0];
            log.error("::❌:: Error at {}.{}(line {}): {}",
                element.getClassName(), element.getMethodName(), element.getLineNumber(), e.getMessage());

            ActionMessage actionMessage = ActionMessage.create();
            actionMessage.setIssuedFrom(IssuedFrom.SAP_BACKEND);
            actionMessage.setContent(e.getMessage());

            responseContext.setIsSuccess(false);
            responseContext.setMessages(Collections.singletonList(actionMessage));

            return responseContext;
        }

    }





    public ResponseContext attachDocument(AttachDocumentPayload payload) {
        
        ResponseContext responseContext = null;
        
        try {

            responseContext = ResponseContext.create();

            /* ::::: Mapping Data ::::: */
            InboundDeliveryDocument inboundDeliveryDocument = payloadMapper.buildPayloadAttachDocument(payload);
                log.info("::📦:: inboundDeliveryDocument(SapDelivery={}, FileID={}, FileType={}, FileName={}, FileValue={})",
                inboundDeliveryDocument.getDelivery(),
                inboundDeliveryDocument.getFileID(),
                inboundDeliveryDocument.getFileType(),
                inboundDeliveryDocument.getFileName(),
                (inboundDeliveryDocument.getFileValue() != null && !inboundDeliveryDocument.getFileValue().isEmpty()) ? inboundDeliveryDocument.getFileValue().substring(0, 10) + "..." : "empty"
            );
            
            ModificationResponse<InboundDeliveryDocument> result = executor.attachDocumentInboundDeliveryToSapS4(inboundDeliveryDocument, "en");

            if (result.getResponseEntity().isDefined()){

                log.info("::🔵:: ModifiedEntity data response: {}", new Gson().toJson(result.getResponseEntity().get()));

                responseContext.setIsSuccess(true);
                responseContext.setResult("success");
            }

            else {

                responseContext.setIsSuccess(false);
                ActionMessage actionMessage = ActionMessage.create();
                actionMessage.setIssuedFrom(IssuedFrom.SAP_BACKEND);
                actionMessage.setContent("Failed to attach document to SAP backend");
                responseContext.setMessages(Collections.singletonList(actionMessage));

            }

            return responseContext;

        } catch (Exception e){

            StackTraceElement element = e.getStackTrace()[0];
            log.error("::❌:: Error at {}.{}(line {}): {}",
                element.getClassName(), element.getMethodName(), element.getLineNumber(), e.getMessage());

            ActionMessage actionMessage = ActionMessage.create();
            actionMessage.setIssuedFrom(IssuedFrom.SAP_BACKEND);
            actionMessage.setContent(e.getMessage());

            responseContext.setIsSuccess(false);
            responseContext.setMessages(Collections.singletonList(actionMessage));

            return responseContext;
        }

    }




    

    





    
}
