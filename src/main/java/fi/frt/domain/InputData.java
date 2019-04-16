package fi.frt.domain;

import java.util.Set;

public interface InputData {
    public boolean isValid();
    public Set<String> getInvalidFields();
}
