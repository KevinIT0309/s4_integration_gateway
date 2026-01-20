namespace cnma.type;

@readonly type SubmitAsnPayload {
    
    packingSlip: String;
    action: String;
    inboundDelivery2Header: InboundDeliveryHeader;
    inboundDelivery2Item: array of InboundDeliveryItem;

}

@readonly type InboundDeliveryHeader {
    action: String;
    deliveryDocument: String;
    supplier: String;
    deliveryDate: String;
    deliveryNote: String;
    externalDeliveryDocument: String;
    documentDate: String;
    deliveryGroup: String;
    totalGrossWeight: String;
    unitOfWeight: String;
    isUpdateWeight: Boolean;
    isPostGoodsReceipt: Boolean;
    isPostInvoice: Boolean;
}

@readonly type InboundDeliveryItem {
    action: String;
    deliveryDocument: String;
    deliveryDocumentItem: String;
    deliveryDate: String;
    poNumber: String;
    poItem: String;
    material: String;
    supplierMaterial: String;
    plant: String;
    storageLocation: String;
    batch: String;
    deliveryQuantity: String;
    unitOfQuantity: String;
    supplier: String;
    deliveryNote: String;
    grossWeight: String;
    netWeight: String;
    unitOfWeight: String;
    manifestNumber: String;
    externalDeliveryDocument: String;
    externalDeliveryDocumentItem: String;
    unitOfVolume: String;
    volume: String;
}


@readonly type AttachDocumentPayload {

    deliveryDocument: String;
    
    fileID: String;
    fileType: String default 'EDXAASNPDF';
    fileName: String;
    fileValue: LargeString;

}