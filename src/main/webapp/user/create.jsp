<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>User Management Application</title>
</head>
<body>
<center>
    <h1>User Management</h1>
    <h2>
        <a href="/users">List All Users</a>
    </h2>
    <%!
    	String hello;
    %>
    
    <%
		//below line will navigate us to other resource let’s say google for now
		hello = request.getServletPath();
		System.out.println("Test jsp: " + application.getAttribute("listCountry"));
		
	%>
	<h1><%= hello %></h1>
	<c:out value="${hello }"></c:out>
</center>
<div align="center">

    <form method="post">
        <table border="1" cellpadding="5">
            <caption>
                <h2>Add New User</h2>
            </caption>
            <tr>
                <th>User Name:</th>
                <td>
                    <input type="text" name="name" id="name" size="45"/>
                </td>
            </tr>
            <tr>
                <th>User Email:</th>
                <td>
                    <input type="text" name="email" id="email" size="45"/>
                </td>
            </tr>
            <tr>
                <th>Country:</th>
                <td>
                    <select name="country">
                    	<c:forEach var ="country" items="${applicationScope.listCountry}">
                    		<option value="${country.id}">
                    			<c:out value="${country.name }"></c:out>
                    		</option>
                    	</c:forEach>
                    </select>
                </td>
                
            </tr>
            <tr>
                <td colspan="2" align="center">
                    <input type="submit" value="Save"/>
                </td>
            </tr>
        </table>
    </form>
</div>
</body>
</html>