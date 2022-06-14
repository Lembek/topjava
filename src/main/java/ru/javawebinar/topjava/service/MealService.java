package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.Collection;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {

    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public void delete(int userId, int mealId) {
        checkNotFoundWithId(repository.delete(userId, mealId), mealId);
    }

    public Meal save(int userId, Meal meal) {
        return checkNotFoundWithId(repository.save(userId, meal), meal.getId());
    }

    public Collection<Meal> getAll(int userId) {
        return repository.getAll(userId);
    }

    public Collection<Meal> getAllWithDateFilter(int userId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return repository.getAllWithDateTimeFilter(userId, startDateTime, endDateTime);
    }

    public Meal get(int userId, int mealId) {
        return checkNotFoundWithId(repository.get(userId, mealId), mealId);
    }

    public Meal update(int userId, Meal meal, int mealId) {
        return checkNotFoundWithId(repository.save(userId, meal), mealId);
    }

    public Meal create(int userId, Meal meal) {
        return repository.save(userId, meal);
    }
}