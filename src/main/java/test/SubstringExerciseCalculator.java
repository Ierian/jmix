package test;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class SubstringExerciseCalculator implements ExerciseCalculator {
    public static final String example = "\tlive, strong, arp\n\tsharp, lively, alive, harp";

    public @Nullable String validateInput(String input) {
        int lineEnd = input.indexOf("\n");
        if (lineEnd == -1) {
            return "Two lines expected.\nExample:\n" + getExample();
        }
        return null;
    }

    /**
     * @throws IllegalArgumentException
     */
    public String calculate(String input) {
        int lineEnd = input.indexOf("\n");
        if (lineEnd == -1) {
            throw new IllegalArgumentException("Two lines expected\nExample:\n" + getExample());
        }

        String[] firstLine = input.substring(0, lineEnd).split(",\\s*");
        String[] secondLine = input.substring(lineEnd + 1).split(",\\s*");
        Set<String> substrings = new HashSet<>(firstLine.length);
        for (String a : firstLine) {
            for (String b : secondLine) {
                if (b.contains(a)) {
                    substrings.add(a);
                }
            }
        }
        return substrings.stream().sorted().collect(Collectors.joining(", "));
    }

    @Override
    public String getExample() {
        return example;
    }
}
