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
	<script type="text/javascript" src="jQuery/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="echarts/echarts.js"></script>
	<script type="text/javascript" src="echarts/linechart.js"></script>
	<script>var __contextpath='${pageContext.request.contextPath}';</script>
	<script type="text/javascript" src="js/line.js"></script>
	<title>lineChart</title>
	</head>
<body>

	<!--布局div-->
	<div class="easyui-layout" data-options="border:false,fit:true" style="width:100%;height:100%">
		<div data-options="region:'west'" style="width:40%;height:100%">
		
			<!-- 嵌套布局div -->
			<div class="easyui-layout" style="width:100%;height:100%;">
			
				<!--左上选择指标维度div-->
				<div data-options="region:'north'" style="width:50%;height:40%">
				
					<!-- Kpi标签页 -->
					<div id="line_tabs" class="easyui-tabs" data-options="border:false,fit:true,tools:'#line_tab_tools'"></div>
					
					<!-- 标签页工具栏  -->
					<div id="line_tab_tools">
						<a href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true" onclick="$('#line_window_kpi').window('open')">添加Kpi</a>
					</div>
				</div>
				
				<!-- 左中选择变量生成折线图div -->
				<div data-options="region:'center'" style="width:50%;height:30%">
					<div id="line_div_var_dim" style="padding:20px">
						选择做横轴的维度：
						<select id="line_var_dim" class="easyui-combobox" style="width:100px"></select>
					</div>
					<div id="line_div_var_custom" style="padding:20px">
						起始时间：
						<div id="line_div_time_ym_s" style="padding:20px">
							<select id="line_time_ym_s" class="easyui-combobox" style="width:100px"></select>
							<input id="line_time_date_s" class="easyui-datebox" style="width:110px" />
						</div>
					</div>
					<div style="padding:0px 20px">
						<button id="line_button_makechart" class="easyui-linkbutton">生成图形</button>
					</div>
				</div>
				
				<!-- 左下图形参数设置div -->
				<div data-options="region:'south'" style="width:50%;height:30%">
					<div id="line_div_chart_var" style="padding:20px">
						折线图标题：
						<input id="line_text_title" class="easyui-textbox" data-options="prompt:'请输入标题'" style="width:50%;height:32px" />
					</div>
				</div>
			</div>
		</div>
		
		<!-- 右边显示饼状图div -->
		<div id="line_div_chart" data-options="region:'center'" style="width:60%;height:100%;padding:0px">
			
		</div>
	</div>
	
	<!-- 选择kpi窗口 -->
	<div id="line_window_kpi" class="easyui-window">
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'center',border:false" style="padding:10px;">
				<div id="line_tab_kpi" class="easyui-tabs" data-options="fit:true">
					<div title="全部" style="padding:10px">
						<ul id="line_tree_kpi" class="easyui-tree"></ul>
					</div>
					<div title="搜索" style="padding:10px">
						<table id="line_dg_kpi"class="easyui-datagrid" data-options="toolbar:'#search_kpi'">
							<thead>
								<tr>
									<th data-options="field:'ck',checkbox:true"></th>
									<th data-options="field:'value',width:80,hidden:true">ID</th>
									<th data-options="field:'text',width:180">名称</th>
								</tr>
							</thead>
						</table>
					</div>
				</div>
			</div>
			<div id="line_win_tool" data-options="region:'south',border:false" style="text-align:right;padding:5px 0 0;">
				<a class="easyui-linkbutton" data-options="iconCls:'icon-ok'" href="javascript:void(0)" style="width:80px">选择</a>
				<a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0)" style="width:80px">关闭</a>
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
				$('#line_dg_kpi').datagrid('loadData', data);
			},
			error : function() {
				alert("查不到输入的指标");
			}
		})
	}
	
	function removePanel(){
		var tab = $('#line_tabs').tabs('getSelected');
		if (tab){
			var index = $('#line_tabs').tabs('getTabIndex', tab);
			$('#line_tabs').tabs('close', index);
		}
	}
	</script>
</body>
</html>