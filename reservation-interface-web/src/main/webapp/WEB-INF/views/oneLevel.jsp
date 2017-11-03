<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page session="false" contentType="text/html; charset=UTF-8"%>

<!DOCTYPE html>
<html lang=pl>
	<head>
		<c:import url="common-header.jsp" />
	</head>
	<body>
		<c:import url="header.jsp" />
	
		<spring:url value="/car-park/${level.parking.id}" var="parkingDetailsUrl"/>
		<a href="${parkingDetailsUrl}"><spring:message code="level.list.back"/></a>
	
		<ul>
			<li><span>${level.parking.name}</span></li>
			<li><span>Poziom nr ${level.order}</span></li>
		</ul>
					
							
		<c:if test="${not empty operationMessage or not empty operationMessageErr}">
			<c:if test="${not empty operationMessage}">
				<p>
					<spring:message code="${operationMessage}" arguments="${operationMessageArgs}" />
				</p>
			</c:if>
			<c:if test="${not empty operationMessageErr}">
				<p><spring:message code="${operationMessageErr}" arguments="${operationMessageArgs}" /></p>
			</c:if>
		</c:if>
	
		<h1><spring:message code="manage.place"/> </h1>

		<table border="1">
			<tbody>
				<c:forEach items="${level.rows}" var="row" varStatus="rowStatus">
				<tr>
					<c:if test="${rowStatus.index eq 0}">
					<td rowspan="${fn:length(level.rows) * 2 - 1}">
						<span><spring:message code="entry.road"/></span>
					</td>
					</c:if>
					<c:forEach items="${row.parkingSpaces}" var="parkingSpace">
						<td>
							<c:choose>
								<c:when test="${parkingSpace.taken}">
									<span>zajęte</span>
								</c:when>
								<c:otherwise>
									<span>wolne</span>
								</c:otherwise>
							</c:choose>
						
							<c:if test="${parkingSpace.forDisable}">
								<br/>
								<span>dla niepełnosprawnych</span>
							</c:if>
							<br/>
							<span>${parkingSpace.placeNumber}</span>
							
							<form:form>
								<input type="hidden" name="placeNumber" value="${parkingSpace.placeNumber}"/>
								<input type="hidden" name="rowId" value="${row.id}"/>
								<input type="hidden" name="placeId" value="${parkingSpace.id}"/>
								<input type="hidden" name="book" value="${not parkingSpace.taken}"/>
								<c:choose>
									<c:when test="${parkingSpace.taken}">
										<spring:message code="place.release" var="buttonMessage"/>
									</c:when>
									<c:otherwise>
										<spring:message code="place.book" var="buttonMessage"/>
									</c:otherwise>
								</c:choose>
								<input type="submit" value="${buttonMessage}"/>
							</form:form>
						</td>
					</c:forEach>
					<c:if test="${fn:length(row.parkingSpaces) lt level.maxPlacesFromAll}">
						<c:forEach begin="0" end="${level.maxPlacesFromAll - fn:length(row.parkingSpaces) - 1}">
							<td>&nbsp;</td>
						</c:forEach>
					</c:if>
				</tr>
				<c:if test="${rowStatus.count lt fn:length(level.rows)}">
					<tr>
						<td colspan="${level.maxPlacesFromAll}">
							<span><spring:message code="level.details.avenue" arguments="${rowStatus.count}"/></span>
						</td>
					</tr>
				</c:if>
				</c:forEach>
			</tbody>
		</table>

		<c:import url="footer.jsp" />
	</body>
</html>