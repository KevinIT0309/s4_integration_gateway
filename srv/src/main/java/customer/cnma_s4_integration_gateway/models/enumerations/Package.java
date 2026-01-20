package customer.cnma_s4_integration_gateway.models.enumerations;

public enum Package {
    
    PurchaseOrder("PO"),
    SaleOrder("SO"),
    AdvanceShipmentNotification("ASN");

    
    private final String value;

    private Package(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
