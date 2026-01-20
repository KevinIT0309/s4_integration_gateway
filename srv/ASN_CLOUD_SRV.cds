using cnma.type as type from '../srv/type';
using cnma.common as common from '../srv/common';

@path:'cnma/ASN_CLOUD_SRV'
service ASN_CLOUD_SRV {

    action submitAsn(data: type.SubmitAsnPayload) returns common.ResponseContext;
    
    action attachDocument(data: type.AttachDocumentPayload) returns common.ResponseContext;
    
}