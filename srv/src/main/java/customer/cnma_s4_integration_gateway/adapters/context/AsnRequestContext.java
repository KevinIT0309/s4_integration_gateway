package customer.cnma_s4_integration_gateway.adapters.context;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class AsnRequestContext<T> extends RequestContext {
    
    private T data;

    public AsnRequestContext(){
        super();
    }

    public AsnRequestContext(String clientId, String packageName, String traceId, T data){
        super(clientId, packageName, traceId);
        this.data = data;
    }

    public T getData(){
        return this.data;
    }

    public void setData(T data){
        this.data = data;
    }

}
