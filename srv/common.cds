namespace cnma.common;



@readonly
type ResponseContext {
    isSuccess   : Boolean;
    result      : LargeString;
    messages    : array of ActionMessage;
}

type IssuedFrom : String @assert.range enum {
    SAP_BACKEND = 'SAP_BACKEND';
    CAP = 'CAP';
}
@readonly
type ActionMessage {
    issuedFrom: IssuedFrom;
    content: String;
}


type AsnAction : String @assert.range enum {
    Submit = 'Submit';
    Update = 'Update';
    Delete = 'Delete';
    AttachDocument = 'AttachDocument';
}