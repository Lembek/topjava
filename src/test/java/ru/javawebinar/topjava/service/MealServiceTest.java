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
        assertThat(service.get(USER_FIRST_BREAKFAST_ID, USER_ID)).usingRecursiveComparison().isEqualTo(USER_FIRST_BREAKFAST);
    }

    @Test(expected = NotFoundException.class)
    public void getWrongOwner() {
        service.get(USER_FIRST_BREAKFAST_ID, ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFound() {
        service.delete(USER_FIRST_BREAKFAST_ID, USER_ID);
        service.get(USER_FIRST_BREAKFAST_ID, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteWrongOwner() {
        service.delete(USER_FIRST_BREAKFAST_ID, ADMIN_ID);
    }

    @Test
    public void getBetweenInclusive() {
        assertThat(service.getBetweenInclusive(null, LocalDate.parse("2020-01-30"), USER_ID))
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(Arrays.asList(USER_FIRST_DINNER, USER_FIRST_LUNCH, USER_FIRST_BREAKFAST));
    }

    @Test
    public void getAll() {
        List<Meal> list = service.getAll(USER_ID);
        assertThat(list).usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(Arrays.asList(USER_SECOND_DINNER, USER_SECOND_LUNCH, USER_SECOND_BREAKFAST, USER_MIDNIGHT_MEAL,
                        USER_FIRST_DINNER, USER_FIRST_LUNCH, USER_FIRST_BREAKFAST));
    }

    @Test
    public void update() {
        service.update(getUpdated(), USER_ID);
        assertThat(service.get(USER_FIRST_BREAKFAST_ID, USER_ID)).usingRecursiveComparison().isEqualTo(getUpdated());

    }

    @Test(expected = NotFoundException.class)
    public void updateWrongOwner() {
        service.update(getUpdated(), ADMIN_ID);
    }

    @Test
    public void create() {
        assertThat(getNew()).usingRecursiveComparison().ignoringFields("id").isEqualTo(service.create(getNew(), USER_ID));
    }

    @Test(expected = DataAccessException.class)
    public void duplicateDateCreate() {
        service.create(new Meal(USER_FIRST_BREAKFAST.getDateTime(), "Пробный завтрак", 10), USER_ID);
    }
}