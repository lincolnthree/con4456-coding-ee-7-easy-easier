<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"
%><!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8" />
    <title>WebSocket Chat</title>
    <link rel="stylesheet" href="res/stylesheets/jquery-ui-1.10.2.css" />
    <link rel="stylesheet" href="res/stylesheets/chat.css" />
    <script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
    <script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.10.2/jquery-ui.min.js"></script>
    <script type="text/javascript">
    var socketPath = "ws://${socketURL}";
    </script>
    <script type="text/javascript" src="res/javascript/my-websocket-app.js"></script>
</head>
<body>
<div id="tabs">
<ul>
<li><a href="#tabs-1">Chat</a></li>
</ul>
<div id="tabs-1">
<p id="error_msg"></p>
<div id="messages"></div>
<div id="input"><input type="text" /></div>
</div>
</div>
</body>
</html>
