<%@ page contentType="text/html;charset=utf-8" %>
<html>
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" type="text/css" href="css/easyui.css">
	<link rel="stylesheet" type="text/css" href="css/icon.css">
	<link rel="stylesheet" type="text/css" href="css/demo.css">
	<link rel="stylesheet" type="text/css" href="css/myicon.css">
	
	<script type="text/javascript" src="jQuery/jquery.min.js"></script>
	<script type="text/javascript" src="jQuery/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="js/Orgtree.js"></script>
	
</head>
<body style="padding:0px">
    <div class="easyui-layout" data-options="fit:'true'">
		<!-- 左边布局 -->
		<div id="p" data-options="region:'west',split:true" title=" " style="width:20%;">
			<div class="easyui-panel" style="padding:5px;border:0px;">
				<ul id="org_tree" class="easyui-tree" data-options="method:'get',animate:true,">
                </ul>
			 </div>
		</div>
		
		<!-- 右边布局 -->
		<div data-options="region:'center'" title=" " style="width:80%;">
		</div>
		
	</div>



<!-- 创建节点起名对话框 -->
	<div id="dlg_menuname" class="easyui-dialog" style="padding:5px;width:320px;height:160px"
			title="创建新目录" iconCls="icon-ok" closed="true"
			left="600" top="280"
			buttons="#dlg-menubuttons">
		<label for="name">请输入名称：</label>
		<input id="setmenuname" type="text" class="easyui-validatebox" value="" /><br/>
	</div>

	<div id="dlg-menubuttons">
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick=append_set_menunode()>确定</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick=cancel_menudlg()>取消</a>
	</div>
	
	<div id="dlg_childname" class="easyui-dialog" style="padding:5px;width:320px;height:160px"
			title="创建新图表" iconCls="icon-ok" closed="true"
			left="600" top="280"
			buttons="#dlg-childbuttons">
		<label for="name">请输入名称：</label>
		<input id="setchildname" type="text" class="easyui-validatebox" value="" /><br/>
	</div>

	<div id="dlg-childbuttons">
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick=append_set_childnode()>确定</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick=cancel_childdlg()>取消</a>
	</div>
			
			
			
			<!-- 目录右键菜单 -->
			<div id="mm" class="easyui-menu" style="width:120px;">
			    <div onclick="addnode_disdlg()" data-options="iconCls:'icon-save'">创建目录</div>
				<div data-options="iconCls:'icon-save'">
					<span>创建图形</span>
					<div style="width:150px;">
						<div onclick="addnode_childdlg()">创建饼图</div>
						<div onclick="append2()">创建折线图</div>
						<div onclick="append3()">创建柱状图</div>
					</div>
				</div>
				<div onclick="changename()" data-options="iconCls:'icon-print'">重命名</div>
				<div onclick="remove_nullcatalog()" data-options="iconCls:'icon-print'">删除空目录</div>
			</div>
		
		
		<!-- 子节点右键菜单 -->
			<div id="nn" class="easyui-menu" style="width:120px;">
				<div onclick=""data-options="iconCls:'icon-save'">编辑图形</div>
				<div onclick="changename()" data-options="iconCls:'icon-print'">重命名</div>
				<div onclick="remove_node()" data-options="iconCls:'icon-print'">删除</div>
			</div>
		
	
</body>
</html>