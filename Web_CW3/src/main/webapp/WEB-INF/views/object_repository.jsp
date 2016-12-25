<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head> 
    <title>Object Repository</title> 
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
   <meta name="viewport" content="width=device-width, initial-scale=1">
   <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
   <link rel="stylesheet" type="text/css" href="style.css">
   <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
   <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
   <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
   <script>
   	$(document).ready(function(){
   		grabProp();
   	});
   	</script>
	<script type="text/javascript">
      google.charts.load('current', {'packages':['treemap']});
      google.charts.setOnLoadCallback(drawChart);
      function drawChart() {
        var data = google.visualization.arrayToDataTable($(treemap));

        tree = new google.visualization.TreeMap(document.getElementById('chart_div'));

        tree.draw(data, {
          minColor: '#f00',
          midColor: '#ddd',
          maxColor: '#0d0',
          headerHeight: 0,
          fontColor: 'black',
		  width: 600,
          showScale: false

        });

      }
    </script>
   	<script>
	function setValue()  {
		if ($("#pvalue").val() == "") {
			alert("Please enter a value.")
		} else {
				$.ajax({
					 type: "GET",
					 url: "/rest/class/setValue",
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
	}
	function createInstance()  {
		
		$.ajax({
			 type: "GET",
			 url: "/rest/class/createInstance",
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
	function deleteInstance()  {
		$.ajax({
			 type: "GET",
			 url: "/rest/class/deleteInstance",
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
    <h1>Object Repository</h1>
    <h4>Create and view instance of classes</h4>
    </div>
    <div class="row">
          <a href="./index" class="btn btn-primary">Back</a>
    <br/>
    <div class="col-sm-4">
    <div class="panel-group" id="accordion">
    <div class="panel panel-default">
      <div class="panel-heading">
        <h4 class="panel-title">
          <a data-toggle="collapse" data-parent="#accordion" href="#collapse1">Create Instance</a>
        </h4>
      </div>
      <div id="collapse1" class="panel-collapse collapse">
        <div class="panel-body">
    			<table class="table table-bordered">
        			<tbody>
            			<!--  <tr><th>id</th><td><input type="text" name="id" required="required"></td></tr>-->
           				<tr><th>Class Name</th><td><select name="classname" id="createinstancename">
						<c:forEach items="${classes}" var="classes" varStatus="itr">
  							<option value="${classes}">${classes}</option>
           			 	</c:forEach>
           			 	</select>
           				<tr><td colspan="2"> <input type="button" onclick="createInstance()" value="Add" class="btn btn-success"></tr>
        			</tbody>
    			</table>
        </div>
      </div>
    </div>
    <div class="panel panel-default">
      <div class="panel-heading">
        <h4 class="panel-title">
          <a data-toggle="collapse" data-parent="#accordion" href="#collapse2">Set Value</a>
        </h4>
      </div>
      <div id="collapse2" class="panel-collapse collapse">
        <div class="panel-body">
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
           			 <select id="classProp" name="pname">
						
           			 </select>
           			 </td></tr>
            		<tr><th>Property Value</th><td>
					<input type="text" name="value" id="pvalue">
					</td></tr>
           			<tr><td colspan="2"> <input type="button" onclick="setValue()" value="Remove" class="btn btn-danger"></tr>
        		</tbody>
    		</table>
        </div>
      </div>
    </div>
    <div class="panel panel-default">
      <div class="panel-heading">
        <h4 class="panel-title">
          <a data-toggle="collapse" data-parent="#accordion" href="#collapse3">Delete Instance</a>
        </h4>
      </div>
      <div id="collapse3" class="panel-collapse collapse">
        <div class="panel-body">
    		<table class="table table-bordered">
        		<tbody>
            		<tr><th>Instance Name</th><td><select name="classname" id="deleteinstancename">
						<c:forEach items="${classes}" var="classes" varStatus="itr">
  							<option value="${classes}">${classes}</option>
           			 	</c:forEach>
           			 	</select>
           			<tr><td colspan="2"> <input type="button" onclick="deleteInstance()" value="Remove" class="btn btn-danger"></tr>
        		</tbody>
    		</table>
        </div>
      </div>
    </div>
    </div>
    </div>
    <div class="col-sm-4">
    	<div id="chart_div" style="width: 900px; height: 500px;"></div>
    </div>
    <div class="col-sm-4">
    	<p id="test"></p>
    </div>
    
</div>
</div>
</body>
</html>