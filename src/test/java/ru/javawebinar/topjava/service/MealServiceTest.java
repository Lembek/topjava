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
        assertEqual(service.get(USER_FIRST_BREAKFAST_ID, USER_ID), UserFirstBreakfast);
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
                Arrays.asList(UserFirstDinner, UserFirstLunch, UserFirstBreakfast));
    }

    @Test
    public void getBetweenInclusiveTwoNull() {
        assertEqual(service.getBetweenInclusive(null, null, ADMIN_ID),
                Arrays.asList(AdminDinner, AdminLunch, AdminBreakfast));
    }

    @Test
    public void getBetweenInclusive() {
        assertEqual(service.getBetweenInclusive(LocalDate.of(2020, 1, 31), LocalDate.of(2020, 2, 5), USER_ID),
                Arrays.asList(UserSecondDinner, UserSecondLunch, UserSecondBreakfast, UserMidnightMeal));
    }

    @Test
    public void getAll() {
        List<Meal> list = service.getAll(USER_ID);
        assertEqual(list, Arrays.asList(UserThirdBreakfast, UserSecondDinner, UserSecondLunch,
                UserSecondBreakfast, UserMidnightMeal, UserFirstDinner, UserFirstLunch, UserFirstBreakfast));
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
        int id = service.create(getNew(), USER_ID).getId();
        assertThat(getNew()).usingRecursiveComparison().ignoringFields("id").isEqualTo(service.get(id, USER_ID));
    }

    @Test
    public void duplicateDateCreate() {
        assertThrows(DataAccessException.class, () ->
                service.create(new Meal(UserFirstBreakfast.getDateTime(), "Пробный завтрак", 10), USER_ID));
    }

    @Test
    public void deleteNotExist() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_EXIST_ID, USER_ID));
    }

    @Test
    public void updateNotExist() {
        assertThrows(NotFoundException.class, () -> service.update(NotExist, USER_ID));
    }

    @Test
    public void getNotExist() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_EXIST_ID, USER_ID));
    }
}