package ru.javawebinar.topjava.service.jdbc;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.service.MealBaseServiceTest;

@ActiveProfiles(profiles = Profiles.JDBC)
public class JdbcMealServiceTest extends MealBaseServiceTest {
}
