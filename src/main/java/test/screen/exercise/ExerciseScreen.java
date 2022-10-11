package test.screen.exercise;

import io.jmix.core.DataManager;
import io.jmix.core.Metadata;
import io.jmix.ui.component.Button;
import io.jmix.ui.component.EntityPicker;
import io.jmix.ui.component.FileUploadField;
import io.jmix.ui.component.HasValue;
import io.jmix.ui.component.SingleFileUploadField;
import io.jmix.ui.component.TextArea;
import io.jmix.ui.download.Downloader;
import io.jmix.ui.model.InstanceContainer;
import io.jmix.ui.screen.Screen;
import io.jmix.ui.screen.Subscribe;
import io.jmix.ui.screen.UiController;
import io.jmix.ui.screen.UiDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import test.ExerciseCalculator;
import test.ExerciseType;
import test.MagicSquareExerciseCalculator;
import test.SubstringExerciseCalculator;
import test.entity.Exercise;

@UiController("ExerciseScreen")
@UiDescriptor("exercise-screen.xml")
public class ExerciseScreen extends Screen {
    @Autowired private Metadata metadata;
    @Autowired private DataManager dataManager;
    @Autowired private InstanceContainer<Exercise> exerciseDc;
    @Autowired private TextArea<String> outputArea;
    @Autowired private EntityPicker<Exercise> exercisePicker;
    @Autowired private FileUploadField fileInput;
    @Autowired private Downloader downloader;
    private static final String example =
        "\t" + ExerciseType.Substring.name() + "\n" + SubstringExerciseCalculator.example
        + "\nor\n\t" + ExerciseType.MagicSquare.name() + "\n" + MagicSquareExerciseCalculator.example;

    @Subscribe
    public void onInit(InitEvent event) {
        Exercise exercise = metadata.create(Exercise.class);
        exerciseDc.setItem(exercise);
    }

    @Subscribe("saveButton")
    public void onSaveButtonClick(Button.ClickEvent event) {
        Exercise exercise = exerciseDc.getItem();
        if (exercise.getType() == null) {
            outputArea.setValue("Error: type not set");
            return;
        }
        String input = nonNullStr(exercise.getInput());
        String validationError = exercise.getCalculator().validateInput(input);
        if (validationError != null) {
            outputArea.setValue("Error: " + validationError);
            return;
        }
        dataManager.save(exercise);
    }

    @Subscribe("calculateButton")
    public void onCalculateButtonClick(Button.ClickEvent event) {
        calculate();
    }

    public void calculate() {
        Exercise exercise = exerciseDc.getItem();
        String input = nonNullStr(exercise.getInput());
        ExerciseCalculator calculator = exercise.getCalculator();
        String result;
        try {
            result = calculator.calculate(input);
        } catch (IllegalArgumentException e) {
            outputArea.setValue("Error: " + e.getMessage());
            return;
        }
        outputArea.setValue(result);
    }

    @Subscribe("exercisePicker")
    public void onExercisePickerValueChange(HasValue.ValueChangeEvent<Exercise> event) {
        Exercise selectedExercise = exercisePicker.getValue();
        if (selectedExercise == null) {
            return;
        }
        exerciseDc.setItem(selectedExercise);
        exercisePicker.setValue(null);
        calculate();
    }

    @Subscribe("clearFormButton")
    public void onClearFormButtonClick(Button.ClickEvent event) {
        Exercise exercise = metadata.create(Exercise.class);
        exerciseDc.setItem(exercise);
    }

    @Subscribe("fileInput")
    public void onFileInputFileUploadSucceed(SingleFileUploadField.FileUploadSucceedEvent event) {
        byte[] file = fileInput.getValue();
        if (file == null) {
            return;
        }
        String text = new String(file);
        int firstLineEnd = text.indexOf("\n");
        if (firstLineEnd == -1) {
            outputArea.setValue("Invalid format.\nExample:\n" + example);
            return;
        }

        String typeStr = text.substring(0, firstLineEnd);
        ExerciseType type;
        if (typeStr.equals(ExerciseType.Substring.name())) {
            type = ExerciseType.Substring;
        } else if (typeStr.equals(ExerciseType.MagicSquare.name())) {
            type = ExerciseType.MagicSquare;
        } else {
            outputArea.setValue("Unknown exercise type: " + typeStr);
            return;
        }

        String input = text.substring(firstLineEnd + 1);
        ExerciseCalculator calculator = type == ExerciseType.Substring ? ExerciseCalculator.SUBSTRING_CALCULATOR
            : ExerciseCalculator.MAGIC_SQUARE_CALCULATOR;
        String validationError = calculator.validateInput(input);
        if (validationError != null) {
            outputArea.setValue("Input error for type '" + typeStr + "': " + validationError);
            return;
        }

        Exercise exercise = metadata.create(Exercise.class);
        exercise.setType(type);
        exercise.setInput(input);
        exerciseDc.setItem(exercise);
        calculate();
    }

    @Subscribe("exportButton")
    public void onExportButtonClick(Button.ClickEvent event) {
        Exercise exercise = exerciseDc.getItem();
        if (exercise.getType() == null) {
            outputArea.setValue("Error: type not set");
            return;
        }
        String input = nonNullStr(exercise.getInput());
        String text = exercise.getType().name() + "\n" + input;
        downloader.download(text.getBytes(), "exercise.txt");
    }

    private String nonNullStr(String s) {
        return (s != null) ? s : "";
    }
}