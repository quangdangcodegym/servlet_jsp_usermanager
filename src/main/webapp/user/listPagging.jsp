<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>User Management Application</title>
    <script src="/js/app.js"></script>
    <link rel="stylesheet" href="/css/app.css">
</head>
<body>
<center>
    <h1 class="header">User Management</h1>
    <h2>
        <a href="/users?action=create">Add New User</a>
    </h2>
    
    <form method="get" ">
	    <input type ="text" placeholder="search..." name = "q" value="${q }"/>
	    <select name="country">
 			<option value="-1">All</option>
 			<c:forEach var="j" items="${listCountry }" >
 				<c:choose>
 					<c:when test="${country== j.getId()}">
 						<option value="${j.getId() }" selected>${j.name }</option>
 					</c:when>
 					<c:otherwise>
 						<option value="${j.getId() }">${j.name }</option>
 					</c:otherwise>
 				</c:choose>
 				
 			</c:forEach>
 		</select>
	    <button >Search</button>
 		
    </form>
</center>
<div align="center">
    <table border="1" cellpadding="5">
        <caption><h2>List of Users</h2></caption>
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Email</th>
            <th>Country</th>
            <th>Actions</th>
        </tr>
        <c:forEach var="user" items="${listUser}">
            <tr>
                <td><c:out value="${user.id}"/></td>
                <td><c:out value="${user.name}"/></td>
                <td><c:out value="${user.email}"/></td>
                <td>
                
                	<c:forEach var="c" items="${listCountry}">
                		<c:if test="${user.getCountry() == c.getId() }">
                			<c:out value="${c.name }"/>
                		</c:if>
                	</c:forEach>
                </td>
                <td>
                    <a href="/users?action=edit&id=${user.id}">Edit</a>
                    <a href="/users?action=delete&id=${user.id}">Delete</a>
                </td>
            </tr>
        </c:forEach>
    </table>
    
    <div class = "pagination">
    	<c:if test="${currentPage!=1 }">
    		 <a href="/users?page=${currentPage-1 }&q=${q}&country=${country}">«</a>
    	</c:if>
    	<c:forEach begin="1" end="${noOfPages }" var = "i">
    		<c:choose>
    			<c:when test="${currentPage eq i }">
    				<a href="#" class="active">${i }</a>
    			</c:when>
    			<c:otherwise>
    				<a href="/users?page=${i }&q=${q}&country=${country}">${i }</a>
    			</c:otherwise>
    		</c:choose>
    	
    	</c:forEach>
    	
    	<c:if test="${currentPage lt noOfPages }">
    		<a href="/users?page=${currentPage +1 }&q=${q}&country=${country}">»</a>
    	</c:if>
    </div>
    
</div>
</body>
</html>