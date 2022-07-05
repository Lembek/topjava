package ru.javawebinar.topjava.service.user;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.ActiveDbProfileResolver;

@ActiveProfiles(resolver = ActiveDbProfileResolver.class)
public class JpaUserServiceTest extends UserBaseServiceTest {
}
