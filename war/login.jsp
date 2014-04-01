<%@page import="java.net.URLEncoder"%>
<%@page import="nikunjy.beathangar.GlobalConstants"%>
<!DOCTYPE html>
<html lang="en" class="no-js">
<head>
<link rel="stylesheet" type="text/css" href="css/login.css" />
<meta charset="UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" type="text/css" href="css/default.css" />
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<script src="js/jquery.easing.min.js"></script>
<script src="js/waypoints.min.js"></script>
<script src="js/jquery.debouncedresize.js"></script>
<script src="js/fixedScrollLayout.js"></script>
<script src="js/main.js"></script>
<style type="text/css">
.centered {
	position: fixed;
	z-index: 100;
	top: 50%;
	left: 50%;
	margin: -100px 0 0 -100px;
	width: 300px;
	height: 300px;
	text-align: center;
	opacity: 0.8;
}
.caption p {
	font-size : 14;
	font-family: "Calibri";
}
 
pre {
	white-space: nowrap;
	font-family: Consolas, Monaco, Lucida Console, Liberation Mono,
		DejaVu Sans Mono, Bitstream Vera Sans Mono, Courier New;
}
</style>
</head>
<body>
<div class="container">
<div id="scroller" class="scroller"> </div>
</div>
<script>

function go(fb_redirect) {
	var fbURL = "http://www.facebook.com/dialog/oauth?client_id=1422564348001086&redirect_uri="
		+ encodeURIComponent(fb_redirect)
		+ "&scope=email,user_likes";
	window.location=fbURL;	
}
</script>
<div id="sec1">
	<form class="login" onsubmit="return false">
		<h1>
			<pre>&lt;Beat-Hangar&gt;</pre>
		</h1>
		<div class="caption"><p>Radio but on video!</p></div>
		<input type="submit" value="Login with FB" class="login-submit"
			onclick="go('<%=GlobalConstants.FB_URL%>')">
		<p class="login-help">
			<a href="/signinGoogle">Dont have Facebook?</a>
		</p>
	</form>
</div>
</body>
</html>