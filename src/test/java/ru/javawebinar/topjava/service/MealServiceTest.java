package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-app-repository.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        assertEqual(service.get(USER_FIRST_BREAKFAST_ID, USER_ID), userFirstBreakfast);
    }

    @Test
    public void getWrongOwner() {
        assertThrows(NotFoundException.class, () -> service.get(USER_FIRST_BREAKFAST_ID, ADMIN_ID));
    }

    @Test
    public void delete() {
        service.delete(USER_FIRST_BREAKFAST_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(USER_FIRST_BREAKFAST_ID, USER_ID));
    }

    @Test
    public void deleteWrongOwner() {
        assertThrows(NotFoundException.class, () -> service.delete(USER_FIRST_BREAKFAST_ID, ADMIN_ID));
    }

    @Test
    public void getBetweenInclusiveOneNull() {
        assertEqual(service.getBetweenInclusive(null, LocalDate.of(2020, 1, 30), USER_ID),
                Arrays.asList(userFirstDinner, userFirstLunch, userFirstBreakfast));
    }

    @Test
    public void getBetweenInclusiveTwoNull() {
        assertEqual(service.getBetweenInclusive(null, null, ADMIN_ID),
                Arrays.asList(adminDinner, adminLunch, adminBreakfast));
    }

    @Test
    public void getBetweenInclusive() {
        assertEqual(service.getBetweenInclusive(LocalDate.of(2020, 1, 31), LocalDate.of(2020, 2, 5), USER_ID),
                Arrays.asList(userSecondDinner, userSecondLunch, userSecondBreakfast, userMidnightMeal));
    }

    @Test
    public void getAll() {
        List<Meal> list = service.getAll(USER_ID);
        assertEqual(list, Arrays.asList(userThirdBreakfast, userSecondDinner, userSecondLunch,
                userSecondBreakfast, userMidnightMeal, userFirstDinner, userFirstLunch, userFirstBreakfast));
    }

    @Test
    public void update() {
        service.update(getUpdated(), USER_ID);
        assertEqual(service.get(USER_FIRST_BREAKFAST_ID, USER_ID), getUpdated());
    }

    @Test
    public void updateWrongOwner() {
        assertThrows(NotFoundException.class, () -> service.update(getUpdated(), ADMIN_ID));
    }

    @Test
    public void create() {
        Meal meal = service.create(getNew(), USER_ID);
        assertThat(service.get(meal.getId(), USER_ID)).usingRecursiveComparison().ignoringFields("id").isEqualTo(getNew());
        assertThat(meal).usingRecursiveComparison().ignoringFields("id").isEqualTo(getNew());
    }

    @Test
    public void duplicateDateCreate() {
        assertThrows(DataAccessException.class, () ->
                service.create(new Meal(userFirstBreakfast.getDateTime(), "Пробный завтрак", 10), USER_ID));
    }

    @Test
    public void deleteNotExist() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_EXIST_ID, USER_ID));
    }

    @Test
    public void updateNotExist() {
        assertThrows(NotFoundException.class, () -> service.update(notExist, USER_ID));
    }

    @Test
    public void getNotExist() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_EXIST_ID, USER_ID));
    }
}