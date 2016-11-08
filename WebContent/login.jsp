<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" />
<title>SZGWNET</title>
<link rel="stylesheet" href="css/loginstyle.css" />
<script src="jQuery/jquery.min.js"></script>

<!--表单验证-->
<script src="js/login/common.js"></script>
<!--背景图片自动更换-->
<script src="js/login/supersized.3.2.7.min.js"></script>
<script src="js/login/supersized-init.js"></script>
<script src="js/login/jquery.validate.min.js?var1.14.0"></script>

<script type="text/javascript">
	var errorMsg ='<%=request.getSession().getAttribute("errorMsg")%>';
<%request.getSession().removeAttribute("errorMsg");%>
	if (errorMsg != 'null' && errorMsg != "") {
		alert(errorMsg);
	}
</script>

</head>
<body>
	<div class="login-container">
		<h1>SZGWNET</h1>
		<form action="${pageContext.request.contextPath}/user/login.do"
			method="post" id="loginForm">
			<div>
				<input type="text" name="name" class="name" placeholder="用户名" autocomplete="off" />
			</div>
			<div>
				<input type="password" name="pwd" class="pwd" placeholder="密码" />
			</div>
			<button id="submit" type="submit">登 陆</button>
		</form>
	</div>

</body>
</html>