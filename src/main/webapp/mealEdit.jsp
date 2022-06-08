<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="ru">
<head>
    <title>Title</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<form action="meals" method="post">
    <h3>Edit meal</h3>
    <input type="hidden" name="mealId" value="${meal.id}">
    <label>Description
        <input type="text" name="description" value="${meal.description}">
    </label><br>
    <label>Calories
        <input type="number" name="calories" value="${meal.calories}">
    </label><br>
    <label>Date/Time
        <input type="datetime-local" name="dateTime" value="${meal.dateTime}">
    </label><br>
    <button type="submit">Update</button>
    <br>
    <a href="meals">Come back</a>
</form>
</body>
</html>
