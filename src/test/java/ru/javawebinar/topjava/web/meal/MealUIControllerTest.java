package ru.javawebinar.topjava.web.meal;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.getWithoutCalories;
import static ru.javawebinar.topjava.TestUtil.userAuth;
import static ru.javawebinar.topjava.UserTestData.user;
import static ru.javawebinar.topjava.web.meal.MealUIController.MEALS_UI_URL;

public class MealUIControllerTest extends AbstractControllerTest {
    private static final String URL = MEALS_UI_URL + "/";

    @Test
    void createWithZeroCalories() throws Exception {
        perform(post(URL)
                .with(userAuth(user))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(getWithoutCalories())))
                .andExpect(status().is4xxClientError());
    }
}
