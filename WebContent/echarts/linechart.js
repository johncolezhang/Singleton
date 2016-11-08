var lineoption = {
		//标题
		title: {
			//主标题
			text: '折线图',
		},
		//提示框
		tooltip: {
			//折线图提示框
			trigger: 'axis'
		},
		//图例
		legend: {
			//数据
			data:[]
		},
		//工具箱
		toolbox: {
			//是否显示
			show: true,
			//工具箱配置
			feature: {
				//数据区域缩放 直角坐标系支持
				dataZoom: {
					//缩放时控制的Y轴，为空则不控制所有Y轴，为false则控制Y轴
					yAxisIndex: false
				},
				//是否数据不可编辑，false表示可编辑
				dataView: {readOnly: false},
				//还原
				restore: {},
				//下载为图片
				saveAsImage: {}
			}
		},
		//横坐标
		xAxis:  {
			type: 'category',
			boundaryGap: true,
			data:['2016'],
//			//坐标轴类型，value数据，category类型，time时间，
//			type: 'category',
//			//起点和重点数据是否定格，是否留白
//			boundaryGap: false,
//			//数据
//			data: ['周一','周二','周三','周四','周五','周六','周日','周一','周二','周三','周四','周五','周六','周日']
		},
		//纵坐标
		yAxis: [{}],
		series: 
		[
		 {}
		]
	};