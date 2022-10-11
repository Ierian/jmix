package test;

import io.jmix.core.metamodel.datatype.impl.EnumClass;

public enum ExerciseType implements EnumClass<String> {
    Substring,
    MagicSquare;

    @Override
    public String getId() {
        return name();
    }
}
