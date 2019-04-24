package fi.frt.domain.textinput;

import java.util.Map;
import java.util.Set;

public interface TextInput {
    Set<String> validate();
    boolean isValid();
    Set<String> getInvalidFields();
    Map<String, Object> getAttrMap();
    void setFromMap(Map<String, Object> map);
}
