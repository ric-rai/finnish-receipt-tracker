package fi.frt.domain.input;

import java.util.Set;

public interface InputData {
    Set<String> validate();
    boolean isValid();
    Set<String> getInvalidFields();
}
