<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ru">
<head>
    <title>Title</title>
    <style>
        <%@include file="/WEB-INF/css/style.css"%>
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<table>
    <tr>
        <th>Дата/Время</th>
        <th>Описание</th>
        <th>Калории</th>
    </tr>
    <c:forEach var="mealTo" items="${mealsTo}">
        <tr class="${mealTo.excess ? "withExcess" : "withoutExcess"}">
            <td>${mealTo.dateTime.toLocalDate()} ${mealTo.dateTime.toLocalTime()}</td>
            <td>${mealTo.description}</td>
            <td>${mealTo.calories}</td>
            <td><a href="meals?action=edit&mealId=${mealTo.id}">Изменить</a></td>
            <td><a href="meals?action=delete&mealId=${mealTo.id}">Удалить</a></td>
        </tr>
    </c:forEach>
</table>
<form action="meals" method="post">
    <h3>Create new Meal</h3>
    <label>Description
        <input type="text" name="description">
    </label><br>
    <label>Calories
        <input type="number" name="calories">
    </label><br>
    <label>Date/Time
        <input type="datetime-local" name="dateTime">
    </label><br>
    <button type="submit">Save</button>
</form>
</body>
</html>
