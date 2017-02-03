<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" type="text/css" href="<c:url value="/css/main.css"/>" />
	<link rel="stylesheet" type="text/css" href="<c:url value="/css/bootstrap.css"/>" />
	<script type="text/javascript" src="<c:url value="/js/jquery-3.1.1.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/js/bootstrap.min.js"/>"></script>
<title>Insert title here</title>
</head>

<title>Insert title here</title>
</head>
<body>
<div class="container">
	<div class="starter-template">
		<button type="button" class="btn btn-danger">Create</button>
		<button type="button" class="btn btn-info">PutItem</button>
		<button type="button" class="btn btn-default">PutItem2</button>
		<button type="button" class="btn btn-primary">Get Count</button>
		<button type="button" class="btn btn-success">Query</button>
		<button type="button" class="btn btn-warning">DeleteTable</button>
	</div>
</div>

<script type="text/javascript">
$(function() {
	bindBtnEvent();
});

function bindBtnEvent() {
	$('.btn-danger').bind({
		click:createTable
	});
	
	$('.btn-info').bind({
		click:putItem
	})
	
	$('.btn-default').bind({
		click:putItem2
	})
	
	$('.btn-success').bind({
		click:query
	})
	
	$('.btn-warning').bind({
		click:deleteTable
	})
	
	$('.btn-primary').bind({
		click:getCount
	})
	
}


function createTable (){
	console.log('............createTable.....');
	$.ajax({
        url: 'createTable.do',
        type:"POST",
        dataType:'text',
        success: function(data){
            console.log(data);
            alert('createTable status : ' + data);
        }
    });
}

function putItem (){
	console.log('..........putItem.......');
	$.ajax({
        url: 'putItem.do',
        type:"POST",
        dataType:'text',
        success: function(data){
            console.log(data);
            alert('putItem status : ' + data);
        }
    });
}

function putItem2 (){
	console.log('..........putItem2.......');
	$.ajax({
        url: 'putItem2.do',
        type:"POST",
        dataType:'text',
        success: function(data){
            console.log(data);
            alert('putItem2 status : ' + data);
        }
    });
}

function query (){
	console.log('..........query.......');
	$.ajax({
        url: 'query.do',
        type:"POST",
        dataType: "json",
        success: function(data){
            console.log(data);
            alert('query data : ' + data);
        }
    });
}

function deleteTable (){
	console.log('..........delete.......');
	$.ajax({
        url: 'deleteTable.do',
        type:"POST",
        dataType:'text',
        success: function(data){
            console.log(data);
            alert('delete status : ' + data);
        }
    });
}

function getCount (){
	console.log('..........getCount.......');
	$.ajax({
        url: 'getCount.do',
        type:"POST",
        dataType:'text',
        success: function(data){
            console.log(data);
            alert('getCount : ' + data);
        }
    });
}


</script>

</body>
</html>