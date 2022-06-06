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
        <tr class="${mealTo.isExcess() ? "withExcess" : "withoutExcess"}">
            <td>${mealTo.getTime()}</td>
            <td>${mealTo.getDescription()}</td>
            <td>${mealTo.getCalories()}</td>
            <td><a href="meals?action=edit&id=${mealTo.getId()}">Изменить</a></td>
            <td><a href="meals?action=delete&id=${mealTo.getId()}">Удалить</a></td>
        </tr>
    </c:forEach>
</table>
<form action="meals" method="post">
    <h3>Create new Meal</h3>
    <label>Description
        <input type="text" name="description">
    </label><br>
    <label>Calories
        <input type="text" name="calories">
    </label><br>
    <label>Date/Time
        <input type="datetime-local" name="dateTime">
    </label><br>
    <button type="submit">Save</button>
</form>
</body>
</html>
