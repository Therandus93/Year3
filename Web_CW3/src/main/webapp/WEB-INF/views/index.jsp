<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head> 
   <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
   <meta name="viewport" content="width=device-width, initial-scale=1">
   <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
   <link rel="stylesheet" type="text/css" href="css/style.css">
   <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
   <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
   <title>HomePage</title>
</head>
<body>
<div class="container-fluid bg-1 text-center">
    <div class="Jumbotron" >
    <h1>Welcome to the ClassBuilder</h1>
    <h4> This web site allows you to create and visually represent java classes.</h4>
    </div>
    <div class="row">
        <div class="btn-group btn-group-lg">
          <a href="./taxBuild" class="btn btn-primary">Taxonomy Builder</a>
          <a href="./objRep" class="btn btn-primary">Object Repository</a>
        </div>
    </div>
</div>
</body>
</html>