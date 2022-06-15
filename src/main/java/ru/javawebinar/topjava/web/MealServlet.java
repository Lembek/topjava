package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);
    private ConfigurableApplicationContext appCont;
    private MealRestController mealController;

    @Override
    public void init() {
        appCont = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        mealController = appCont.getBean(MealRestController.class);
    }

    @Override
    public void destroy() {
        appCont.close();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                int mealId = getId(request);
                log.info("Delete mealId={}", mealId);
                mealController.delete(mealId);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                        mealController.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");
                List<MealTo> mealsTo;
                if (request.getParameter("filter") == null) {
                    mealsTo = mealController.getAll();
                } else {
                    String startDateString = request.getParameter("startDate");
                    String startTimeString = request.getParameter("startTime");
                    String endDateString = request.getParameter("endDate");
                    String endTimeString = request.getParameter("endTime");

                    LocalDate startDate = startDateString.isEmpty() ? null : LocalDate.parse(startDateString);
                    LocalDate endDate = endDateString.isEmpty() ? null : LocalDate.parse(endDateString);
                    LocalTime startTime = startTimeString.isEmpty() ? null : LocalTime.parse(startTimeString);
                    LocalTime endTime = endTimeString.isEmpty() ? null : LocalTime.parse(endTimeString);

                    mealsTo = mealController.getAllWithDateFilter(startDate, endDate, startTime, endTime);

                    request.setAttribute("startTime", startTime);
                    request.setAttribute("endTime", endTime);
                    request.setAttribute("startDate", startDate);
                    request.setAttribute("endDate", endDate);
                }
                request.setAttribute("meals", mealsTo);
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");

        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        if (meal.isNew()) {
            mealController.create(meal);
            log.info("Create {}", meal);
        } else {
            mealController.update(meal, meal.getId());
            log.info("Update {}", meal);
        }
        response.sendRedirect("meals");
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
