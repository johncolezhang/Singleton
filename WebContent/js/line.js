$(function(){
	////////////////////////////////js本地变量数据
	//时间频度数据
	var options_rate = [ {
		text : "年份",
		value : "year"
	}, {
		text : "月份",
		value : "month"
	}, {
		text : "日期",
		value : "day"
	} ];
	
	// 年数据
	var options_time_year = [ {
		text : "2015年",
		value : "2015"
	}, {
		text : "2016年",
		value : "2016"
	} ];

	// 月数据
	var options_time_month = [ {
		text : "2015年6月",
		value : "201506"
	}, {
		text : "2015年7月",
		value : "201507"
	}, {
		text : "2015年8月",
		value : "201508"
	}, {
		text : "2015年9月",
		value : "201509"
	}, {
		text : "2015年10月",
		value : "201510"
	}, {
		text : "2015年11月",
		value : "201511"
	}, {
		text : "2015年12月",
		value : "201512"
	}, {
		text : "2016年1月",
		value : "201601"
	}, {
		text : "2016年2月",
		value : "201602"
	}, {
		text : "2016年3月",
		value : "201603"
	}, {
		text : "2016年4月",
		value : "201604"
	}, {
		text : "2016年5月",
		value : "201605"
	}, {
		text : "2016年6月",
		value : "201606"
	}, {
		text : "2016年7月",
		value : "201607"
	}, {
		text : "2016年8月",
		value : "201608"
	}, {
		text : "2016年9月",
		value : "201609"
	}, {
		text : "2016年10月",
		value : "201610"
	} ];
	
	//数据展示类型
	var options_charttype = [ {
		text : "折线",
		value : "line"
	}, {
		text : "柱状",
		value : "bar"
	} ];
	
	////////////////////////////////第一次加载页面各个组件的显示或隐藏
	$('#line_div_var_custom').hide();//隐藏起始时间下拉框
	$('#line_div_var_dim').hide();//隐藏选择横轴维度下拉框
	$('#line_time_date_s').next(".combo").hide();//隐藏选择起始日期组件
	$('#line_button_makechart').hide();//隐藏生成图表按钮
	$('#line_div_chart_var').hide();//隐藏图形参数设置div
	
	////////////////////////////////第一次打开页面加载缓存
	$.ajax({
		url : __contextpath+"/main/init.do",
		error : function(){
			alert("加载缓存失败");
		}
	})
	
	////////////////////////////////窗口属性 事件 及子组件属性
	// 设置窗口属性
	$('#line_window_kpi').window({
		title : 'Select_Kpi',
		modal : true,
		shadow : true,
		closed : true,
		iconCls : 'icon-search',
		width : 300,
		height : 400,
		padding : 10,
		minimizable : false,
		maximizable : false,
		top : ($(window).height() - 300) * 0.5,
		left : ($(window).width() - 400) * 0.5,
		
		onOpen : function(){
			// 加载窗口树
			$.ajax({
				url : __contextpath+"/main/selectKpi.do",
				dataType : "json",
				success : function(data) {
					$('#line_tree_kpi').tree('loadData', data);
				},
				error : function() {
					alert("树有问题");
				}
			})
			// 加载窗口表格
			$.ajax({
				url : __contextpath+"/main/searchKpi.do",
				dataType : "json",
				success : function(data) {
					$('#line_dg_kpi').datagrid('loadData', data);
				},
				error : function() {
					alert("表有问题");
				}
			})
		},
	})
	
	//窗口点击选择事件
	$('#line_win_tool').children(':first').click(function () {
		//定义变量存放用户在小窗口选择的Kpi_id和Kpi_name
		var text = '';
		var id = '';
		//定义变量存放用户选择的Kpi节点node
		var nodes;
		//删除所有当前存在的Kpi标签页
		var tabs = $("#line_tabs").tabs("tabs");
		var length = tabs.length;
		for(var i = 0; i < length; i++) {
		    var onetab = tabs[0];
		    var title = onetab.panel('options').tab.text();
		    $("#line_tabs").tabs("close", title);
		}
		//找到当前小窗口中选中的标签 全部 搜索
		var selValue = $('#line_tab_kpi').find('.tabs-selected').text();
		if (selValue == "全部") {
			//在全部标签页的树中选择Kpi
			nodes = $('#line_tree_kpi').tree('getChecked');
			for(var i=0; i<nodes.length; i++){
				if (text != '')
					text += ',';
				text += nodes[i].text;
				if (id != '')
					id += ',';
				id += nodes[i].id;
			}
		} else if (selValue == "搜索"){
			//在搜索标签页中选择Kpi
			nodes = $('#line_dg_kpi').datagrid('getChecked');
			text = '';
			id = '';
			for(var i=0; i<nodes.length; i++){
				if (text != '')
					text += ',';
				text += nodes[i].text;
				if (id != '')
					id += ',';
				id += nodes[i].id;
			}
		}
		
		//关闭选择窗口
		$('#line_tab_tools').find('a').html("编辑Kpi");
		$('#line_window_kpi').window('close');
		
		//ajax加载每个Kpi标签页数据
		$.ajax({
			url : __contextpath+"/main/getKpiTabs.do?Kpi_id="+id,
			dataType : "text",
			nodes : nodes,
			success : function(data){
				//获取没一个Kpi对应的dim信息
				var kpi_dims_acc = data.split("/");
				
				
				
				
				//用户只选择了一个Kpi
				if(nodes.length == 1){//如果只有一个Kpi 则没有公共dim标签页
					var kpi_dims = kpi_dims_acc[0].split("@");
					//存放当前新增标签也的kpi_id
					var kpi_id = nodes[0].id;
					//生成标签页
					$('#line_tabs').tabs('add',{
						title : nodes[0].text,
						content : function(){
							//存放html代码
							var con = "";
							//循环读取每一个Kpi的dim
							for(var j=0; j<kpi_dims.length; j++){
								var dim = kpi_dims[j].split(",");
								var dim_id = dim[0];
								var dim_name = dim[1];
								var dim_type = dim[2];
								//判断dim类型
								if(dim_type == "0"){//时间类型
									con += "<div id='line_div_"+kpi_id+"_"+dim_id+"_"+dim_type+"' style='padding:20px'>频率：<select id='line_rate' class='easyui-combobox' style='width:70px;'></select>"+dim_name+":<input id='line_time_date' class='easyui-datebox' style='width:120px;'/><select id='line_time_ym' class='easyui-combobox' style='width:100px;'></select></div>";
								} else if(dim_type == "1"){//树类型
									con += '<div id="line_div_'+kpi_id+"_"+dim_id+'_'+dim_type+'" style="padding:20px">'+dim_name+'：<select id="line_'+kpi_id+"_"+dim_id+'" class="easyui-combotree" style="width:150px;"></select></div>';
								} else if(dim_type == "2"){//列表类型
									con += '<div id="line_div_'+kpi_id+"_"+dim_id+'_'+dim_type+'" style="padding:20px">'+dim_name+'：<select id="line_'+kpi_id+"_"+dim_id+'" class="easyui-combobox" style="width:150px;"></select></div>';
								}
							} 
							con += '<div id="line_div_'+kpi_id+'_charttype" style="padding:20px">展示类型：<select id="line_'+kpi_id+'_charttype" class="easyui-combobox" style="width:70px;"></select></div>';
							return con;
						},
						closable : false
					})
					//拼接完成html，现在用ajax动态添加数据
					for(var j=0; j<kpi_dims.length; j++){
						var dim = kpi_dims[j].split(",");
						var dim_id = dim[0];
						var dim_name = dim[1];
						var dim_type = dim[2];
						//alert(dim_id+" "+dim_name+" "+dim_type+" "+kpi_id);
						if(dim_type == "1"){//树结构
							$.ajax({
								dim_id : dim_id,
								dim_name : dim_name,
								url : __contextpath+"/main/getDimComboTree.do?dim_id="+dim_id,
								dataType : "json",
								success : function(tree){
									$('#line_'+kpi_id+"_"+this.dim_id).combotree('loadData',tree);
									$('#line_'+kpi_id+"_"+this.dim_id).combotree({
										animate : true,
										valueField : 'id',
										textField : 'text',
										panelHeight : 'auto',
										value : '请选择'+this.dim_name,
									});
								},
								error : function(){
									alert("树类型加载有问题");
								}
							})
						} else if(dim_type == "2"){//下拉列表结构
							$.ajax({
								dim_id : dim_id,
								dim_name : dim_name,
								url : __contextpath+"/main/getDimComboBox.do?dim_id="+dim_id,
								dataType : "json",
								success : function(box){
									$('#line_'+kpi_id+"_"+this.dim_id).combobox('loadData', box);
									$('#line_'+kpi_id+"_"+this.dim_id).combobox({
										valueField : 'value',
										textField : 'text',
										panelHeight : 'auto',
										value : '请选择'+this.dim_name,
									});
								},
								error : function(){
									alert("下拉列表类型加载有问题");
								}
							})
						}
					}
					$('#line_'+kpi_id+'_charttype').combobox({
						valueField : 'value',
						textField : 'text',
						data : options_charttype,
						panelHeight : 'auto',

						// 设置初始值
						onLoadSuccess : function() {
							var data = $('#line_'+kpi_id+'_charttype').combobox('getData');
							$('#line_'+kpi_id+'_charttype').combobox('select', data[0].value);
						},
					})
					
					
					
					
					
				} else {//如果不止一个Kpi 则有公共dim标签页
					//生成公共dim标签页
					var kpi_dims = kpi_dims_acc[0].split("@");
					//生成标签页
					$('#line_tabs').tabs('add',{
						title : '各指标公共维度',
						content : function(){
							var con = "";
							//循环读取每一个Kpi的dim
							for(var j=0; j<kpi_dims.length; j++){
								var dim = kpi_dims[j].split(",");
								var dim_id = dim[0];
								var dim_name = dim[1];
								var dim_type = dim[2];
								//判断dim类型
								if(dim_type == "0"){//时间类型
									con += "<div id='line_div_"+dim_id+"_"+dim_type+"' style='padding:20px'>频率：<select id='line_rate' class='easyui-combobox' style='width:70px;'></select>"+dim_name+":<input id='line_time_date' class='easyui-datebox' style='width:120px;'/><select id='line_time_ym' class='easyui-combobox' style='width:100px;'></select></div>";
								} else if(dim_type == "1"){//树类型
									con += '<div id="line_div_'+dim_id+'_'+dim_type+'" style="padding:20px">'+dim_name+'：<select id="line_'+dim_id+'" class="easyui-combotree" style="width:150px;"></select></div>';
								} else if(dim_type == "2"){//列表类型
									con += '<div id="line_div_'+dim_id+'_'+dim_type+'" style="padding:20px">'+dim_name+'：<select id="line_'+dim_id+'" class="easyui-combobox" style="width:150px;"></select></div>';
								}
							} 
							return con;
						},
						closable : false
					})
					//拼接完成html，现在用ajax动态添加数据
					for(var j=0; j<kpi_dims.length; j++){
						var dim = kpi_dims[j].split(",");
						var dim_id = dim[0];
						var dim_name = dim[1];
						var dim_type = dim[2];
						//alert(dim_id+" "+dim_name+" "+dim_type+" "+kpi_id);
						if(dim_type == "1"){//树结构
							$.ajax({
								dim_id : dim_id,
								dim_name : dim_name,
								url : __contextpath+"/main/getDimComboTree.do?dim_id="+dim_id,
								dataType : "json",
								success : function(tree){
									$('#line_'+this.dim_id).combotree('loadData',tree);
									$('#line_'+this.dim_id).combotree({
										animate : true,
										valueField : 'id',
										textField : 'text',
										panelHeight : 'auto',
										value : '请选择'+this.dim_name,
									});
								},
								error : function(){
									alert("公共树类型加载有问题");
								}
							})
						} else if(dim_type == "2"){//下拉列表结构
							$.ajax({
								dim_id : dim_id,
								dim_name : dim_name,
								url : __contextpath+"/main/getDimComboBox.do?dim_id="+dim_id,
								dataType : "json",
								success : function(box){
									$('#line_'+this.dim_id).combobox('loadData', box);
									$('#line_'+this.dim_id).combobox({
										valueField : 'value',
										textField : 'text',
										panelHeight : 'auto',
										value : '请选择'+this.dim_name,
									});
								},
								error : function(){
									alert("公共下拉列表类型加载有问题");
								}
							})
						}
					}
					//循环Kpi 生成Kpi剩余dim标签页
					for(var i=1; i<kpi_dims_acc.length; i++){
						var kpi_dims = kpi_dims_acc[i].split("@");
						//获取当前标签页的kpi_id
						var kpi_id = nodes[i-1].id;
						//生成每个Kpi单独dim标签页
						$('#line_tabs').tabs('add',{
							title : nodes[i-1].text,
							content : function(){
								//存放html代码
								var con = "";
								//循环读取每一个Kpi的dim
								for(var j=0; j<kpi_dims.length; j++){
									var dim = kpi_dims[j].split(",");
									var dim_id = dim[0];
									var dim_name = dim[1];
									var dim_type = dim[2];
									//判断dim类型
									if(dim_type == "0"){//时间类型
										con += "<div id='line_div_"+kpi_id+"_"+dim_id+"_"+dim_type+"' style='padding:20px'>频率：<select id='line_rate' class='easyui-combobox' style='width:70px;'></select>"+dim_name+":<input id='line_time_date' class='easyui-datebox' style='width:120px;'/><select id='line_time_ym' class='easyui-combobox' style='width:100px;'></select></div>";
									} else if(dim_type == "1"){//树类型
										con += '<div id="line_div_'+kpi_id+"_"+dim_id+'_'+dim_type+'" style="padding:20px">'+dim_name+'：<select id="line_'+kpi_id+"_"+dim_id+'" class="easyui-combotree" style="width:150px;"></select></div>';
									} else if(dim_type == "2"){//列表类型
										con += '<div id="line_div_'+kpi_id+"_"+dim_id+'_'+dim_type+'" style="padding:20px">'+dim_name+'：<select id="line_'+kpi_id+"_"+dim_id+'" class="easyui-combobox" style="width:150px;"></select></div>';
									}
								} 
								con += '<div id="line_div_'+kpi_id+'_charttype" style="padding:20px">展示类型：<select id="line_'+kpi_id+'_charttype" class="easyui-combobox" style="width:70px;"></select></div>';
								return con;
							},
							closable : false
						})
						
						
						
						
						//拼接完成html，现在用ajax动态添加数据
						for(var j=0; j<kpi_dims.length; j++){
							var dim = kpi_dims[j].split(",");
							var dim_id = dim[0];
							var dim_name = dim[1];
							var dim_type = dim[2];
							//alert(dim_id+" "+dim_name+" "+dim_type+" "+kpi_id);
							if(dim_type == "1"){//树结构
								$.ajax({
									kpi_id : kpi_id,
									dim_id : dim_id,
									dim_name : dim_name,
									url : __contextpath+"/main/getDimComboTree.do?dim_id="+dim_id,
									dataType : "json",
									success : function(tree){
										$('#line_'+this.kpi_id+"_"+this.dim_id).combotree('loadData',tree);
										$('#line_'+this.kpi_id+"_"+this.dim_id).combotree({
											animate : true,
											valueField : 'id',
											textField : 'text',
											panelHeight : 'auto',
											value : '请选择'+this.dim_name,
										});
									},
									error : function(){
										alert(this.kpi_id+"_"+this.dim_name+"树类型加载有问题");
									}
								})
							} else if(dim_type == "2"){//下拉列表结构
								$.ajax({
									kpi_id : kpi_id,
									dim_id : dim_id,
									dim_name : dim_name,
									url : __contextpath+"/main/getDimComboBox.do?dim_id="+dim_id,
									dataType : "json",
									success : function(box){
										$('#line_'+this.kpi_id+"_"+this.dim_id).combobox('loadData', box);
										$('#line_'+this.kpi_id+"_"+this.dim_id).combobox({
											valueField : 'value',
											textField : 'text',
											panelHeight : 'auto',
											value : '请选择'+this.dim_name,
										});
									},
									error : function(){
										alert(this.kpi_id+"_"+this.dim_name+"下拉列表类型加载有问题");
									}
								})
							}
						}
						$('#line_'+kpi_id+'_charttype').combobox({
							valueField : 'value',
							textField : 'text',
							data : options_charttype,
							panelHeight : 'auto',

							// 设置初始值
							onLoadSuccess : function() {
								var data = $('#line_'+kpi_id+'_charttype').combobox('getData');
								$('#line_'+kpi_id+'_charttype').combobox('select', data[0].value);
							},
						})
					}
				}

				
				
				// 频率下拉框属性设置
				$("#line_rate").combobox({
					valueField : 'value',
					textField : 'text',
					data : options_rate,
					panelHeight : 'auto',

					// 设置初始值
					onLoadSuccess : function() {
						var data = $('#line_rate').combobox('getData');
						$('#line_rate').combobox('select', data[0].value);
					},

					// 时间combobox联动
					onChange : function(newVal, oldVal) {
						var selValue = $("#line_rate").combobox('getValue');
						// 选择年份
						if (selValue == "year") {
							// 清楚原来ym这个select中的选项,显示年月下拉框，隐藏日期组件
							$("#line_time_ym").combobox("clear");
							$('#line_time_ym').next(".combo").show();
							$('#line_time_date').next(".combo").hide();

							$("#line_time_ym_s").combobox("clear");
							$('#line_time_ym_s').next(".combo").show();
							$('#line_time_date_s').next(".combo").hide();

							// 重新设置ym当前select中的选项
							$("#line_time_ym").combobox({
								panelHeight : 'auto',
								data : options_time_year,
								// 设置初始值
								onLoadSuccess : function() {
									var data = $('#line_time_ym').combobox('getData');
									$('#line_time_ym').combobox('select', data[0].value);
								}
							});
							
							// 重新设置ym当前select中的选项
							$("#line_time_ym_s").combobox({
								panelHeight : 'auto',
								data : options_time_year,
								// 设置初始值
								onLoadSuccess : function() {
									var data = $('#line_time_ym_s').combobox('getData');
									$('#line_time_ym_s').combobox('select', data[0].value);
								}
							});
						}
						// 选择月份
						if (selValue == "month") {
							// 清楚原来ym这个select中的选项，显示年月下拉框，隐藏日期组件
							$("#line_time_ym").combobox("clear");
							$('#line_time_ym').next(".combo").show();
							$('#line_time_date').next(".combo").hide();
							
							$("#line_time_ym_s").combobox("clear");
							$('#line_time_ym_s').next(".combo").show();
							$('#line_time_date_s').next(".combo").hide();

							// 重新设置ym当前select中的选项
							$("#line_time_ym").combobox({
								panelHeight : '150',
								data : options_time_month,
								// 设置初始值
								onLoadSuccess : function() {
									var data = $('#line_time_ym').combobox('getData');
									$('#line_time_ym').combobox('select', data[0].value);
								}
							});
							
							// 重新设置ym当前select中的选项
							$("#line_time_ym_s").combobox({
								panelHeight : '150',
								data : options_time_month,
								// 设置初始值
								onLoadSuccess : function() {
									var data = $('#line_time_ym_s').combobox('getData');
									$('#line_time_ym_s').combobox('select', data[0].value);
								}
							});
						}
						// 选择日期
						if (selValue == "day") {
							// 清楚原来ym这个select中的选项，显示年月下拉框，隐藏日期组件
							$("#line_time_ym").combobox("clear");
							$('#line_time_ym').next(".combo").hide();
							$('#line_time_date').next(".combo").show();
							
							$("#line_time_ym_s").combobox("clear");
							$('#line_time_ym_s').next(".combo").hide();
							$('#line_time_date_s').next(".combo").show();
						}
					}
				});
				//第一次刷新时，初始化候年月下拉框
				$("#line_time_ym").combobox({
					valueField : 'value',
					textField : 'text',
					data : options_time_year,
					panelHeight : 'auto',

					// 设置初始值
					onLoadSuccess : function() {
						var data = $('#line_time_ym').combobox('getData');
						$('#line_time_ym').combobox('select', data[0].value);
					}
				});
				
				
				
				var dims_custom = kpi_dims_acc[0].split('@');
				var res = "";
				for(var i=0; i<dims_custom.length; i++){
					var dim = dims_custom[i].split(',');
					res += '{"text":"'+dim[1]+'","value":'+dim[0]+'},';
				}
				res = res.substring(0,res.length-1);
				res = '['+res+']';
				var jres = JSON.parse(res);
				//定义左中的变量值
				$("#line_var_dim").combobox("clear");
				$('#line_var_dim').combobox('loadData', jres);
				$("#line_var_dim").combobox({
					// 设置初始值
					onLoadSuccess : function() {
						var data = $('#line_var_dim').combobox('getData');
						$('#line_var_dim').combobox('select', data[0].value);
					}
				});
				
				//显示该显示的组件
				$('#line_div_var_dim').show();//显示选择横轴维度下拉框
				$('#line_button_makechart').show();//显示生成图表按钮
				$('#line_div_chart_var').show();//显示图形参数设置div
			},
			error :  function(){
				alert("Kpi不能为空");
			}
		})
	})
	
	//窗口点击关闭事件
	$('#line_win_tool').children(':first').next().click(function () {
		$('#line_window_kpi').window('close');
	})
	
	//设置窗口中的树属性
	$('#line_tree_kpi').tree({
		method : 'get',
		animate : true,
		checkbox : true,
		onlyLeafCheck : 'true',//只在子节点前显示复选框
		cascadeCheck : false,//上下级检查
	})
	
	//设置窗口中的表格属性
	$('#line_dg_kpi').datagrid({
		fitColumns : true,
		striped : true,//条文化
		singleSelect : false,
		method : 'get',
		rownumbers : true,
		fit : true,
		border : false,
		selectOnCheck : true,
		checkOnSelect : true,
	})
	
	///////////////////////////////////////其他组件
	
	//定义左中的变量值
	$('#line_var_dim').combobox({
		valueField : 'value',
		textField : 'text',
		panelHeight : 'auto',
		value : '请选择维度',
		
		// combobox联动
		onChange : function(newVal, oldVal) {
			var selValue = $("#line_var_dim").combobox('getValue');
			//判断用户选择要做横轴的维度
			if(selValue == "1"){//用户选择了时间
				$('#line_div_var_custom').show();
			} else {
				$('#line_div_var_custom').hide();
			}
		}
	})
	
	
	//////////////////////////////////////定义生图形按钮
	$('#line_button_makechart').on('click',function(){
		//获取当前用户选择完的kpi标签页
		var tabs = $("#line_tabs").tabs("tabs");//得到所有标签页jq对象
		var length = tabs.length;//获得标签页长度，用来判断是多个指标还是单指标
		//清空之前表格的数据
		//for(var n=0; n<lineoption.series.length; n++){
		lineoption.series = [];
		lineoption.yAxis = [];
		//}
		/////////////////////////////////////////////////////////////单指标情况
		if(length == 1){
			var panel = $('#line_tabs').find('.panel').children();
			var dims_len = panel.children().length-1;
			var onetab = tabs[0];
		    var title = onetab.panel('options').tab.text();
			//定义指标名称
			var kpi_name=title;
			//定义维度数组
			var dims="";
			//定义指标id
			var kpi_id="";
			//定义维度id
			var dim_id="";
			//定义维度的type
			var dim_type="";
			//获取用户选择的横轴维度
			var dim_var = $('#line_var_dim').combobox('getValue');//dim_id
			//循环读取当前tab页的各个维度
			for(var i=0; i<panel.children().length; i++){
				var div_id = panel.children().eq(i).attr('id');
				var array = div_id.split('_');
				//获取到当前指标id
				kpi_id = array[2];
				//获取到当前维度id
				dim_id = array[3];
				//获取当前维度的type
				dim_type = array[4];
				//判断当前维度是否为时间维度
				if(dim_id == "1"){//是时间维度
					var rate = $('#line_rate').combobox('getValue');
					var dim1 = "";
					//判断用户选择的横轴维度是否为时间
					if(dim_var == "1"){//为时间
						if(rate == "year" || rate == "month"){//如果选择频率是年或月
							dim1 = "dim1@"+$('#line_time_ym_s').combobox('getValue')+","+$('#line_time_ym').combobox('getValue')+","+rate;
						} else {//如果选择的时间是日期
							var time = $('#line_time_date').datebox('getValue');
							var time_s = $('#line_time_date_s').datebox('getValue');
							var temp = time.split("-");
							var time = temp[0]+temp[1]+temp[2];
							temp = time_s.split("-");
							var time_s = temp[0]+temp[1]+temp[2];
							dim1 = "dim1@"+time_s+","+time+","+rate;
						}
						
					} else {//不为时间
						if(rate == "year" || rate == "month"){//如果选择频率是年或月
							dim1 = "dim1@"+$('#line_time_ym').combobox('getValue')+","+rate;
						} else {//如果选择的时间是日期
							var time = $('#line_time_date').datebox('getValue');
							var temp = time.split("-");
							var time = temp[0]+temp[1]+temp[2];
							dim1 = "dim1@"+time+","+rate;
						}
					}
					dims = dims+dim1+"^";
				} else {//当前读取的是非时间维度
					//判断当前维度的类别
					if(dim_id == "2"){//维度2 树结构
						var dim2 = "dim2@"+$("#line_"+kpi_id+"_"+dim_id).combotree('tree').tree('getSelected').id;
						dims = dims+dim2+"^";
					} else if(dim_id == "3"){//维度3 树结构
						var dim3 = "dim3@"+$("#line_"+kpi_id+"_"+dim_id).combotree('tree').tree('getSelected').id;
						dims = dims+dim3+"^";
					} else if(dim_id == "4"){//维度4 表结构
						var dim4 = "dim4@"+$("#line_"+kpi_id+"_"+dim_id).combobox('getValue');
						dims = dims+dim4+"^";
					}
				}
			}
			//加载图表信息
			$.ajax({
				url : __contextpath+"/main/getLineKpiValue.do?dims="+dims+"&dim_id="+dim_var+"&kpi_id="+kpi_id,
				dataType : "text",
				success : function(data_y){
					//定义纵坐标
					var dy = data_y.split(',');
					//获取类型
					var type = $('#line_'+kpi_id+"_charttype").combobox('getValue');
					//插入数据
					lineoption.series[lineoption.series.length] = {'type' : type,'name' : kpi_name,'data' : dy};
					//加载横坐标
					$.ajax({
						url : __contextpath+"/main/getLineKpiXAxis.do?dims="+dims+"&dim_id="+dim_var,
						dataType:"text",
						success:function(data_x){
							//获取横坐标
							var dx = data_x.split(',');
							lineoption.xAxis.data = dx;
							lineoption.legend.data = [kpi_name];
							$.ajax({
								url : __contextpath+"/main/getKpiUnitName.do?kpi_ids="+kpi_id,
								dataType : "text",
								success : function(units){
									var unit = units.split(",");
									for(var u=0; u<unit.length; u++){
										lineoption.yAxis[lineoption.yAxis.length] = {'type' : 'value','name' : unit[u]};
									}
									var myChart = echarts.init($('#line_div_chart').get(0));
									myChart.setOption(lineoption);
								},
								error : function(){
									alert("单位获取有问题");
								}
							})
						},
						error:function(){
							alert("X_error");
						}
					})
				},
				error : function(){
					alert("Y_error");
				}
			})
			
			
		} else {///////////////////////////////////////////////////多指标情况
			//获取包括在公共标签页在内的所有标签页
			var panel = $('#line_tabs').find('.panel');
//			alert(panel.eq(0).children().children().length);
			var panel_public = panel.eq(0).children().children();
			//定义字符串存放公共维度
			var dims_public = "";
			//定义所有的指标的维度
			var kpis_dims = "";
			//定义字符串数组存放多个kpi_id
			var kpi_ids = "";
			//存放多个kpi_name
			var kpi_names = new Array();
			//获取用户选择的横轴维度
			var dim_var = $('#line_var_dim').combobox('getValue');//dim_id
			
			//循环读取公共标签页的维度
			for(var j=0; j<panel_public.length; j++){
				//获取当前维度的div_id
				var div_id = panel_public.eq(j).attr('id');
				//获取当前维度的维度id
				var dim_id = div_id.split('_')[2];
				//判断当前的维度id
				if(dim_id == "1"){//维度1 时间
					var rate = $('#line_rate').combobox('getValue');
					var dim1 = "";
					//判断用户选择的横轴维度是否为时间
					if(dim_var == "1"){//为时间 则有起始时间
						if(rate == "year" || rate == "month"){//如果选择频率是年或月
							dim1 = "dim1@"+$('#line_time_ym_s').combobox('getValue')+","+$('#line_time_ym').combobox('getValue')+","+rate;
						} else {//如果选择的时间是日期
							var time = $('#line_time_date').datebox('getValue');
							var time_s = $('#line_time_date_s').datebox('getValue');
							var temp = time.split("-");
							var time = temp[0]+temp[1]+temp[2];
							temp = time_s.split("-");
							var time_s = temp[0]+temp[1]+temp[2];
							dim1 = "dim1@"+time_s+","+time+","+rate;
						}
						
					} else {//不为时间 则无起始时间
						if(rate == "year" || rate == "month"){//如果选择频率是年或月
							dim1 = "dim1@"+$('#line_time_ym').combobox('getValue')+","+rate;
						} else {//如果选择的时间是日期
							var time = $('#line_time_date').datebox('getValue');
							var temp = time.split("-");
							var time = temp[0]+temp[1]+temp[2];
							dim1 = "dim1@"+time+","+rate;
						}
					}
					dims_public = dims_public+dim1+"^";
				} else if(dim_id == "2"){//维度2 树结构
					var dim2 = "dim2@"+$("#line_"+dim_id).combotree('tree').tree('getSelected').id;
					dims_public = dims_public+dim2+"^";
				} else if(dim_id == "3"){//维度3 树结构
					var dim3 = "dim3@"+$("#line_"+dim_id).combotree('tree').tree('getSelected').id;
					dims_public = dims_public+dim3+"^";
				} else if(dim_id == "4"){//维度4 表结构
					var dim4 = "dim4@"+$("#line_"+dim_id).combobox('getValue');
					dims_public = dims_public+dim4+"^";
				}
			}
			
			//循环读取非公共标签页
			for(var i=1; i<panel.length; i++){
				var dims_pri = "";
				var onetab = tabs[i];
			    var title = onetab.panel('options').tab.text();
			    //定义指标名称
				var kpi_name=title;
				kpi_names[i-1] = kpi_name;
				//获取单独指标的标签页
				var panel_pri = panel.eq(i).children().children();
				//定义kpi_id
				var kpi_id=panel_pri.eq(0).attr('id').split('_')[2];
				//循环读取每个标签页的维度
				for(var j=0; j<panel_pri.length; j++){
					//获取当前维度的div_id
					var div_id = panel_pri.eq(j).attr('id');
					//获取当前维度的dim_id
					var dim_id = div_id.split('_')[3];
					//判断当前维度的类别
					if(dim_id == "2"){//维度2 树结构
						var dim2 = "dim2@"+$("#line_"+kpi_id+"_"+dim_id).combotree('tree').tree('getSelected').id;
						dims_pri = dims_pri+dim2+"^";
					} else if(dim_id == "3"){//维度3 树结构
						var dim3 = "dim3@"+$("#line_"+kpi_id+"_"+dim_id).combotree('tree').tree('getSelected').id;
						dims_pri = dims_pri+dim3+"^";
					} else if(dim_id == "4"){//维度4 表结构
						var dim4 = "dim4@"+$("#line_"+kpi_id+"_"+dim_id).combobox('getValue');
						dims_pri = dims_pri+dim4+"^";
					}
				}
				//拼接所有指标id
				kpi_ids = kpi_ids+kpi_id+"_";
				//拼接所有指标的维度信息
				kpis_dims = kpis_dims+dims_public+dims_pri+"-";
			}
			//加载图表信息
			$.ajax({
				url : __contextpath+"/main/getKpiUnitName.do?kpi_ids="+kpi_ids,
				dataType : "text",
				success : function(units){
					var unit_ids = units.split("@")[0];
					var unit_names = units.split("@")[1];
					unit_id = unit_ids.split(",");
					unit_name = unit_names.split(",");
					lineoption.yAxis[0] = {'type' : 'value','position' : 'left','name' : unit_name[0]};
					for(var u=1; u<unit_id.length; u++){
						lineoption.yAxis[lineoption.yAxis.length] = {'type' : 'value','position' : 'right','offset' : (u-1)*30,'name' : unit_name[u]};
					}				
					$.ajax({
						url : __contextpath+"/main/getLineKpiXAxis.do?dims="+dims_public+dims_pri+"&dim_id="+dim_var,
						dataType:"text",
						success:function(data_x){
							//获取横坐标
							var dx = data_x.split(',');
							lineoption.xAxis.data = dx;
							lineoption.legend.data = kpi_names;
							//定义标题
							var tt = $('#line_text_title').textbox('getValue');
							if(tt == "" || tt == null){
								lineoption.title.text = "多指标折线图";
							} else {								
								lineoption.title.text = tt;
							}
							$.ajax({
								url : __contextpath+"/main/getLineKpiValues.do?kpis_dims="+kpis_dims+"&dim_id="+dim_var+"&kpi_ids="+kpi_ids,
								dataType : "text",
								success : function(data_y){
									//定义纵坐标
									var data = data_y.split('!');
									var kpi_unit_ids = Array();
									for(var i=0; i<data.length; i++){
										kpi_unit_ids[i] = data[i].split("@")[0];
										data[i] = data[i].split("@")[1];
									}
									//获取kpi_id
									var kpi_id_temp = kpi_ids.split('_');
									//定义最小值数组来存放不同轴的最小值
									var min = new Array();
									var max = new Array();
									for(var i=0; i<unit_id.length; i++){
										min[i] = 999999;
										max[i] = 0;
									}
									for(var i=0; i<data.length; i++){
										var dy = data[i].split(',');
										var type = $('#line_'+kpi_id_temp[i]+"_charttype").combobox('getValue');
										for(var j=0; j<unit_id.length; j++){
											if(kpi_unit_ids[i] == unit_id[j]){
												for(var n=0; n<dy.length; n++){
													if(dy[n]<min[j]){
														min[j] = parseInt(dy[n]);
													}
													if(dy[n]>max[j]){
														max[j] = parseInt(dy[n]);
													}
												}
												var temp_m = (max[j]-min[j])/10;
												lineoption.yAxis[j].min = min[j]-temp_m;
												//lineoption.yAxis[j].max = max[j]+temp_m;
												//插入数据
												lineoption.series[lineoption.series.length] = {'type' : type,'yAxisIndex' : j,'name' : kpi_names[i],'data' : dy};
											}
										}
									}
									var myChart = echarts.init($('#line_div_chart').get(0));
									myChart.setOption(lineoption);
								},
								error : function(){
									alert("单位获取有问题");
								}
							})
						},
						error:function(){
							alert("X_error");
						}
					})
				},
				error : function(){
					alert("Y_error");
				}
			})
		}
	})
})