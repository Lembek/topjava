package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreamsOptional(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesByDays = new HashMap<>();
        meals.forEach(userMeal -> caloriesByDays.merge(userMeal.getDateTime().toLocalDate(), userMeal.getCalories(), Integer::sum));

        List<UserMealWithExcess> result = new ArrayList<>();
        meals.forEach(userMeal -> {
            if (TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime))
                result.add(new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(),
                        caloriesByDays.get(userMeal.getDateTime().toLocalDate()) > caloriesPerDay));
        });
        return result;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesByDays = meals.stream()
                .collect(Collectors.toMap(userMeal -> userMeal.getDateTime().toLocalDate(), UserMeal::getCalories, Integer::sum));

        return meals.stream()
                .filter(userMeal -> TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime))
                .map(userMeal -> new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(),
                        userMeal.getCalories(), caloriesByDays.get(userMeal.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    public static List<UserMealWithExcess> filteredByStreamsOptional(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Collector<UserMeal, Map.Entry<Map<LocalDate, Integer>, List<UserMealWithExcess>>, List<UserMealWithExcess>> myCollector =
                new Collector<UserMeal, Map.Entry<Map<LocalDate, Integer>, List<UserMealWithExcess>>, List<UserMealWithExcess>>() {
                    @Override
                    public Supplier<Map.Entry<Map<LocalDate, Integer>, List<UserMealWithExcess>>> supplier() {
                        return () -> new AbstractMap.SimpleImmutableEntry<>(new HashMap<>(), new ArrayList<>());
                    }

                    @Override
                    public BiConsumer<Map.Entry<Map<LocalDate, Integer>, List<UserMealWithExcess>>, UserMeal> accumulator() {
                        return (entry, userMeal) -> {
                            entry.getKey().merge(userMeal.getDateTime().toLocalDate(), userMeal.getCalories(), Integer::sum);
                            if (TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime))
                                entry.getValue().add(new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories()));
                        };
                    }

                    @Override
                    public BinaryOperator<Map.Entry<Map<LocalDate, Integer>, List<UserMealWithExcess>>> combiner() {
                        return (a, b) -> {
                            a.getKey().putAll(b.getKey());
                            a.getValue().addAll(b.getValue());
                            return a;
                        };
                    }

                    @Override
                    public Function<Map.Entry<Map<LocalDate, Integer>, List<UserMealWithExcess>>, List<UserMealWithExcess>> finisher() {
                        return (entry) -> entry.getValue().stream()
                                .peek(meal -> meal.setExcess(entry.getKey().get(meal.getDateTime().toLocalDate()) > caloriesPerDay))
                                .collect(Collectors.toList());
                    }

                    @Override
                    public Set<Characteristics> characteristics() {
                        return new HashSet<>();
                    }
                };

        return meals.stream().collect(myCollector);
    }
}
