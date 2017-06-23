<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta charset="utf-8">
<title>幸运大转盘</title>

<meta name="viewport" content="width=device-width,initial-scale=1"/> 
<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/jQueryRotate.2.2.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery.easing.min.js"></script>
<link rel="stylesheet" href="http://code.jquery.com/mobile/1.1.0-rc.2/jquery.mobile-1.1.0-rc.2.min.css"/>
<script src="http://code.jquery.com/mobile/1.1.0-rc.2/jquery.mobile-1.1.0-rc.2.min.js"></script>

<style type="text/css">
#disk{
       width:100%;
       height:417px;
       background:url('http://fzh123.duapp.com/images/disk.jpg')
     }
.demo{
       width:417px;
       height:417px;
       position:relative;
       margin:50px auto
     }
#start{
       width:5px;
       height:150px;
       position:absolute;
       top:20px;
       left:80px
}
#start img{
    cursor:pointer
}
</style>

<script type="text/javascript">
$(function(){
	$("#startbtn").rotate({
		bind:{
			click:function(){
				 var angle = "";//奖项区间角度
				 var rand = Math.floor(Math.random() * 1000);//随机数
	             var num = Math.random() * 27;//Math.random()*(最大值-最小值+1)+最小值
				 //根据随机数确定奖项区间角度
			     if(rand==1){                      //一等奖(1/1000)
					 angle = Math.floor(Math.random() * 29);//[1-29]
				 }else if(rand >=2 && rand<=3){    //二等奖(2/1000)
					 angle = Math.floor(num + 302);         //[302-328]
				 }else if(rand >=4 && rand<=6){    //三等奖(3/1000)
					 angle = Math.floor(num + 242);         //[242-268]
				 }else if(rand >=7 && rand<=10){   //四等奖(4/1000)
					 angle = Math.floor(num + 182);         //[182-208]
				 }else if(rand >=11 && rand<=15){  //五等奖(5/1000)
					 angle = Math.floor(num + 122);         //[122-148]
				 }else if(rand >=16 && rand<=21){  //六等奖(6/1000)
					 angle = Math.floor(num + 62);          //[62-88]
				 }else{                            //七等奖(979/1000)
					 var arr = ["32","92","152","212","272","332"];
					 var index = Math.floor(Math.random()*6);
					 var min = arr[index];
					 var rdAngle = parseInt(num)+parseInt(min);
					 angle = rdAngle;
				 }
				
				 $(this).rotate({
					 	duration:2000,       //转动时间
					 	angle: 0,
            			animateTo:1800+angle,//转动角度
						easing: $.easing.easeOutSine,
						callback: function(){
						    if(angle<30){
							   alert('你中了一等奖!');
							}else if(angle >=302 && angle<=328){
							   alert('你中了二等奖!');
							}else if(angle >=242 && angle<=268){
							   alert('你中了三等奖!');
							}else if(angle >=182 && angle<=208){
							   alert('你中了四等奖!');
							}else if(angle >=122 && angle<=148){
							   alert('你中了五等奖!');
							}else if(angle >=62 && angle<=88){
							   alert('你中了六等奖!');
							}else{
							   alert('你中了七等奖!');
							}
						}
				 });
			}
		}
	});
});
</script>
</head>

<body>
<div data-role="page" class="demo">
    <div data-role="header"><h1>幸运大转盘</h1></div>
    <div data-role="content">
        <div>
            <img width="300px" height="300px" src="http://fzh123.duapp.com/images/disk.jpg">
        </div>
        <div>
            <img width="120px" height="240px" style="position: absolute;left: 107px;top: 88px" src="http://fzh123.duapp.com/images/start.png" id="startbtn">
        </div>
                         如点击"开始抽奖"转盘不转,请刷新再试!
    </div>
    <div data-role="footer">
        <h4>Copyright © 2013 fzhwx开发者</h4>
    </div>
</div>

<!-- <div id="main">
   <div class="demo">
        <div id="disk"></div>
        <div id="start"><img src="http://fzh123.duapp.com/images/start.png" id="startbtn"></div>
   </div>
</div> -->

</body>
</html>