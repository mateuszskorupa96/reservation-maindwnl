<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page session="false" contentType="text/html; charset=UTF-8"%>

<!DOCTYPE html>
<html lang=pl>
<head>
<c:import url="common-header.jsp" />
</head>
<body>

	<c:import url="header.jsp" />

	<c:if test="${not empty parkingList}">

		<c:if test="${not empty  operationMessage}">
			<p>
				<spring:message code="${operationMessage}" />
			</p>
		</c:if>

		<h1><spring:message code="parking.list.choose"/> </h1>
		<ul>
			<c:forEach items="${parkingList}" var="parking">
				<spring:url value="car-park/${parking.id}" var="parkingUrl" />
				<li><a href="${parkingUrl}">${parking.name}</a></li>
			</c:forEach>
		</ul>

	</c:if>

	<c:import url="footer.jsp" />

</body>
</html>