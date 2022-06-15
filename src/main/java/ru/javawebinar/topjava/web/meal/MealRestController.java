package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {
    private static final Logger log = LoggerFactory.getLogger(MealRestController.class);

    private final MealService service;

    public MealRestController(MealService service) {
        this.service = service;
    }

    public List<MealTo> getAll() {
        log.info("get all in controller");
        return MealsUtil.getTos(service.getAll(authUserId()), authUserCaloriesPerDay());
    }

    public List<MealTo> getAllWithDateFilter(LocalDate startDate, LocalDate endDate,
                                             LocalTime startTime, LocalTime endTime) {
        log.info("get all with date filter in controller");
        if (startDate == null) {
            startDate = LocalDate.MIN;
        }
        if (endDate == null) {
            endDate = LocalDate.MAX;
        }
        if (startTime == null) {
            startTime = LocalTime.MIN;
        }
        if (endTime == null) {
            endTime = LocalTime.MAX;
        }
        return MealsUtil.getFilteredTos(
                service.getAllWithDateFilter(authUserId(), startDate, endDate),
                authUserCaloriesPerDay(), startTime, endTime);
    }

    public Meal get(int mealId) {
        log.info("get {} in controller", mealId);
        return service.get(authUserId(), mealId);
    }

    public void delete(int mealId) {
        log.info("delete {} in controller", mealId);
        service.delete(authUserId(), mealId);
    }

    public Meal create(Meal meal) {
        log.info("create {} in controller", meal);
        checkNew(meal);
        return service.create(authUserId(), meal);
    }

    public Meal update(Meal meal, int mealId) {
        log.info("update {} with id={} in controller", meal, mealId);
        assureIdConsistent(meal, mealId);
        return service.update(authUserId(), meal);
    }
}