package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;

import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int USER_FIRST_BREAKFAST_ID = START_SEQ + 3;
    public static final int USER_FIRST_LUNCH_ID = START_SEQ + 4;
    public static final int USER_FIRST_DINNER_ID = START_SEQ + 5;
    public static final int USER_MIDNIGHT_MEAL_ID = START_SEQ + 6;
    public static final int USER_SECOND_BREAKFAST_ID = START_SEQ + 7;
    public static final int USER_SECOND_LUNCH_ID = START_SEQ + 8;
    public static final int USER_SECOND_DINNER_ID = START_SEQ + 9;

    public static final Meal USER_FIRST_BREAKFAST = new Meal(USER_FIRST_BREAKFAST_ID, LocalDateTime.parse("2020-01-30T10:00"), "Завтрак", 500);
    public static final Meal USER_FIRST_LUNCH = new Meal(USER_FIRST_LUNCH_ID, LocalDateTime.parse("2020-01-30T13:00"), "Обед", 1000);
    public static final Meal USER_FIRST_DINNER = new Meal(USER_FIRST_DINNER_ID, LocalDateTime.parse("2020-01-30T20:00"), "Ужин", 500);
    public static final Meal USER_MIDNIGHT_MEAL = new Meal(USER_MIDNIGHT_MEAL_ID, LocalDateTime.parse("2020-01-31T00:00"), "Еда на граничное время", 100);
    public static final Meal USER_SECOND_BREAKFAST = new Meal(USER_SECOND_BREAKFAST_ID, LocalDateTime.parse("2020-01-31T10:00"), "Завтрак", 1000);
    public static final Meal USER_SECOND_LUNCH = new Meal(USER_SECOND_LUNCH_ID, LocalDateTime.parse("2020-01-31T13:00"), "Обед", 500);
    public static final Meal USER_SECOND_DINNER = new Meal(USER_SECOND_DINNER_ID, LocalDateTime.parse("2020-01-31T20:00"), "Ужин", 410);

    public static Meal getNew() {
        return new Meal(LocalDateTime.parse("2022-01-30T11:00"), "Второй завтрак", 500);
    }

    public static Meal getUpdated() {
        Meal meal = new Meal(USER_FIRST_BREAKFAST);
        meal.setCalories(123);
        meal.setDateTime(LocalDateTime.parse("2020-03-30T10:00"));
        meal.setDescription("Новый завтрак");
        return meal;
    }
}
