<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Car Manager - Add new Car</title>
    </head>
    <body>
        <h1>Add new Car</h1>

        <c:if test="${not empty error}">
            <p class="error">
                <c:out escapeXml="false" value="${error}"/>
            </p>
        </c:if>

        <form action="<c:url value="/AddCar"/>" method="post">
            <table>
                <tr>
                    <th>Producer:</th>
                    <td><input type="text" name="producer" value="${carForm.producer}"/></td>
                </tr>
                <tr>
                    <th>Model:</th>
                    <td><input type="text" name="model" value="${carForm.model}"/></td>
                </tr>
                <tr>
                    <th>SPZ:</th>
                    <td><input type="text" name="spz" value="${carForm.spz}"/></td>
                </tr>
                <tr>
                    <th>Manufactured:</th>
                    <td><input type="text" name="manufactured" value="${carForm.manufactured}"/></td>
                </tr>
                <tr>
                    <th>Price:</th>
                    <td><input type="text" name="price" value="${carForm.price}"/></td>
                </tr>
            </table>
            <p>
                <input type="Submit" value="Add Car" name="submit"/>
                <input type="Submit" value="Cancel" name="cancel"/>
            </p>
        </form>
        <p><a href="<c:url value="/ListCars"/>">List Cars</a></p>
    </body>
</html>
