package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int USER_FIRST_BREAKFAST_ID = START_SEQ + 3;
    public static final int USER_FIRST_LUNCH_ID = START_SEQ + 4;
    public static final int USER_FIRST_DINNER_ID = START_SEQ + 5;
    public static final int USER_MIDNIGHT_MEAL_ID = START_SEQ + 6;
    public static final int USER_SECOND_BREAKFAST_ID = START_SEQ + 7;
    public static final int USER_SECOND_LUNCH_ID = START_SEQ + 8;
    public static final int USER_SECOND_DINNER_ID = START_SEQ + 9;
    public static final int USER_THIRD_BREAKFAST_ID = START_SEQ + 10;
    public static final int ADMIN_BREAKFAST_ID = START_SEQ + 11;
    public static final int ADMIN_LUNCH_ID = START_SEQ + 12;
    public static final int ADMIN_DINNER_ID = START_SEQ + 13;

    public static final Meal userFirstBreakfast = new Meal(USER_FIRST_BREAKFAST_ID, LocalDateTime.of(2020, 1, 30, 10, 0), "Завтрак", 500);
    public static final Meal userFirstLunch = new Meal(USER_FIRST_LUNCH_ID, LocalDateTime.of(2020, 1, 30, 13, 0), "Обед", 1000);
    public static final Meal userFirstDinner = new Meal(USER_FIRST_DINNER_ID, LocalDateTime.of(2020, 1, 30, 20, 0), "Ужин", 500);
    public static final Meal userMidnightMeal = new Meal(USER_MIDNIGHT_MEAL_ID, LocalDateTime.of(2020, 1, 31, 0, 0), "Еда на граничное время", 100);
    public static final Meal userSecondBreakfast = new Meal(USER_SECOND_BREAKFAST_ID, LocalDateTime.of(2020, 1, 31, 10, 0), "Завтрак", 1000);
    public static final Meal userSecondLunch = new Meal(USER_SECOND_LUNCH_ID, LocalDateTime.of(2020, 1, 31, 13, 0), "Обед", 500);
    public static final Meal userSecondDinner = new Meal(USER_SECOND_DINNER_ID, LocalDateTime.of(2020, 1, 31, 20, 0), "Ужин", 410);
    public static final Meal userThirdBreakfast = new Meal(USER_THIRD_BREAKFAST_ID, LocalDateTime.of(2020, 2, 20, 9, 0), "Завтрак", 415);
    public static final Meal adminBreakfast = new Meal(ADMIN_BREAKFAST_ID, LocalDateTime.of(2021, 1, 31, 10, 0), "Завтрак админа", 500);
    public static final Meal adminLunch = new Meal(ADMIN_LUNCH_ID, LocalDateTime.of(2021, 1, 31, 13, 0), "Обед админа", 700);
    public static final Meal adminDinner = new Meal(ADMIN_DINNER_ID, LocalDateTime.of(2021, 1, 31, 20, 0), "Ужин админа", 600);

    public static final int NOT_EXIST_ID = 10;
    public static final Meal notExist = new Meal(NOT_EXIST_ID, LocalDateTime.of(2021, 1, 31, 20, 0), "Еда мечты", 1);

    public static Meal getNew() {
        return new Meal(LocalDateTime.of(2022,1,30,11,0), "Второй завтрак", 500);
    }

    public static Meal getUpdated() {
        Meal meal = new Meal(userFirstBreakfast);
        meal.setCalories(123);
        meal.setDateTime(LocalDateTime.of(2020,3,30,10,0));
        meal.setDescription("Новый завтрак");
        return meal;
    }

    public static void assertEqual(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertEqual(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }
}
