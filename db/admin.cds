namespace cnma.adminconfiguration;

@cds.persistence.exists
entity DefaultValues {
    key objType          : String;
    key paramName        : String;
        paramValue       : String;
        paramDescription : String;
        refObjectType    : String;
        refFieldName     : String;
        isRefObject      : Boolean;
}