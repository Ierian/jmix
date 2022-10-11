package test.entity;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.JmixEntity;
import test.ExerciseCalculator;
import test.ExerciseType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@JmixEntity
@Table(name = "EXERCISE")
@Entity
public class Exercise {
    @JmixGeneratedValue @Column(name = "ID", nullable = false) @Id private UUID id;

    @NotNull @Column(name = "TYPE_", nullable = false) private String type;

    @NotNull @Column(name = "INPUT", nullable = false)
    protected String input;

    public ExerciseType getType() {
        return type == null ? null : ExerciseType.valueOf(type);
    }

    public void setType(ExerciseType type) {
        this.type = type == null ? null : type.getId();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public ExerciseCalculator getCalculator() {
        return ExerciseType.Substring.equals(getType()) ? ExerciseCalculator.SUBSTRING_CALCULATOR
            : ExerciseCalculator.MAGIC_SQUARE_CALCULATOR;
    }
}