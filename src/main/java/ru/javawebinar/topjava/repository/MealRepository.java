package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public interface MealRepository {
    Meal save(Meal meal);

    Meal update(int id, LocalDateTime dateTime, String description, int calories);

    Meal get(int id);

    void delete(int id);

    List<Meal> getAll();
}
