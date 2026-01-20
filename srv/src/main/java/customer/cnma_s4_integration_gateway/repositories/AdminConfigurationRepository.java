package customer.cnma_s4_integration_gateway.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sap.cds.ql.Select;
import com.sap.cds.services.persistence.PersistenceService;

import cds.gen.cnma.adminconfiguration.Adminconfiguration_;
import cds.gen.cnma.adminconfiguration.DefaultValues;
import customer.cnma_s4_integration_gateway.constants.AppConstant;

@Repository
public class AdminConfigurationRepository {
    
    @Autowired
    private PersistenceService db;

    public List<DefaultValues> retrieveDefaultValueByFieldValue(String field, Object value){

        return db.run(
            Select.from(Adminconfiguration_.DEFAULT_VALUES)
            .where(
                o -> o.get(field).is(value)
                .and(o.objType().eq(AppConstant.OBJECT_TYPE))
            )
        ).listOf(DefaultValues.class);
        
    }
}
