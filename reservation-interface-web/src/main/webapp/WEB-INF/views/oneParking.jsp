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

	<spring:url value="/car-park" var="parkingListUrl" />
	<a href=${parkingListUrl }><spring:message code="parking.list.back"/> </a>
	<ul>
		<li><span>${parking.name}</span></li>
	</ul>

	<c:if test="${not empty parking.levels}">
		<h1><spring:message code="level.list.choose"/></h1>
		<ul>
			<c:forEach items="${parking.levels}" var="level">
				<spring:url value="${parking.id}/level/${level.id}" var="levelUrl" />
				<li>
					<a href=${levelUrl}>Poziom nr ${level.order}</a></li>
			</c:forEach>
		</ul>
	</c:if>
	<c:import url="footer.jsp" />

</body>
</html>