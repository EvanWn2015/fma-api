<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
	<script type="text/javascript" src="<c:url value="/js/jquery-3.1.1.min.js"/>"></script>
	<link href="<c:url value='/css/bootstrap.css' />"  rel="stylesheet"></link>
	<link href="<c:url value='/css/main.css' />"  rel="stylesheet"></link>
<title>Insert title here</title>
</head>
<body>
	<div id="mainWrapper">
		<div class="login-container">
			<div class="login-card">
				<div>
				
					<div class=" input-sm">
						<input type="text" class="form-control" id="username" name="ssoId" placeholder="Enter Username" required>
					</div>
					<div class="input-sm">
						<input type="password" class="form-control" id="password" name="password" placeholder="Enter Password" required>
					</div>
					<div class="form-actions">
						<input type="submit" class="btn btn-block btn-primary"  id="btnLogin" value="Log in">
					</div>
				</div>
			
			</div>
		</div>
	</div>
<script type="text/javascript">
$(function() {
	bindEvent();
})

function bindEvent() {
	$('#btnLogin').bind({
		click : login
	}); 
}
	
function login (){
	
/* 	$.post("login", { 
			username : $('#username').val(),
			password : $('#password').val()
			},function(data){
			console.log('' + data.username);
			     alert("Data Loaded: " + data.username);
		}); */

	$.ajax({
		url: 'login',
		type: 'POST',
		data: {
			username : $('#username').val(),
			password : $('#password').val()
		},
		success: function(data) {
			/*  alert('登陸成功------'); */
		}
	 });
}
</script>
</body>
</html>