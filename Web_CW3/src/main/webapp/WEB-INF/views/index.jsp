    <!DOCTYPE HTML>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    <%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
    <html lang="en">
    <head> 
        <title>JSP output</title> 
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    </head>
    <body>
        <h1>Welcome ${sessionScope.name }</h1>
        
        <a href="/searchProperty"><p>Search Properties</p></a>
        <a href="/mailBox"><p>View Mailbox</p></a>
        
        
        
        <c:if test="${role=='ROLE_ADMIN'}">
        	<a href="/statsPage"><p>View stats page</p></a>
        	<a href="/reviewReports"><p>View property reports</p></a>
        </c:if>
        
        <c:if test="${role=='ROLE_LANDLORD'}">
        	<a href="/addProperty"><p>Add new property</p></a>
        </c:if>
        
        <a href="/logout"><p>Log out</p></a>
    </body>
    </html>