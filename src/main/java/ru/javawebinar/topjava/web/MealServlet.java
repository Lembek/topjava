package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.repository.InMemoryMealRepository;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.JspUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    private MealRepository mealRepository;

    @Override
    public void init() {
        mealRepository = new InMemoryMealRepository();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = (request.getParameter("action") == null ? "" : request.getParameter("action"));
        Integer mealId = JspUtil.getIdFromJsp(request, "mealId");
        switch (action) {
            case "delete":
                mealRepository.delete(mealId);
                log.debug("redirect to meals after delete");
                response.sendRedirect("meals");
                break;
            case "edit":
                request.setAttribute("meal", mealRepository.get(mealId));
                log.debug("redirect to mealEdit");
                request.getRequestDispatcher("mealEdit.jsp").forward(request, response);
                break;
            default:
                List<MealTo> mealsTo = MealsUtil.filteredByStreams(mealRepository.getAll(),
                        LocalTime.MIN, LocalTime.MAX, MealsUtil.CALORIES_PER_DAY);
                request.setAttribute("mealsTo", mealsTo);
                log.debug("meal table request");
                request.getRequestDispatcher("meals.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"));
        Meal meal = new Meal(dateTime, description, calories);
        Integer mealId = JspUtil.getIdFromJsp(request, "mealId");
        if (mealId != null) {
            meal.setId(mealId);
        }
        mealRepository.save(meal);
        log.debug("redirect to meals after save");
        response.sendRedirect("meals");
    }
}
