<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<script type="text/javascript">
    const i18n = [];
    i18n["addTitle"] = '<spring:message code="${param.context}.add"/>';
    i18n["editTitle"] = '<spring:message code="${param.context}.edit"/>';
    <c:forEach var="key" items='<%=new String[]{"common.deleted","common.saved", "common.errorStatus","common.confirm", "common.enabled", "common.disabled", "common.select"}%>'>
    i18n["${key}"] = "<spring:message code="${key}"/>";
    </c:forEach>
</script>
</html>
