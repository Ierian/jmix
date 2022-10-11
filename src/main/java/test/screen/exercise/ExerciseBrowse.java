package test.screen.exercise;

import io.jmix.ui.screen.*;
import test.entity.Exercise;

@UiController("Exercise.browse")
@UiDescriptor("exercise-browse.xml")
@LookupComponent("exercisesTable")
public class ExerciseBrowse extends StandardLookup<Exercise> {
}