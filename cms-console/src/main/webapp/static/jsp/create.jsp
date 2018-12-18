<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!doctype html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="static/css/bootstrap.min.css" />
<script type="text/javascript" src="static/js/jquery-3.3.1.js"></script>
<script type="text/javascript" src="static/layer/layer.js"></script>
<script type="text/javascript" src="static/js/validform-1.19.0.js"></script>
<style>
#licenseTab {
	width: 600px;
	margin: 10px auto;
}

#licenseTab table tr th {
	line-height: 2.6;
}

caption {
	text-align: center;
}

#licenseBtn {
	width: 100%;
	height: 40px;
	text-align: right;
}
</style>
</head>
<body>
		<div id="licenseTab">
			<table class="table table-striped table-bordered">
				<caption>
					<h1>License 创建</h1>
				</caption>
				<tbody>
					<tr class="success">
						<th>ID</th>
						<td><input type="text" class="form-control" id="subject"
							name="subject" placeholder="请输入ID" disabled="disabled"
							value="newVerification"></td>
					</tr>
					<tr class="warning">
						<th>MAC地址</th>
						<td><input type="text" class="form-control" id="macAddress"
							name="macAddress" placeholder="请输入MAC地址,格式如：80-00-0B-56-3B-32"
							></td>
					</tr>
					<tr class="danger" id="dirInput">
						<th>License路径</th>
						<td><input type="text" class="form-control" id="licPath"
							name="licPath" placeholder="请输入License创建路径"
							value="C:\Users\Public\Downloads"></td>
					</tr>
					<!--  
					<tr class="danger" id="dirChoose">
						<th>保存路径：</th>
						<td><input id="filePath" type="text" name="licPath" size="30"
							value="" class="form-control" onclick="browseFolder()"></td>
					</tr>-->
					<tr class="success">
						<th>发布日期</th>
						<td><input type="date" class="form-control" id="issuedTime"
							name="issuedTime" value=""></td>
					</tr>
					<tr class="warning">
						<th>生效日期</th>
						<td><input type="date" class="form-control" id="notBefore"
							name="notBefore" value=""></td>
					</tr>
					<tr class="danger">
						<th>失效日期</th>
						<td><input type="date" class="form-control" id="notAfter"
							name="notAfter" value=""></td>
					</tr>
				</tbody>
			</table>
			<div id="licenseBtn">
				<button id="btn_sub" class="btn btn-info" onclick="create()">生成</button>
			</div>
		</div>
	<script>
		$(function() {

			//日期默认取当前日期
			var time = new Date();
			var day = ("0" + time.getDate()).slice(-2);
			var month = ("0" + (time.getMonth() + 1)).slice(-2);
			var today = time.getFullYear() + "-" + (month) + "-" + (day);
			$('input[type="date"]').val(today);

			//浏览器
			if (getOs() == 'Chrome' || getOs() == 'Firefox') {
				//$("#dirChoose").hide();
			} else {
				//$("#dirInput").hide();
			}

			//$("#subject").attr("disabled", false);
		
			
		})

		//IE浏览器文件下载路径选择
		function browseFolder() {

			try {
				var Message = "选择文件夹"; //选择框提示信息
				var Shell = new ActiveXObject("Shell.Application");
				//var Folder = Shell.BrowseForFolder(0, Message, 64, 17); //起始目录为：我的电脑
				var Folder = Shell.BrowseForFolder(0, Message, 0); //起始目录为：桌面
				if (Folder != null) {
					var path = Folder.Self.Path; // 返回路径
					$("#filePath").val(path);
					return Folder;
				}
			} catch (e) {
				layer.msg("保存异常：" + e.message);
			}
		}

		function getOs() {
			//如果是edge浏览器，此方法无用
			//Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36 Edge/17.17134
			var OsObject = "";
			if (isIE = navigator.userAgent.indexOf("MSIE") != -1) {
				browseType = "MSIE";
				return "MSIE";
			}
			if (isFirefox = navigator.userAgent.indexOf("Firefox") != -1) {
				browseType = "Firefox";
				return "Firefox";
			}
			if (isChrome = navigator.userAgent.indexOf("Chrome") != -1) {
				browseType = "Chrome";
				return "Chrome";
			}
			if (isSafari = navigator.userAgent.indexOf("Safari") != -1) {
				browseType = "Safari";
				return "Safari";
			}
			if (isOpera = navigator.userAgent.indexOf("Opera") != -1) {
				browseType = "Opera";
				return "Opera";
			}
			if (isIE = navigator.userAgent.indexOf("like") != -1) {
				browseType = "MSIE";
				return "MSIE";
			}
		}

		function checkMac() {
			//mac地址正则表达式 
			var reg_name = /[A-F\d]{2}-[A-F\d]{2}-[A-F\d]{2}-[A-F\d]{2}-[A-F\d]{2}-[A-F\d]{2}/;
			var macAddress = $("#macAddress").val();
			if (!reg_name.test(macAddress)) {
				layer.msg("mac地址格式不正确,mac地址格式为00-24-21-19-BD-E4",{icon:2});
				return false;
			}
			return true;
		}
		

		function create(){
			var macAddress = $("#macAddress").val();
			var notBefore = $("#notBefore").val();
			var notAfter = $("#notAfter").val();
			var licPath = $("#licPath").val();
			var subject = $("#subject").val();
			var issuedTime = $("#issuedTime").val();
		
			if (macAddress=='' || notBefore==''||notAfter==''||licPath==''||subject==''||issuedTime=='') {
				layer.msg("不能为空",{icon:2});
				return
			}
			
			//mac地址正则表达式 
			var reg_name = /[A-F\d]{2}-[A-F\d]{2}-[A-F\d]{2}-[A-F\d]{2}-[A-F\d]{2}-[A-F\d]{2}/;
			if (!reg_name.test(macAddress)) {
				layer.msg("mac地址不正确,mac地址格式为00-24-21-19-BD-E4",{icon:2});
				return
			}
			
			$.ajax({
				async : false,
				cache : false,
				type : 'POST',
				data : {"macAddress":macAddress,"notBefore":notBefore,"notAfter":notAfter,"licPath":licPath,"subject":subject,"issuedTime":issuedTime},
				url : 'LicenseController/create.do',// 请求的action路径
				error : function() {// 请求失败处理函数
					layer.msg('license生成失败!!', {icon: 5});
				},
				success : function(data) {
					if (data.success == true) {
						
						layer.msg('license生成成功!!', {icon: 1});
						
					} else if (data.success == false) {
						
						layer.msg('license生成失败!!' , {icon: 5});
					} 
				}
			});
			
		}
	</script>
</body>
</html>