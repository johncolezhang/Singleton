<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="css/easyui.css">
<link rel="stylesheet" type="text/css" href="css/icon.css">

<script type="text/javascript" src="jQuery/jquery.min.js"></script>
<script type="text/javascript" src="jQuery/jquery.easyui.min.js"></script>
<script type="text/javascript" src="jQuery/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="jQuery/datagrid-cellediting.js"></script>
</head>

<script type="text/javascript">

	$(function() {

		var dg = $('#dg')
				.datagrid(
						{
							border : false,
							fitColumns : true,    //自适应列的宽度
							rownumbers : true,    //显示带有行号的列
							nowrap : false,       //不换行，把数据显示在一行里,设置为false即换行
							toolbar : '#tb',             //数据网格（datagrid）面板的头部工具栏
							pagination : true,           //在数据网格（datagrid）底部显示分页工具栏
							singleSelect : true,         //设置为 true，则只允许选中一行
							url : "${pageContext.request.contextPath}/index/getGwnUser.do",
							columns : [ [ {
								field : 'id',
								hidden : true,
							}, {
								width : 5,
								field : 'name',
								title : '用户名',
								editor : 'text'    //当是字符串（string）时则指编辑类型
							}, {
								width : 5,
								field : 'pwd',
								title : '密码',
							}, {
								width : 5,
								field : 'orgId',
								title : '用户级别',
								editor : 'text'
							} ] ],
							onClickCell : onClickCell,   //当用户单击一个单元格时触发
							onEndEdit : onEndEdit,       //结束编辑时触发
							
							//当用户完成编辑一行时触发，参数包括：rowIndex：编辑行的索引，从 0 开始      rowData：编辑行对应的记录      changes：更改的字段/值对
							onAfterEdit : function(rowIndex, rowData, changes) {
								flag = true;
								//alert('22211113333333');
								$.ajax({
											dataType : "json",
											type : "POST",
											url : "${pageContext.request.contextPath}/user/saveEditUser.do",
											data : {
												id : rowData.id,
												name : rowData.name,
												pwd : rowData.pwd,
												orgId : rowData.orgId,
											},
											success : function(result) {
											},
											error : function(){
												alert("错误");
											}
										});
							}
						});
	});

	var flag = true;
	var editIndex = undefined;
	
	function endEditing() {
		if (flag) {
			if (editIndex == undefined) {
				return true
			}
			//验证指定的行，有效时返回 true
			//结束对一行进行编辑
			if ($('#dg').datagrid('validateRow', editIndex)) 
			{
				$('#dg').datagrid('endEdit', editIndex);
				editIndex = undefined;
				return true;
			} 
			else {
				return false;
			}
		}
	}
	
	function onEndEdit(index, row) {
		//获取指定的编辑器
		var ed = $(this).datagrid('getEditor', {
			index : index,
			field : 'productid'
		});
	}
	
	function onClickCell(index, field) {
		if (editIndex != index) {
			if (endEditing()) {
				$('#dg').datagrid('selectRow', index).datagrid('beginEdit',index);
				var ed = $('#dg').datagrid('getEditor', {
					index : index,
					field : field
				});
				if (ed) {
					($(ed.target).data('textbox') ? $(ed.target).textbox('textbox') : $(ed.target)).focus();
				}
				editIndex = index;
			} else {
				setTimeout(function() {
					$('#dg').datagrid('selectRow', editIndex);
				}, 0);
			}
		}
	}

	function append() {
		if (endEditing()) {
			//追加一个新行。新的行将被添加在最后的位置
			$('#dg').datagrid('appendRow', {
				status : 'P'
				}
			);
			flag = false;
			editIndex = $('#dg').datagrid('getRows').length - 1;
			//选中一行，行索引从 0 开始，所以上面要减1，然后开始对一行进行编辑
			$('#dg').datagrid('selectRow', editIndex).datagrid('beginEdit',editIndex);
		}
	}

	function accept() {
		if (editIndex == undefined) {
			return true
		}
		//验证指定的行，有效时返回 true
		if ($('#dg').datagrid('validateRow', editIndex)) 
		{
			$('#dg').datagrid('endEdit', editIndex);
			editIndex = undefined;
			return true;
		} else {
			return false;
		}
	}

	
	function removeit() {
		if (editIndex == undefined) {
			return
		}
		//删除时先获取选择行
		var rows = $('#dg').datagrid("getSelections");
		//选择要删除的行
		if (rows.length > 0) {
			$.messager.confirm("提示","你确定要删除吗?",
							function(r) {
								if (r) {
									//将选择到的行存入数组并用,分隔转换成字符串，
									//删除
									$
											.post(
													"${pageContext.request.contextPath}/user/deleteUser.do",
													{
														id : rows[0].id,
													},
													function(result) {
														var flag = true;
														if (result > 0) {
															$('#dg')
																	.datagrid(
																			'cancelEdit',
																			editIndex)
																	.datagrid(
																			'deleteRow',
																			editIndex);
															editIndex = undefined;
															$.messager
																	.alert(
																			"系统提示",
																			"您已成功删除<font color=red>"
																					+ result
																					+ "</font>条数据");
															$("#dg").datagrid(
																	"reload",
																	{});
														} 
													}, "json");
								}
							});
		} else {
			$.messager.alert("提示", "请选择要删除的行", "error");
		}
	}
</script>
<body>
	<div id="dg"></div>
	<div id="tb" style="height: auto">
		<a href="javascript:void(0)" class="easyui-linkbutton"
			data-options="iconCls:'icon-add',plain:true" onclick="append()">添加</a>
		<a href="javascript:void(0)" class="easyui-linkbutton"
			data-options="iconCls:'icon-save',plain:true" onclick="accept()">保存</a><a
			href="javascript:void(0)" class="easyui-linkbutton"
			data-options="iconCls:'icon-remove',plain:true" onclick="removeit()">删除</a>
	</div>
</body>
</html>