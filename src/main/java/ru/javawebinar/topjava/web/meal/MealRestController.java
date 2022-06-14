package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.util.List;

import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {

    private final MealService service;

    public MealRestController(MealService service) {
        this.service = service;
    }

    public List<MealTo> getAll() {
        return MealsUtil.getTos(service.getAll(authUserId()), authUserCaloriesPerDay());
    }

    public List<MealTo> getAllWithDateFilter(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return MealsUtil.getTos(service.getAllWithDateFilter(authUserId(), startDateTime, endDateTime),
                authUserCaloriesPerDay());
    }

    public Meal get(int mealId) {
        return service.get(authUserId(), mealId);
    }

    public void delete(int mealId) {
        service.delete(authUserId(), mealId);
    }

    public Meal create(Meal meal) {
        return service.create(authUserId(), meal);
    }

    public Meal update(Meal meal, int mealId) {
        return service.update(authUserId(), meal, mealId);
    }
}