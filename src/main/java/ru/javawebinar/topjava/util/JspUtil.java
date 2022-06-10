package ru.javawebinar.topjava.util;

import javax.servlet.http.HttpServletRequest;

public class JspUtil {
    public static Integer getIdFromRequest(HttpServletRequest request, String name) {
        return request.getParameter(name) != null ? Integer.parseInt(request.getParameter(name)) : null;
    }
}
