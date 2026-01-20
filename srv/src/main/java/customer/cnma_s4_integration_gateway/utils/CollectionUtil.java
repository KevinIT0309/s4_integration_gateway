package customer.cnma_s4_integration_gateway.utils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

import org.apache.commons.collections4.MultiValuedMap;

public class CollectionUtil {
    
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }
    
    public static <T> Boolean isListContainingElements(List<T> inputList){
        return (inputList != null && !inputList.isEmpty()) ? Boolean.TRUE : Boolean.FALSE;
    }

    public static Boolean isMapContainingElements(Map<?, ?> map) {
        return (map != null && !map.isEmpty()) ? Boolean.TRUE : Boolean.FALSE;
    }

    public static Boolean isMultiValueMapContainingElements(MultiValuedMap<?, ?> map) {
        return (map != null && !map.isEmpty()) ? Boolean.TRUE : Boolean.FALSE;
    }

    public static <T> boolean isSetContainingElements(Set<T> set) {
        return (set != null && !set.isEmpty()) ? Boolean.TRUE : Boolean.FALSE;
    }

    public static <T> Boolean isCollectionContainingElements(Collection<?> collection) {
        return (collection != null && !collection.isEmpty()) ? Boolean.TRUE : Boolean.FALSE;
    }

    public static <T> Boolean isCollectionEmpty(Collection<?> collection) {
        return (collection == null || collection.isEmpty()) ? Boolean.TRUE : Boolean.FALSE;
    }
}
