$(function() {
	//动态从数据库中加载科室树
	$.ajax({
		url : "InitServlet",
		dataType:"json",
		success:function(data){
			$('#sel_area').combotree('loadData',data);
			$.ajax({
				url : "GetValueServlet?variable=2016/106,year,table,1/",
				dataType:"json",
				success:function(data){
					$('#dg').treegrid('loadData',data);
				}
			
			})
		}
	})
	
	
	var myChart = echarts.init($('.div-right').get(0));
	myChart.setOption(lineoption);
	// 按钮点击事件
	$('#button').click(
		function() {
			var temp = $("#sel_area").combotree("getValues");

			// 设置flag判断用户输入
			var flag = true;

			var v_sel_rate = $('#sel_rate').combobox('getValue');
			var v_sel_time_ym = $('#sel_time_ym').combobox(
					'getValue');
			var v_sel_time_date = $('#sel_time_date').combobox(
					'getValue');
			var variable = "";
			var v_sel_area = "";
			
			// js判断用户输入
			if ($("#sel_area").combotree('tree')
					.tree('getSelected') == null) {
				flag = false;
				alert("请选择要查询的科室");
			}

			if (flag) {
				v_sel_area = $("#sel_area").combotree('tree').tree('getSelected').id; // 得到树对象选择的节点
			}

			if (v_sel_rate == "day" && v_sel_time_date == "") {
				alert("请选择要查询的具体日期");
				flag = false;
			}
			
			// 对日期进行处理
			if (v_sel_time_date != "") {
				var temp_date = v_sel_time_date.split("/");
				v_sel_time_date = temp_date[2] + temp_date[0]
						+ temp_date[1];
			}

			if (v_sel_rate == "year" || v_sel_rate == "month") {
				variable = v_sel_time_ym + "/" + v_sel_area + ","
						+ v_sel_rate + ",table,1/2/3/4/5/6/7/8/9/10/11/12/13/14/15/16/17/18/19/20/";
			}
			if (v_sel_rate == "day") {
				variable = v_sel_time_date + "/" + v_sel_area + ","
						+ v_sel_rate + ",table,1/2/3/4/5/6/7/8/9/10/11/12/13/14/15/16/17/18/19/20/";
			}

			if (flag) {
//							 alert(variable);
//							location.href = "GetValueServlet?variable="
//									+ variable;
				 $.ajax({
					 url : "GetValueServlet?variable="+variable,
					 dataType:"json",
					 success:function(data){
					     $('#dg').treegrid('loadData',data);
					     
					    }
				 })
			}
		})
					
					
	//指标数据
	var options_kpi = [ {
		text : "预算管理指标",
		value : "预算管理指标"
	}, {
		text : "预算执行率",
		value : "预算执行率"
	}, {
		text : "预算收入执行率",
		value : "预算收入执行率"
	}, {
		text : "预算支出执行率",
		value : "预算支出执行率"
	}, {
		text : "财政专项拨款执行率",
		value : "财政专项拨款执行率"
	}, {
		text : "结余和风险管理指标",
		value : "结余和风险管理指标"
	}, {
		text : "业务收支结余率",
		value : "业务收支结余率"
	}, {
		text : "资产负债率",
		value : "资产负债率"
	}, {
		text : "流动比率",
		value : "流动比率"
	}, {
		text : "资产运营指标",
		value : "资产运营指标"
	}, {
		text : "总资产周转率",
		value : "总资产周转率"
	}, {
		text : "应收账款周转天数",
		value : "应收账款周转天数"
	}, {
		text : "存货周转率",
		value : "存货周转率"
	}, {
		text : "成本管理指标",
		value : "成本管理指标"
	}, {
		text : "每门诊人次收入",
		value : "每门诊人次收入"
	}, {
		text : "每门诊人次支出",
		value : "每门诊人次支出"
	}, {
		text : "门诊收入成本率",
		value : "门诊收入成本率"
	}, {
		text : "每住院人次收入",
		value : "每住院人次收入"
	}, {
		text : "每住院人次支出",
		value : "每住院人次支出"
	}, {
		text : "住院收入成本率",
		value : "住院收入成本率"
	}, ];

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

	// 测试方法
	$('#check').click(function() {
		// $('#hidden').val($("#sel_area").combotree("getValues"));
		// alert($('#hidden').val());
		// alert(options_time_year_f);
		// delete options_time_year_f[1];
		// alert(options_time_year_f);
		// $('#sel_time_ym').next(".combo").show();
		// $('#sel_time_date').next(".combo").hide();
		// var t = $("#sel_area").combotree('tree'); // 得到树对象
		// var n = t.tree('getSelected'); // 得到选择的节点
		// alert(n.text);
		alert($("#sel_area").combotree("getSelected").text);
	})

	// 变量下拉框属性
	$("#sel_kpi").combobox({
		valueField : 'value',
		textField : 'text',
		data : options_kpi,

		// 设置初始值
		onLoadSuccess : function() {
			var data = $('#sel_kpi').combobox('getData');
			$('#sel_kpi').combobox('select', data[0].value);
		}
	});

	// 地区下拉框属性
	$("#sel_area").combotree({
		animate : true,
		valueField : 'value',
		textField : 'text',
		panelHeight : '250',
		value : '请选择科室',

		onLoadSuccess : function(data) {
			var da = $("#sel_area").combotree('tree').tree("getRoot");
			$("#sel_area").combotree('tree').tree("check", da.text);
		}
	});

	// 频率下拉框属性设置
	$("#sel_rate").combobox({
		valueField : 'value',
		textField : 'text',
		data : options_rate,
		panelHeight : 'auto',

		// 设置初始值
		onLoadSuccess : function() {
			var data = $('#sel_rate').combobox('getData');
			$('#sel_rate').combobox('select', data[0].value);
		},

		// 时间combobox联动
		onChange : function(newVal, oldVal) {
			var selValue = $("#sel_rate").combobox('getValue');
			// 选择年份
			if (selValue == "year") {
				// 清楚原来ym这个select中的选项,显示年月下拉框，隐藏日期组件
				$("#sel_time_ym").combobox("clear");
				$('#sel_time_ym').next(".combo").show();
				$('#sel_time_date').next(".combo").hide();

				// 重新设置ym当前select中的选项
				$("#sel_time_ym").combobox({
					panelHeight : 'auto',
					data : options_time_year,
				});
			}
			// 选择月份
			if (selValue == "month") {
				// 清楚原来ym这个select中的选项，显示年月下拉框，隐藏日期组件
				$("#sel_time_ym").combobox("clear");
				$('#sel_time_ym').next(".combo").show();
				$('#sel_time_date').next(".combo").hide();

				// 重新设置ym当前select中的选项
				$("#sel_time_ym").combobox({
					panelHeight : '150',
					data : options_time_month,
				});
			}
			// 选择日期
			if (selValue == "day") {
				// 清楚原来ym这个select中的选项，显示年月下拉框，隐藏日期组件
				$("#sel_time_ym").combobox("clear");
				$('#sel_time_ym').next(".combo").hide();
				$('#sel_time_date').next(".combo").show();
			}
		}
	});
	
	//第一次刷新时，初始化候年月下拉框
	$("#sel_time_ym").combobox({
		valueField : 'value',
		textField : 'text',
		data : options_time_year,
		panelHeight : 'auto',

		// 设置初始值
		onLoadSuccess : function() {
			var data = $('#sel_time_ym').combobox('getData');
			$('#sel_time_ym').combobox('select', data[0].value);
		}

	});
	
	//初始化数据表格
	$('#dg').treegrid({
		idField: 'kpi_id',
		treeField: 'kpi_name',
		columns : [ [ {
			field : 'kpi_name',
			title : '指标名称',
			width : 200,
			align : 'center',
			formatter : function(value, row, index) {
				var rate = "";
				if(row.dim1.length == 4){
					rate = "year";
				}
				if(row.dim1.length == 6){
					rate = "month";
				}
				if(row.dim1.length == 8){
					rate = "day";
				}
				var variable ="'" + row.dim1 + "/" + row.dim2_id + ","+ rate + ",chart," + row.kpi_id + "/" + "@"+row.kpi_id+","+row.kpi_name+","+row.dim1+","+row.dim2+"'";
				var array = new Array(row.kpi_id,row.kpi_name,row.dim1,row.dim2_id,row.dim2);
//				alert(variable);
				return '<a id="'+ row.kpi_id +'" href="javascript:void(0);" onclick="mkchart('+ variable +')">' + value + '</a>';
			}
		}, {
			field : 'kpi_id',
			title : '指标id',
			width : 100,
			align : 'center',
			hidden: true
		}, {
			field : 'dim2_id',
			title : 'dim_id',
			width : 100,
			align : 'center',
			hidden: true
		}, {
			field : 'dim1',
			title : '时间',
			width : 100,
			align : 'center'
		}, {
			field : 'dim2',
			title : '部门',
			width : 75,
			align : 'center'
		}, {
			field : 'kpi_value',
			title : '指标值',
			width : 75,
			align : 'center'
		}, {
			field : 'ext1',
			title : '上期',
			width : 75,
			align : 'center'
		}, {
			field : 'ext2',
			title : '同期',
			width : 75,
			align : 'center'
		}, {
			field : 'ext3',
			title : '比上期',
			width : 75,
			align : 'center'
		}, {
			field : 'ext4',
			title : '比同期',
			width : 75,
			align : 'center'
		} ] ]
	});

})