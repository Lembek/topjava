package ru.javawebinar.topjava.service.jdbc;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.service.UserBaseServiceTest;

@ActiveProfiles(profiles = Profiles.JDBC)
public class JdbcUserServiceTest extends UserBaseServiceTest {
}
