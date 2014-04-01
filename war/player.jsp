<%@page import="java.net.URLEncoder"%>
<%@page import="nikunjy.beathangar.GlobalConstants"%>
<%@page import="java.util.List"%>
<%
	List<String> music = (List<String>)request.getAttribute("musicList");
	String name = (String)request.getAttribute("name");
	String image = (String)request.getAttribute("image");
%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css" href="http://bootswatch.com/superhero/bootstrap.css" />
<link rel="stylesheet" type="text/css" href="http://bootswatch.com/assets/css/bootswatch.min.css" />
<script src="js/jquery.js"></script>
<style type="text/css">
	.navbar-center {
		left:40%; 
		position:absolute;
	}
	
</style>
</head>
  <body>
  <div class="navbar navbar-default navbar-fixed-top">
      <div class="container">
        <div class="navbar-header">
          <a href="../" class="navbar-brand">VRadio</a>
          <button class="navbar-toggle" type="button" data-toggle="collapse" data-target="#navbar-main">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
        </div>
        <div class="navbar-collapse collapse" id="navbar-main">
          <ul class="nav navbar-nav navbar-center">
          	<li>
          	<div id="title" style="margin-left:10px;width:300px;font-size:14px;margin-right:30px;text-align:center;">
          	</div>
          	</li>
          	<li>
          	<div id="refresh" style="margin-left:40px;width:300px;font-size:14px;margin-right:30px;text-align:center;">
          	<img src="images/refresh-icon.png" height=40>
          	</div>
          	</li>
          </ul>
          <ul class="nav navbar-nav navbar-right">
            <li><a href="http://builtwithbootstrap.com/" target="_blank">Settings</a></li>
            <li><a href="http://facebook.com/" target="_blank"><img src="<%out.print(image);%>" style="zoom:0.10;"></a></li>
          </ul>

        </div>
      </div>
    </div>
    <br/><br/>
    <div class="container">
    <div class="row">
  		<div class="col-md-4">
    	<div class="well">
    		<%
    		out.println("<b>Artists for "+name+" </b>:<br/>");
    		for(String artist : music) { 
    			out.println(artist+"<br/>");
    		}
    		%>
    	</div>
    	</div>
    	<div class="col-md-8">
    	<div id="player"></div>
    	</div>
    </div>
    </div>
    <script>
      var tag = document.createElement('script');
      tag.src = "https://www.youtube.com/iframe_api";
      var firstScriptTag = document.getElementsByTagName('script')[0];
      firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);
      var player;
      function onYouTubeIframeAPIReady() {
    	  $.ajax({
    		  dataType: "json",
    		  url: "/suggest"
    		}).done(function(data) {
    		$("#title").text(data.title);
        	player = new YT.Player('player', {
          	height: '400',
          	width: '800',
          	videoId: data.id,
          	events: {
            	'onReady': onPlayerReady,
            	'onStateChange': onPlayerStateChange
          	}
        	});
    		});
      }
      function onPlayerReady(event) {
        event.target.playVideo();
      }
      function onPlayerStateChange(event) {
        if (event.data == YT.PlayerState.ENDED) {
        	$.ajax({
        		dataType: "json",
        		  url: "/suggest"
        		}).done(function(data) {
        			$("#title").text(data.title);
        			player.loadVideoById(data.id);
        		});
        }
      }
      function stopVideo() {
        player.stopVideo();
      }
      $(document).ready(function(e) {
    	 $("#refresh").click(function(event){
    		 $.ajax({
    			 dataType: "json",
       		  	url: "/suggest"
       		}).done(function(data) {
       			$("#title").text(data.title);
       			player.loadVideoById(data.id);
       		});
    	 });
      });
    </script>
  </body>
</html>