package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger();
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);

    {
        MealsUtil.meals.forEach(meal -> this.save(SecurityUtil.authUserId(), meal));
    }

    @Override
    public Meal save(int userId, Meal meal) {
        log.info("save {}", meal);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            repository.put(meal.getId(), meal);
            return meal;
        }
        return checkOwnership(userId, meal.getId()) ?
                repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal) : null;
    }

    @Override
    public boolean delete(int userId, int mealId) {
        log.info("delete {}", mealId);
        if (checkOwnership(userId, mealId)) return repository.remove(mealId) != null;
        return false;
    }

    @Override
    public Meal get(int userId, int mealId) {
        log.info("get {}", mealId);
        return checkOwnership(userId, mealId) ? repository.get(mealId) : null;
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        log.info("getAll");
        return getAllWithFilters(userId, meal -> true, meal -> true);
    }

    @Override
    public Collection<Meal> getAllWithDateTimeFilter(int userId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        log.info("getAll with date filter");
        return getAllWithFilters(userId,
                meal -> DateTimeUtil.isBetweenHalfClose(meal.getDate(), startDateTime.toLocalDate(), endDateTime.toLocalDate()),
                meal -> DateTimeUtil.isBetweenHalfOpen(meal.getTime(), startDateTime.toLocalTime(), endDateTime.toLocalTime()));
    }

    private List<Meal> getAllWithFilters(int userId, Predicate<Meal> filter1, Predicate<Meal> filter2) {
        return repository.values().stream()
                .filter(meal -> checkOwnership(userId, meal.getId()) && filter1.test(meal) && filter2.test(meal))
                .sorted(Comparator.comparing(Meal::getTime).reversed())
                .collect(Collectors.toList());
    }

    private boolean checkOwnership(int userId, int mealId) {
        Meal meal = repository.get(mealId);
        return meal != null && meal.getUserId() == userId;
    }
}

