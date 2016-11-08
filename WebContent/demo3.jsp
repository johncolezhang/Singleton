<html>
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" type="text/css" href="css/easyui.css">
	<link rel="stylesheet" type="text/css" href="css/icon.css">
	<link rel="stylesheet" type="text/css" href="css/demo.css">
	
	<script type="text/javascript" src="jQuery/jquery.min.js"></script>
	<script type="text/javascript" src="jQuery/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="echarts/echarts.js"></script>
	<script type="text/javascript" src="echarts/linechart.js"></script>
	<script type="text/javascript" src="js/demo3.js"></script>
</head>

<body>
	<!--布局div-->
	<div class="easyui-layout" style="width:100%;height:100%;">
		<!--左边数据显示div-->
		<div data-options="region:'west'" title="南山医院数据指标" style="width:50%;padding:0px">
			<table id="dg"class="easyui-treegrid" style="width:700px;height:99%" data-options="border:false,fit:true,rownumbers:false,singleSelect:true,method:'get',toolbar:'#ft'"></table>
		</div>
		<div class="div-right" data-options="region:'center'" title="图像分析"></div>
	</div>
	<div id="ft" style="padding:2px 5px;">
		频率：
		<select id="sel_rate" name="sel_rate" class="easyui-combobox" style="width:70px"></select>
		时间：
		<input id="sel_time_date" name="sel_time_date" class="easyui-datebox" style="width:110px" />
		<select id="sel_time_ym" name="sel_time_ym" class="easyui-combobox" style="width:100px"></select>
		科室：
		<select id="sel_area" name="sel_area" class="easyui-combotree" style="width:150px;"></select>
		<button id="button" type="button" class="easyui-linkbutton" iconCls="icon-search">查询</button>
	</div>
	<script type="text/javascript">
	function mkchart(variable){
		var vararray = variable.split('@');
		alert(vararray[0]);
		//ajax读取图纵坐标
		$.ajax({
			url : "LineChartYServlet?variable="+vararray[0],
			dataType:"text",
			success:function(data2){
				dy = data2.split(',');
				//var item = {data:dy};
				var flag = false;
				
				for(var i=0; i<dy.length; i++){
					if(dy[i] != '0'){
						flag = true;
					}
				}
				if (flag) {
					lineoption.series[0].data = dy;
				} else {
					lineoption.series[0].data = null;
				}
				//alert(data2+" "+dy+" "+dy[0]);
				//ajax读取图横坐标
				$.ajax({
					url : "LineChartXServlet?variable="+vararray[0],
					dataType:"text",
					success:function(data){
						var dx = data.split(',');
						lineoption.xAxis.data = dx;
						//alert(data+" "+dx+" "+dx[0]);
						//加载数据
						var vars = vararray[1].split(',');
						lineoption.title.text = vars[2]+" "+vars[3]+" "+vars[1]+" 折线图";
						var myChart = echarts.init($('.div-right').get(0));
						myChart.setOption(lineoption);
					},
					error:function(){
						alert("error");
					}
				})
			},
			error:function(){
				alert("error");
			}
		})
	}
	</script>
	
</body>
</html>