<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<script type="text/javascript" src="<c:url value="/js/jquery-3.1.1.min.js"/>"></script>
	<link href="<c:url value='/css/bootstrap.css' />"  rel="stylesheet"></link>
	<link href="<c:url value='/css/main.css' />"  rel="stylesheet"></link>
<title>Insert title here</title>
</head>

<title>Insert title here</title>
</head>
<body>
<div class="container">
	<button type="button" class="btn btn-danger">Create</button>
	<button type="button" class="btn btn-info">PutItem</button>
	<button type="button" class="btn btn-success">PutItem</button>
	
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
	
	$('.btn-success').bind({
		click:query
	})
	
	 
}


function createTable (){
	console.log('............createTable.....');
	
	$.ajax({
        url: 'createTable',
        data: {id:'1'},
        type:"POST",
        dataType:'text',
        success: function(data){
            alert(data);
        },
         error:function(xhr, ajaxOptions, thrownError){ 
            alert(xhr.status); 
            alert(thrownError); 
         }
    });
}

function putItem (){
	console.log('..........putItem.......');
}

function query (){
	console.log('..........query.......');
}


</script>

</body>
</html>