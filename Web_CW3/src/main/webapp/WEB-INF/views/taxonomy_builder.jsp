<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head> 
    <title>Taxonomy Builder</title> 
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
   <meta name="viewport" content="width=device-width, initial-scale=1">
   <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
   <link href="css/style.css" type="text/css" rel="stylesheet">
   <style id="treeview1-style" type="text/css"> 
   .treeview .list-group-item{cursor:pointer}.treeview span.indent{margin-left:10px;margin-right:10px}
   .treeview span.icon{width:12px;margin-right:5px}.treeview .node-disabled{color:silver;cursor:not-allowed}
   .node-treeview1{}.node-treeview1:not(.node-disabled):hover{background-color:#F5F5F5;} 
   </style>
   <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
   <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script src="js/bootstrap-treeview.js"></script>
<script>



$(document).ready(function(){

	var tree;
	
	$.ajax({
		type: "GET",
		url: "/rest/class/hierarchy/root",
		dataType: 'json',
		success: function(result){
			var defaultData = JSON.stringify(result);
			tree = defaultData;
			$("#test").text(defaultData);
	}});
				
	$('#treeview').treeview({data: tree});
});


$("#classesProp").change(function() {
	$("#test").text("");
	$.ajax({
		 type: "GET",
		 url: "/grabProperties",
		 success: function(result){
			 
		 }});
})
	


  	</script>
</head>
<body>
<div class="container-fluid bg-1 text-center">
    <div class="Jumbotron" >
    <h1>Taxonomy Builder</h1>
    <h4>create/delete/alter classes</h4>
    </div>
    <div class="row">
          <a href="/index" class="btn btn-primary">Back</a>
    </div>
    <br/>
    <div class="col-sm-4">
    
    <div class="panel-group" id="accordion">
    <div class="panel panel-default">
      <div class="panel-heading">
        <h4 class="panel-title">
          <a data-toggle="collapse" data-parent="#accordion" href="#collapse1">Create Class</a>
        </h4>
      </div>
      <div id="collapse1" class="panel-collapse collapse">
        <div class="panel-body">
        	<form action="/rest/class/create" method="post">
    			<table class="table table-bordered">
        			<tbody>
            			<!--  <tr><th>id</th><td><input type="text" name="id" required="required"></td></tr>-->
           				<tr><th>Class Name</th><td><input type="text" name="name" required="required"></td></tr>
            			<tr><th>Superclass Name</th><td><input type="text" name="superclass"></td></tr>
           				<tr><td colspan="2"> <input type="submit" value="Create" class="btn btn-success"></tr>
        			</tbody>
    			</table>
    		</form>
        </div>
      </div>
    </div>
    <div class="panel panel-default">
      <div class="panel-heading">
        <h4 class="panel-title">
          <a data-toggle="collapse" data-parent="#accordion" href="#collapse2">Add Property</a>
        </h4>
      </div>
      <div id="collapse2" class="panel-collapse collapse">
        <div class="panel-body">
        	<form action="/rest/class/addprop" method="post">
    			<table class="table table-bordered">
        			<tbody>
            			<!--  <tr><th>id</th><td><input type="text" name="id" required="required"></td></tr>-->
           				<tr><th>Property Name</th><td><input type="text" name="className" required="required"></td></tr>
            			<tr><th>Property Type</th><td><input type="text" name="className" required="required"></td></tr>
           				<tr><td colspan="2"> <input type="submit" value="Add" class="btn btn-success"></tr>
        			</tbody>
    			</table>
    		</form>
        </div>
      </div>
    </div>
    <div class="panel panel-default">
      <div class="panel-heading">
        <h4 class="panel-title">
          <a data-toggle="collapse" data-parent="#accordion" href="#collapse3">Remove Property</a>
        </h4>
      </div>
      <div id="collapse3" class="panel-collapse collapse">
        <div class="panel-body">
        	<form action="/rest/class/addprop" method="post">
    			<table class="table table-bordered">
        			<tbody>
            			<!--  <tr><th>id</th><td><input type="text" name="id" required="required"></td></tr>-->
           				<tr><th>Class Name</th><td><select id="classesProp">
						<c:forEach items="${classes}" var="classes" varStatus="itr">
  							<option value="${classes}">${classes}</option>
           			 	</c:forEach>
           			 	</select>
           			 	</td></tr>
           			 	<tr><th>Property Name</th><td><select>
						<c:forEach items="${property}" var="properties" varStatus="itr">
  							<option value="${property}">${property}</option>
           			 	</c:forEach>
           			 	</select>
           			 	</td></tr>
           				<tr><td colspan="2"> <input type="submit" value="Remove" class="btn btn-danger"></tr>
        			</tbody>
    			</table>
    		</form>
        </div>
      </div>
    </div>
    <div class="panel panel-default">
      <div class="panel-heading">
        <h4 class="panel-title">
          <a data-toggle="collapse" data-parent="#accordion" href="#collapse4">Delete Class</a>
        </h4>
      </div>
      <div id="collapse4" class="panel-collapse collapse">
        <div class="panel-body">
        	<form action="/rest/class/delete" method="post">
    			<table class="table table-bordered">
        			<tbody>
            			<!--  <tr><th>id</th><td><input type="text" name="id" required="required"></td></tr>-->
           				<tr><th>Class Name</th><td><select id="classDel" name="name">
						<c:forEach items="${classes}" var="classes" varStatus="itr">
  							<option value="${classes}">${classes}</option>
           			 	</c:forEach>
           			 	</select>
            			</td></tr>
           				<tr><td colspan="2"> <input type="submit" value="Delete" class="btn btn-danger"></tr>
        			</tbody>
    			</table>
    		</form>
        </div>
      </div>
    </div>
  	</div>
	</div>
	 <div class="col-sm-4">
    	<div class="treeview" id="treeview1">
    		<div class="panel-group">
    			<div class="panel panel-default">
      				<div class="panel-heading">
        				<h4 class="panel-title">
          				<a data-toggle="collapse" href="#collapse5">Get JSON</a>
       				 	</h4>
      				</div>
      			<div id="collapse5" class="panel-collapse collapse">
       		<div class="panel-body">
				<form action="/rest/class/hierarchy" method="get">
    			<table class="table table-bordered">
        			<tbody>
            			<!--  <tr><th>id</th><td><input type="text" name="id" required="required"></td></tr>-->
           				<tr><th>Root Class</th><td><select name="name">
						<c:forEach items="${classes}" var="classes" varStatus="itr">
  							<option value="${classes}">${classes}</option>
           			 	</c:forEach>
           			 	</select>
            			</td></tr>
            			<tr><th>Direct</th><td><select name="direct">
            				<option value="false">False</option>
							<option value="true">True</option>
           			 	</select>
            			</td></tr>
           				<tr><td colspan="2"> <input type="submit" value="Get JSON Tree" class="btn btn-info"></tr>
        			</tbody>
    			</table>
    		</form>
			</div>
      </div>
    </div>
  </div>
    	</div>
	</div>
	<div class="col-sm-4">
	<div><p id="test">Base Text</p></div>
	</div>
	<!-- <ul class="list-group">
    	<li class="list-group-item node-treeview1" style="color:undefined;background-color:undefined;" data-nodeid="0">
    	<span class="icon expand-icon glyphicon glyphicon-minus"></span>
    	<span class="icon node-icon"></span>Parent 1</li>
    	<li class="list-group-item node-treeview1" style="color:undefined;background-color:undefined;" data-nodeid="1">
    	<span class="indent"></span>
    	<span class="icon expand-icon glyphicon glyphicon-minus"></span>
    	<span class="icon node-icon"></span>Child 1</li>
    	<li class="list-group-item node-treeview1" style="color:undefined;background-color:undefined;" data-nodeid="2">
    	<span class="indent"></span><span class="indent"></span><span class="icon glyphicon"></span>
    	<span class="icon node-icon"></span>Child 1</li>
    	<li class="list-group-item node-treeview1" style="color:undefined;background-color:undefined;" data-nodeid="4">
    	<span class="indent"></span><span class="icon glyphicon"></span>
    	<span class="icon node-icon"></span>Child 2</li>
    	<li class="list-group-item node-treeview1" style="color:undefined;background-color:undefined;" data-nodeid="5">
    	<span class="icon glyphicon"></span>
    	<span class="icon node-icon"></span>Parent 2</li>
    	<li class="list-group-item node-treeview1" style="color:undefined;background-color:undefined;" data-nodeid="6">
    	<span class="icon glyphicon"></span>
    	<span class="icon node-icon"></span>Parent 3</li>
    	<li class="list-group-item node-treeview1" style="color:undefined;background-color:undefined;" data-nodeid="7">
    	<span class="icon glyphicon"></span>
    	<span class="icon node-icon"></span>Parent 4</li>
    	<li class="list-group-item node-treeview1" style="color:undefined;background-color:undefined;" data-nodeid="8">
    	<span class="icon glyphicon"></span>
    	<span class="icon node-icon"></span>Parent 5</li></ul> -->
</div>

</body>
</html>