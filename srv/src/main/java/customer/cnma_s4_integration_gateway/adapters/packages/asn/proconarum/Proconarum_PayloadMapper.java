package customer.cnma_s4_integration_gateway.adapters.packages.asn.proconarum;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import cds.gen.cnma.type.SubmitAsnPayload;
import cds.gen.cnma.type.AttachDocumentPayload;
import customer.cnma_s4_integration_gateway.utils.DataUtil;
import customer.cnma_s4_integration_gateway.vdm.proconarum.namespaces.asnsrv.InboundDelivery;
import customer.cnma_s4_integration_gateway.vdm.proconarum.namespaces.asnsrv.InboundDeliveryAutoHU;
import customer.cnma_s4_integration_gateway.vdm.proconarum.namespaces.asnsrv.InboundDeliveryDocument;
import customer.cnma_s4_integration_gateway.vdm.proconarum.namespaces.asnsrv.InboundDeliveryHUHeader;
import customer.cnma_s4_integration_gateway.vdm.proconarum.namespaces.asnsrv.InboundDeliveryHUItem;
import customer.cnma_s4_integration_gateway.vdm.proconarum.namespaces.asnsrv.InboundDeliveryHeader;
import customer.cnma_s4_integration_gateway.vdm.proconarum.namespaces.asnsrv.InboundDeliveryHeaderExtension;
import customer.cnma_s4_integration_gateway.vdm.proconarum.namespaces.asnsrv.InboundDeliveryItem;
import customer.cnma_s4_integration_gateway.vdm.proconarum.namespaces.asnsrv.InboundDeliveryItemExtension;
import customer.cnma_s4_integration_gateway.vdm.proconarum.namespaces.asnsrv.MessageResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class Proconarum_PayloadMapper {
    



    public InboundDelivery buildPayloadCreateInboundDelivery(SubmitAsnPayload asnPayload){

        log.info("::🛠:: buildPayloadCreateInboundDelivery");
        
        InboundDelivery inboundDelivery = new InboundDelivery();

        /* Declare elements of Inbound Delivery Data */
        
        List<InboundDeliveryHeader> InboundDelivery2Header = new ArrayList<>();
        List<InboundDeliveryItem> InboundDelivery2Item = new ArrayList<>();
        
        List<InboundDeliveryAutoHU> InboundDelivery2AutoHU = new ArrayList<>();
        List<InboundDeliveryHUHeader> InboundDelivery2AutoHUResp = new ArrayList<InboundDeliveryHUHeader>();
        List<MessageResponse> handlingUnitMessageResponseS4List = mappingHandlingHandlerMessageResponseAsnSrv();

        List<InboundDeliveryItemExtension> InboundDelivery2ItemExtension = new ArrayList<>();
        List<InboundDeliveryHeaderExtension> InboundDelivery2HeaderExtension = new ArrayList<>();

        /* Prepare data InboundDelivery2Header */
        InboundDeliveryHeader inboundDeliveryHeader = new InboundDeliveryHeader();

        inboundDeliveryHeader.setAction(asnPayload.getAction());
        inboundDeliveryHeader.setBillOfLading(asnPayload.getPackingSlip());

        inboundDeliveryHeader.setDelivery(asnPayload.getInboundDelivery2Header().getDeliveryDocument());
        inboundDeliveryHeader.setSupplier(asnPayload.getInboundDelivery2Header().getSupplier());
        inboundDeliveryHeader.setDeliveryDate(asnPayload.getInboundDelivery2Header().getDeliveryDate().replace("-", ""));
        inboundDeliveryHeader.setDeliveryNote(asnPayload.getInboundDelivery2Header().getDeliveryNote());
        inboundDeliveryHeader.setShipmentID(asnPayload.getInboundDelivery2Header().getExternalDeliveryDocument());

        inboundDeliveryHeader.setDocumentDate(asnPayload.getInboundDelivery2Header().getDocumentDate().replace("-", ""));
        inboundDeliveryHeader.setDeliveryGroup(asnPayload.getInboundDelivery2Header().getDeliveryGroup());

        inboundDeliveryHeader.setTotalGrossWeight(DataUtil.safeBigDecimal(asnPayload.getInboundDelivery2Header().getTotalGrossWeight()));

        inboundDeliveryHeader.setUnitOfTotalWeight(asnPayload.getInboundDelivery2Header().getUnitOfWeight());
        
        inboundDeliveryHeader.setIsUpdateWeight(asnPayload.getInboundDelivery2Header().getIsUpdateWeight());
        inboundDeliveryHeader.setIsPostGoodsReceipt(asnPayload.getInboundDelivery2Header().getIsPostGoodsReceipt());
        inboundDeliveryHeader.setIsPostInvoice(asnPayload.getInboundDelivery2Header().getIsPostInvoice());

        InboundDelivery2Header.add(inboundDeliveryHeader);

        for (cds.gen.cnma.type.InboundDeliveryItem itemPayload: asnPayload.getInboundDelivery2Item()){

            InboundDeliveryItem inboundDeliveryItem = new InboundDeliveryItem();
            
            inboundDeliveryItem.setAction(itemPayload.getAction());
            inboundDeliveryItem.setDelivery(null);
            inboundDeliveryItem.setDeliveryItem(itemPayload.getDeliveryDocumentItem());
            inboundDeliveryItem.setPONumber(itemPayload.getPoNumber());
            inboundDeliveryItem.setPOItem(itemPayload.getPoItem());
            inboundDeliveryItem.setMaterial(itemPayload.getMaterial());
            inboundDeliveryItem.setSupplierMaterial(itemPayload.getSupplierMaterial());
            inboundDeliveryItem.setPlant(itemPayload.getPlant());
            inboundDeliveryItem.setStorageLocation(itemPayload.getStorageLocation());
            inboundDeliveryItem.setBatch(itemPayload.getBatch());
            inboundDeliveryItem.setDeliveryQuantity(DataUtil.safeBigDecimal(itemPayload.getDeliveryQuantity()));
            inboundDeliveryItem.setUnitOfQuantity(itemPayload.getUnitOfQuantity());
            inboundDeliveryItem.setSupplier(itemPayload.getSupplier());
            inboundDeliveryItem.setDeliveryNote(itemPayload.getDeliveryNote());

            inboundDeliveryItem.setSupplier(asnPayload.getInboundDelivery2Header().getSupplier());
            inboundDeliveryItem.setDeliveryDate(asnPayload.getInboundDelivery2Header().getDeliveryDate().replace("-", ""));

            inboundDeliveryItem.setGrossWeight(DataUtil.safeBigDecimal(itemPayload.getGrossWeight()));
            inboundDeliveryItem.setNetWeight(DataUtil.safeBigDecimal(itemPayload.getNetWeight()));

            inboundDeliveryItem.setUnitOfWeight(itemPayload.getUnitOfWeight());
            inboundDeliveryItem.setManifestNumber(itemPayload.getManifestNumber());
            inboundDeliveryItem.setShipmentID(itemPayload.getExternalDeliveryDocument());
            inboundDeliveryItem.setShipmentItemID(itemPayload.getDeliveryDocumentItem());
            inboundDeliveryItem.setUnitOfVolume(itemPayload.getUnitOfVolume());

            inboundDeliveryItem.setVolume(DataUtil.safeBigDecimal(itemPayload.getVolume()));

            InboundDelivery2Item.add(inboundDeliveryItem);

        }


        /*--------------------------------------------------------------*/
        /* Prepare data InboundDelivery2AutoHUResp []                   */
        /*--------------------------------------------------------------*/
        List<InboundDeliveryHUItem> HandlingUnit2Item = new ArrayList<InboundDeliveryHUItem>();
        InboundDeliveryHUItem dummy = new InboundDeliveryHUItem();
        HandlingUnit2Item.add(dummy);

        InboundDeliveryHUHeader inboundDeliveryHuHeader = new InboundDeliveryHUHeader();
        inboundDeliveryHuHeader.setHandlingUnit2Item(HandlingUnit2Item);
        InboundDelivery2AutoHUResp.add(inboundDeliveryHuHeader);

        /* -------------------------------------------- */
        /* Prepare Request Inbound Delivery data        */
        /* -------------------------------------------- */
        inboundDelivery.setAction(asnPayload.getAction());
        inboundDelivery.setInboundDelivery2Header(InboundDelivery2Header);

        inboundDelivery.setInboundDelivery2Item(
        InboundDelivery2Item.stream()
            .sorted(
                Comparator.comparing(
                    InboundDeliveryItem::getShipmentItemID,
                    Comparator.nullsLast(Comparator.naturalOrder())
                )
            )
            .collect(Collectors.toList())
        );


        inboundDelivery.setInboundDelivery2AutoHU(InboundDelivery2AutoHU);
        inboundDelivery.setInboundDelivery2AutoHUResp(InboundDelivery2AutoHUResp);
        inboundDelivery.setInboundDelivery2MessageResponse(handlingUnitMessageResponseS4List);

        inboundDelivery.setInboundDelivery2ItemExtension(InboundDelivery2ItemExtension);
        inboundDelivery.setInboundDelivery2HeaderExtension(InboundDelivery2HeaderExtension);

        return inboundDelivery;// ready to use
    }


    protected List<MessageResponse> mappingHandlingHandlerMessageResponseAsnSrv(){
        List<MessageResponse> toHandlingUnitHandler2MessageResponse = new ArrayList<>();
        MessageResponse message = new MessageResponse();
        toHandlingUnitHandler2MessageResponse.add(message);
        return toHandlingUnitHandler2MessageResponse;
    }




    public InboundDeliveryDocument buildPayloadAttachDocument(AttachDocumentPayload asnPayload){

        log.info("::🛠:: buildPayloadAttachDocument");

        InboundDeliveryDocument inboundDeliveryDocument = new InboundDeliveryDocument();

        inboundDeliveryDocument.setDelivery(asnPayload.getDeliveryDocument());

        inboundDeliveryDocument.setFileID(asnPayload.getFileID());
        inboundDeliveryDocument.setFileType(
            (asnPayload.getFileType() != null) ? asnPayload.getFileType() : "EDXAASNPDF"
        );
        inboundDeliveryDocument.setFileName(asnPayload.getFileName());
        String base64File = asnPayload.getFileValue();
        inboundDeliveryDocument.setFileValue(base64File);
        
        return inboundDeliveryDocument;// ready to use
    }




}
