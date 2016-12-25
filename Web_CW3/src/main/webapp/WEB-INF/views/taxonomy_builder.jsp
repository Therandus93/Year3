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
   <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
   <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script>



$(document).ready(function(){

	var tree;
	
	grabProp();
	
	$.ajax({
		type: "GET",
		url: "/rest/class/tree",
		dataType: 'json',
		success: function(result){
			var defaultData = JSON.stringify(result);
			tree = defaultData.replace(/},/g, "},<br/>"); 
			$("#test").html("<p>" + tree + "</p><br/> <strong> Couldn't get the Graphical TreeView to work, added the level property" +
					" to help show the Tree levels but one is wrong (Due to multiple inheritences), but if you follow the colons and DB, it is correct," +
					" in case you couldn't tell, i ran out of time</strong>");
	}});
	
});


</script>
<script>
function grabProp()  {
			$.ajax({
				 type: "GET",
				 url: "/rest/class/grabProp/",
				 data:  "name=" + $("#classesProp").val(),
				 success: function(result){
					 p = result;
	    			 var options = '';
	                 for (var i = 0; i < p.length; i++) {
	                   options += '<option value="' + p[i] + '">' + p[i] + '</option>';
	                 }
	                 $("#classProp").html(options);
	    		}	 	
		});	
}


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
           				<tr><th>Class Name</th><td><select name="classname">
						<c:forEach items="${classes}" var="classes" varStatus="itr">
  							<option value="${classes}">${classes}</option>
           			 	</c:forEach>
           			 	</select>
           			 	</td></tr>
           			 	<tr><th>Property Name</th><td><input type="text" name="pname"></td></tr>
            			<tr><th>Property Type</th><td>
						<select id="propType" name="type">
							<option value="String">String</option>
							<option value="int">Integer</option>
							<option value="double">Double</option>
							<option value="boolean">Boolean</option>
							<c:forEach items="${classes}" var="classes" varStatus="itr">
  							<option value="${classes}">${classes}</option>
           			 		</c:forEach>
           			 	</select>
						</td></tr>
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
        	<form action="/rest/class/delprop" method="post">
    			<table class="table table-bordered">
        			<tbody>
            			<!--  <tr><th>id</th><td><input type="text" name="id" required="required"></td></tr>-->
           				<tr><th>Class Name</th><td><select id="classesProp" onChange="grabProp()">
						<c:forEach items="${classes}" var="classes" varStatus="itr">
  							<option value="${classes}">${classes}</option>
           			 	</c:forEach>
           			 	</select>
           			 	</td></tr>
           			 	<tr><th>Property Name</th><td>
           			 	<select id="classProp">
						
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
    <div class="panel panel-default">
      <div class="panel-heading">
        <h4 class="panel-title">
          <a data-toggle="collapse" data-parent="#accordion" href="#collapse5">Create Tree From String</a>
        </h4>
      </div>
      <div id="collapse5" class="panel-collapse collapse">
        <div class="panel-body">
        	<form action="/rest/class/createHierarchy" method="post" id="createhierarchy">
    			<table class="table table-bordered">
        			<tbody>
            			<!--  <tr><th>id</th><td><input type="text" name="id" required="required"></td></tr>-->
           				<tr><th>Class Name</th><td><select id="classDel" name="root">
           				<option value="Root">Root</option>
						<c:forEach items="${classes}" var="classes" varStatus="itr">
  							<option value="${classes}">${classes}</option>
           			 	</c:forEach>
           			 	</select>
            			</td></tr>
            			<tr><th>Hierarchy String</th><td><textarea name="construct" rows="4" cols="50">Enter text here...</textarea></td></tr>
           				<tr><td colspan="2"> <input type="submit" value="Create" class="btn btn-success"></tr>
        			</tbody>
    			</table>
    		</form>
        </div>
      </div>
    </div>
  	</div>
  	<div class="panel-group" id="accordionJSON">
    <div class="panel panel-default">
      <div class="panel-heading">
        <h4 class="panel-title">
          <a data-toggle="collapse" data-parent="#accordionJSON" href="#collapseJSON1">Get JSON (Includes MetaType Values)</a>
        </h4>
      </div>
      <div id="collapseJSON1" class="panel-collapse collapse">
        <div class="panel-body">
        	<form action="/rest/class/hierarchy" method="get">
    			<table class="table table-bordered">
        			<tbody>
            			<!--  <tr><th>id</th><td><input type="text" name="id" required="required"></td></tr>-->
           				<tr><th>Root Class</th><td><select name="name">
           				<option value="Root">Root</option>
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
    <div class="panel panel-default">
      <div class="panel-heading">
        <h4 class="panel-title">
          <a data-toggle="collapse" data-parent="#accordionJSON" href="#collapseJSON2">Get SubClass List</a>
        </h4>
      </div>
      <div id="collapseJSON2" class="panel-collapse collapse">
        <div class="panel-body">
        	<form action="/rest/class/getSubclassList" method="post">
    			<table class="table table-bordered">
        			<tbody>
            			<!--  <tr><th>id</th><td><input type="text" name="id" required="required"></td></tr>-->
           				<tr><th>Root Class</th><td><select name="name">
           				<option value="Root">Root</option>
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
           				<tr><td colspan="2"> <input type="submit" value="Get Tree List" class="btn btn-info"></tr>
        			</tbody>
    			</table>
    		</form>
        </div>
      </div>
    </div>
    </div>
	</div>
	<div class="col-sm-4">
    <div id="tree">
    <h2>Tree View (Simplified)</h2>
  		<p id="test">Base Text</p>
    </div>
	</div>
	<div class="col-sm-4">
	<h2>SubClass List</h2>
		<table class="table table-bordered">
    		<tbody>
				<tr><th>Selected Class:</th><td>
    				<c:out value="${selectedclass}"/>
    			</td></tr>
    			<tr><th>is Direct?:</th><td>
    				<c:out value="${direct}"/>
    			</td></tr>
				<tr><th>SubClass List</th><td>
    				<c:out value="${subtree}"/>
    			</td></tr>
    		</tbody>
    	</table>
	</div>
</div>

</body>
</html>