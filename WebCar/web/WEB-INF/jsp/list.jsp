<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Car Manager - Car list</title>                       
    </head>
    <body>
        <h1>Car list</h1>

        <table border="1">
            <tr><th>Producer</th><th>Model</th><th>SPZ</th><th>Manufactured</th><th>Price</th></tr>
        <c:forEach var="car" items="${cars}">
            <tr>
                <td><c:out value="${car.producer}"/></td>
                <td><c:out value="${car.model}"/></td>
                <td><c:out value="${car.spz}"/></td>
                <td><c:out value="${car.manufactured}"/></td>
                <td><c:out value="${car.price}"/></td>
            </tr>
        </c:forEach>
        </table>
        
        <p><a href="<c:url value="/AddCar"/>">Add Car</a></p>
        
    </body>
</html>
