package fi.frt.domain.input;

import java.util.Map;
import java.util.Set;

public interface InputData {
    Set<String> validate();
    boolean isValid();
    Set<String> getInvalidFields();
    Map<String, Object> getAttrMap();
    void setFromMap(Map<String, Object> map);
}
