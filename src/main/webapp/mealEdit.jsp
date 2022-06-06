<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="ru">
<head>
    <title>Title</title>
</head>
<body>
<form action="meals" method="post">
    <h3>Edit meal</h3>
    <input type="hidden" name="mealId" value="${meal.getId()}">
    <label>Description
        <input type="text" name="description" value="${meal.getDescription()}">
    </label><br>
    <label>Calories
        <input type="text" name="calories" value="${meal.getCalories()}">
    </label><br>
    <label>Date/Time
        <input type="datetime-local" name="dateTime" value="${meal.getDateTime()}">
    </label><br>
    <button type="submit">Update</button>
</form>
</body>
</html>
