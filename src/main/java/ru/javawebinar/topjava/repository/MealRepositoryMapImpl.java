package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealRepositoryMapImpl implements MealRepository {
    private static final AtomicInteger currentId = new AtomicInteger(0);
    private final Map<Integer, Meal> meals = new ConcurrentHashMap<>();

    private static final class InstanceHolder {
        static final MealRepositoryMapImpl instance = new MealRepositoryMapImpl();
    }

    public static MealRepositoryMapImpl getInstance() {
        return InstanceHolder.instance;
    }

    private MealRepositoryMapImpl() {
        MealsUtil.meals.forEach(this::save);
    }

    @Override
    public Meal save(Meal meal) {
        meal.setId(currentId.getAndIncrement());
        return meals.put(meal.getId(), meal);
    }

    @Override
    public Meal update(int id, LocalDateTime dateTime, String description, int calories) {
        Meal mealForUpdate = meals.get(id);
        if (dateTime != null)
            mealForUpdate.setDateTime(dateTime);
        if (description != null && !description.isEmpty())
            mealForUpdate.setDescription(description);
        if (calories != 0)
            mealForUpdate.setCalories(calories);
        return meals.put(id, mealForUpdate);
    }

    @Override
    public Meal get(int id) {
        return meals.get(id);
    }

    @Override
    public void delete(int id) {
        meals.remove(id);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(this.meals.values());
    }
}
