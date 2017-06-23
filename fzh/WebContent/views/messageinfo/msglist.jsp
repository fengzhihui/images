<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/global.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>fzhwx开发者-公众号管理平台</title>
<link rel="shortcut icon"
	href="${path}/images/wx/wally.jpg" />
<meta content="IE=edge,chrome=1" http-equiv="X-UA-Compatible">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">
<link rel="stylesheet" type="text/css"
	href="${path}/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${path}/css/theme.css">
<link rel="stylesheet"
	href="${path}/lib/font-awesome/css/font-awesome.css">
<script type="text/javascript" src="${path}/lib/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${path}/js/bootstrap.js"></script>
<script src="${path}/js/highcharts.js"></script>

<script type="text/javascript">
//报表
$(function () {
    $('#container').highcharts({
        chart: {
            type: 'area'
        },
		//图例
		legend: {
		   enabled: true, //是否显示图例
           backgroundColor: '#FCFFC5'
        },
        title: {
            text: '消息交互统计图'
        },
        /**subtitle: {
            text: '来源: <a href="#">'+
                '广汇东湖城</a>'
        }, */
        xAxis: {
        	categories: [ ${data.msgDate} ]
        },
        yAxis: {
            title: {
                text: '消息交互趋势图'
            },
            labels: {
                formatter: function() {
                    return this.value / 1;
                }
            }
        },
        tooltip: {
            pointFormat: '{series.name} <b>{point.y:,.0f}</b><br/>',// {point.x}
			crosshairs:[
			  //横坐标
			  {width:1,color:'green',dashStyle:'shortdot'},
			  //纵坐标
			  {width:1,color:'red',dashStyle:'shortdot'}
			]
        },
        plotOptions: {
		    series: {
			    //threshold: 10000, //阀值
				lineColor: 'red',
				fillOpacity: 0.2 //填充透明度
				//lineWidth: 3, //轮廓线宽
                //fillColor: 'green',
			/**fillColor: {
                linearGradient: [0, 0, 0, 300],
                stops: [
                    [0, 'rgb(69, 114, 167)'],
                    [1, 'rgba(2,0,0,0)']
                ]
            },**/
			},
            area: {
			    //fillOpacity: 0.2, //填充透明度
                //pointStart: 10,
				//lineColor: 'green',
				//fillColor: 'red',
				//标记点
                marker: {
                    enabled: true,
                    symbol: 'circle',
                    radius: 3,
                    states: {
                        hover: {
                            enabled: true
                        }
                    }
                }
            }
        },
        series: 
        	[{
                data: [ ${data.usrSum} ],
                //step: 'right',//left,right,center,true,默认false
                lineColor: 'blue',
                name: '消息发送人数'
             },
            {
                data: [ ${data.msgSum} ],
                //color: 'red',//阴影
                //step: 'right',//left,right,center,true,默认false
                lineColor: 'gray',
                name: '消息发送次数'
            },
            {
                data: [ ${data.msgAvg} ],
                lineColor: 'green',
                name: '人均发送次数'
            }]
    });
});

function query(){
	document.formList.action = '${path}/messageinfo/calcUserMsg';
	document.formList.submit();
}

//下拉框保值
$(function(){
     var dt = $('#dt').val();
     $("#date").attr("value", dt);
     var slt = $("#date").get(0);
     if(dt !=null && dt.length !=0){
     	slt[dt-1].selected = true;
     }
});
</script>
</head>
<body class="">
	<jsp:include page="/common/header.jsp" flush="true" />
	<jsp:include page="/common/leftbar.jsp" flush="true" />
	<div class="content">
		<div class="container-fluid">
			<form key="formList" cssClass="form-horizontal" name="formList"
				theme="simple" method="POST">
				<fieldset>
					<legend>消息发送统计列表</legend>
				</fieldset>
				<div class="control-group">
					日期 <input id="dt" type="hidden" value="${date}" />
					<select name="date" id="date">
						<option value="1">按日</option>
						<option value="2">按周</option>
						<option value="3">按月</option>
					</select>
					<button type="button" class="btn" onclick="query();">查询</button>
				</div>

				<table class="table table-bordered table-hover table-condensed">
					<thead>
						<tr class="thead-text">
							<th style="text-align: center"><a href="javascript:void(0);">统计时间</a>
							</th>
							<th style="text-align: center"><a href="javascript:void(0);">活跃用户数</a>
							</th>
							<th style="text-align: center"><a href="javascript:void(0);">交互消息总量</a>
							</th>
							<th style="text-align: center"><a href="javascript:void(0);">人均消息交互量</a>
							</th>
						</tr>
					</thead>
					<c:if test="${fn:length(data.list)==0}">
						<tr>
							<td colspan="4" style="text-align:center">未查询到相关记录！</td>
						</tr>
					</c:if>
					<c:forEach items="${data.list}" var="entry">
						<tr>
							<td style="text-align: center">${entry[2]}</td>
							<td style="text-align: center">${entry[0]}</td>
							<td style="text-align: center">${entry[1]}</td>
							<td style="text-align: center">${entry[3]}</td>
						</tr>
					</c:forEach>
				</table>
			</form>
			<div id="container"
				style="min-width: 310px; height: 400px; margin: 0 auto"></div>
		</div>
	</div>
	<jsp:include page="/common/footer.jsp" flush="true" />
</body>
</html>
