package test;

import javax.annotation.Nullable;

public interface ExerciseCalculator {
    /**
     * @throws IllegalArgumentException
     */
    String calculate(String input);

    @Nullable String validateInput(String input);

    String getExample();

    SubstringExerciseCalculator SUBSTRING_CALCULATOR = new SubstringExerciseCalculator();
    MagicSquareExerciseCalculator MAGIC_SQUARE_CALCULATOR = new MagicSquareExerciseCalculator();
}
