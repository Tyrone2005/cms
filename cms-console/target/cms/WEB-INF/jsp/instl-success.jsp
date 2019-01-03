<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<% String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+ request.getContextPath()+"/";   %>
<html>
	<head>
		<meta charset="UTF-8">
		<title></title>
		<style>
			#main{
				width:600px;
				height: 100%;
				margin: 0 auto;
			}
			.setTime{
				width: 100%;
				height: 40px;
				line-height: 40px;
				text-align: center;
				color: #999999;
			}
			#times{
				color: red;
			}
			.btn{
				width: 100%;
				height: 60px;
				line-height: 60px;
				text-align: center;
			}
			.btn button{
				width: 100px;
				height: 40px;
				font-size: 25px;
				color: #fff;
				border: none;
				background: #00BFFF;
				border-radius: 5px;
			}
		</style>
	</head>
	<body onload="closeWindow()">
		<div id="main">
			<img src="../static/images/set-success.png" alt="" />
			<div class="setTime">
				<span id="times"></span> 秒后返回主页
			</div>
			<div class="btn">
				<button type="button" onclick='openHref()'>返 回</button>
			</div>
		</div>
		<script>
			var time=5;  
			function openHref(){
				window.location.href = '<%=basePath %>' ;
			}
			
		    function closeWindow(){  
		        window.setTimeout('closeWindow()',1000);  
		        if(time>0){  
		            document.getElementById("times").innerHTML=time;  
		            time--;  
		        }  
		        else{  
		            window.location.href='<%=basePath %>' ;
		        }  
		    } 
		</script>
	</body>
</html>
