<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" type="text/css" href="css/easyui.css">
	<link rel="stylesheet" type="text/css" href="css/icon.css">
	<link rel="stylesheet" type="text/css" href="css/demo.css">
	
	<script type="text/javascript" src="jQuery/jquery.min.js"></script>
	<script type="text/javascript" src="jQuery/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="echarts/echarts.js"></script>
	<script type="text/javascript" src="echarts/piecharts.js"></script>
	<script type="text/javascript" src="echarts/linechart.js"></script>
	<script>var __contextpath='${pageContext.request.contextPath}';</script>
	<script type="text/javascript" src="js/piechart.js"></script>
<title>pie_chart</title>
</head>
<body>
	<!--布局div-->
	<div class="easyui-layout" data-options="border:false,fit:true" style="width:100%;height:100%">
		<div data-options="region:'west'" style="width:40%;height:100%">
			<!-- 嵌套布局div -->
			<div class="easyui-layout" style="width:100%;height:100%;">
				<!--左上选择指标维度div-->
				<div data-options="region:'north'" style="width:50%;height:40%;padding:20px">
					数据选择：
					<div id="pie_div_kpi" style="width:100%;height:15%;padding:20px">
						图形类型：
						<select id="pie_sel_charttype" name="pie_sel_rate" class="easyui-combobox" style="width:100px"></select>
						Kpi：
						<button id="pie_button_kpi" class="easyui-linkbutton" onclick="$('#window_kpi').window('open')">选择KPI</button>
						<!--button id="ccc" class="easyui-linkbutton">check</button-->
					</div>
					<!-- 时间维度div -->
					<div id="pie_div_dim1" style="width:50%;height:15%;padding:20px">
						频率：
						<select id="pie_sel_rate" name="pie_sel_rate" class="easyui-combobox" style="width:70px"></select>
						时间：
						<input id="pie_sel_time_date" name="pie_sel_time_date" class="easyui-datebox" style="width:110px" />
						<select id="pie_sel_time_ym" name="pie_sel_time_ym" class="easyui-combobox" style="width:100px"></select>
					</div>
					<div id="pie_div_dims"></div>
				</div>
				<!-- 左中选择变量生成饼状图div -->
				<div data-options="region:'center'" style="width:50%;height:30% ;padding:20px">
					<div id="pie_div_chart_dims" style="width:50%;height:15% ;padding:20px">
						<p id="pie_p_var">选择占比的维度</p>
						<select id="pie_sel_dim_chart" name="pie_sel_time_ym" class="easyui-combobox" style="width:100px"></select>
					</div>
					<div id="pie_div_chart_dim1" style="height:15% ;padding:20px">
						起始时间：
						<div id="pie_div_ym">
							<select id="pie_sel_time_ym_f" name="pie_sel_time_ym_f" class="easyui-combobox" style="width:100px"></select>
						</div>
						<div id="pie_div_date">
							<input id="pie_sel_time_date_f" name="pie_sel_time_date_f" class="easyui-datebox" style="width:110px" />
						</div>
					</div>
					<div style="height:15% ;padding:20px">
						<button id="button_makechart" class="easyui-linkbutton">生成图形</button>
					</div>
				</div>
				<!-- 左下图形参数设置div -->
				<div data-options="region:'south'" style="width:50%;height:30%;padding:20px">
					图形参数选择：
					<div id="line_div_3">
					
					</div>
					<div id="pie_div_3">
						<div id="pie_div_var" style="padding:20px">
							<p id="pie_p_title">饼状图标题：</p>
							<input id="pie_text_title" class="easyui-textbox" data-options="prompt:'输入标题'" style="width:50%;height:32px" />
						</div>
						<!-- 玫瑰图的div -->
						<div id="pie_div_isrose" style="width:50%;height:15%;padding:20px">
							是否为玫瑰图：
							<input type="radio" name="isrose" value="false" />否
							<input type="radio" name="isrose" value="true"/>是
						</div>
						<!-- 半径的div -->
						<div id="pie_div_radius1" style="width:100%;height:15%;padding:20px">
							选择内半径：
							<select id="pie_sel_inner_radius" name="pie_sel_inner_radius" class="easyui-combobox" style="width:70px"></select>
							选择外半径：
							<select id="pie_sel_outer_radius" name="pie_sel_outer_radius" class="easyui-combobox" style="width:70px"></select>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- 右边显示饼状图div -->
		<div id="div_chart" data-options="region:'center'" style="width:60%;height:100%;padding:0px">
			
		</div>
	</div>
			<!-- 选择kpi窗口 -->
			<div id="window_kpi" class="easyui-window">
				<div class="easyui-layout" data-options="fit:true">
					<div data-options="region:'center',border:false" style="padding:10px;">
						<div id="tab_kpi" class="easyui-tabs" data-options="fit:true">
							<div title="全部" style="padding:10px">
								<ul id="tree_kpi" class="easyui-tree"></ul>
							</div>
							<div title="搜索" style="padding:10px">
								<table id="db_kpi"class="easyui-datagrid" style="width:700px;height:99%" data-options="border:false,fit:true,rownumbers:true,singleSelect:true,method:'get',toolbar:'#search_kpi'">
									<thead>
										<tr>
											<th data-options="field:'value',width:80,hidden:true">ID</th>
											<th data-options="field:'text',width:180">名称</th>
										</tr>
									</thead>
								</table>
							</div>
						</div>
					</div>
					<div data-options="region:'south',border:false" style="text-align:right;padding:5px 0 0;">
						<a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:void(0)" onclick="choose()" style="width:80px">选择</a>
						<a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" onclick="$('#window_kpi').window('close')" style="width:80px">关闭</a>
					</div>
				</div>
			</div>
			<div id="search_kpi" style="padding:2px 5px;">
				<input class="easyui-searchbox" data-options="prompt:'请输入要查找的指标',searcher:doSearch,fit:true" style="width:200px"></input>
			</div>
	<script type="text/javascript">
		function doSearch(value) {
			$.ajax({
				url : "${pageContext.request.contextPath}/main/searchKpi.do?variable="+value,
				dataType : "json",
				success : function(data) {
					$('#db_kpi').datagrid('loadData', data);
				},
				error : function() {
					alert("查不到输入的指标");
				}
			})
		}
		
		var kpi_id = "";
		
		function choose(){
			//alert($('.tabs-selected').text());
			if ($('.tabs-selected').text() == "全部") {
				kpi_id = $('#tree_kpi').tree('getSelected').id;
				var kpi_text =$('#tree_kpi').tree('getSelected').text;
				$('#pie_div_chart_dims').show();
				$.ajax({
					url : "${pageContext.request.contextPath}/main/getDimValue.do?kpi_id="+kpi_id,
					dataType : "text",
					success : function(data) {
						var array = data.split("@");
						var jdata = JSON.parse(array[1]);
						$('#pie_sel_dim_chart').combobox('loadData', jdata);
						var dims = array[0].split(",");
						for(var i=0; i<dims.length; i++){
							//说明该指标包含时间维度
							if(dims[i] == "1"){
								$('#pie_div_dim1').show();
							}else if(dims[i] == "2"){
								$('#pie_div_dims').append('<div id="pie_div_dim2" style="width:50%;height:15%;padding:20px">科室：<select id="pie_dim2" class="easyui-combotree" style="width:150px;"></select></div>');
							}else if(dims[i] == "3"){
								$('#pie_div_dims').append('<div id="pie_div_dim3" style="width:50%;height:15%;padding:20px">地区：<select id="pie_dim3" class="easyui-combotree" style="width:150px;"></select></div>');
							}else if(dims[i] == "4"){
								$('#pie_div_dims').append('<div id="pie_div_dim4" style="width:50%;height:15%;padding:20px">品牌：<select id="pie_dim4" class="easyui-combobox" style="width:150px;"></select></div>');
							}
						}
						//加载动态生成的html组件
						for(var i=0; i<dims.length; i++){
							if(dims[i] == "2"){
								$.ajax({
									url : "${pageContext.request.contextPath}/main/getDimComboTree.do?dim_id=2",
									dataType : "json",
									success : function(dim_data){
										$('#pie_dim2').combotree('loadData', dim_data);
									},
									error : function(){
										alert("dim2加载出错");
									}
								})
							}else if(dims[i] == "3"){
								$.ajax({
									url : "${pageContext.request.contextPath}/main/getDimComboTree.do?dim_id=3",
									dataType : "json",
									success : function(dim_data){
										$('#pie_dim3').combotree('loadData', dim_data);
									},
									error : function(){
										alert("dim3加载出错");
									}
								})
							}else if(dims[i] == "4"){
								$.ajax({
									url : "${pageContext.request.contextPath}/main/getDimComboTree.do?dim_id=4",
									dataType : "json",
									success : function(dim_data){
										$('#pie_dim4').combobox('loadData', dim_data);
									},
									error : function(){
										alert("dim4加载出错");
									}
								})
							}
						}
						$('#pie_button_kpi').html(kpi_text);
					}
				})
			} else {
				//alert($('#db_kpi').datagrid('getSelected').value);
				kpi_id = $('#db_kpi').datagrid('getSelected').value;
				var kpi_text=$('#db_kpi').datagrid('getSelected').text;
				$('#pie_div_chart_dims').show();
				$.ajax({
					url : "GetDimValueServlet?variables="+kpi_id+"@0",
					dataType : "text",
					success : function(data) {
						var dims = data.split(",");
						for(var i=0; i<dims.length; i++){
							//说明该指标包含时间维度
							if(dims[i] == "1"){
								$('#pie_div_dim1').show();
							}
							else if(dims[i] == "2"){
								$('#pie_div_dim2').show();
							}
						}
						$('#pie_button_kpi').html(kpi_text);
					}
				})
				$.ajax({
					url : "GetDimValueServlet?variables="+kpi_id+"@1",
					dataType : "json",
					success : function(data) {
						$('#pie_sel_dim_chart').combobox('loadData', data);
						//有时间变量
						if($('#pie_sel_dim_chart').combobox('getValue') == "1"){
							$('#pie_div_chart_dim1').show();
							if($('#pie_sel_rate').combobox('getValue') != "date"){
								$('#pie_div_ym').show();
								$('#pie_div_date').hide();
							}else{
								$('#pie_div_date').show();
								$('#pie_div_ym').hide();
							}
						}
					}
				})
			}
			//$('#pie_div_var').show();
			//$('#pie_div_isrose').show();
			//$('#pie_div_radius').show();
			$('#window_kpi').window('close');
		}
	</script>
</body>
</html>