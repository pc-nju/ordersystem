// $(function() {
// 	var myChart = echarts.init(document.getElementById('report'));
// 	common.ajax({
// 		url : $('#basePath').val() + '/orderReport/count',
// 		success : function(data) {
// 			var option = {
// 			    title: {
// 			        text: '商户类别订单数'
// 			    },
// 			    tooltip: {
// 			        trigger: 'axis'
// 			    },
// 			    grid: {
// 			        left: '3%',
// 			        right: '4%',
// 			        bottom: '3%',
// 			        containLabel: true
// 			    },
// 			    toolbox: {
// 			        feature: {
// 			            saveAsImage: {}
// 			        }
// 			    },
// 			    xAxis: {
// 			        type: 'category',
// 			        boundaryGap: false
// 			    },
// 			    yAxis: {
// 			        type: 'value'
// 			    }
// 			};
// 			$.extend(true,option,data);
// 			myChart.setOption(option);
// 		},
// 		type : 'GET'
// 	});
// });
$(function () {
	//基于准备好的dom，初始化echarts实例
	var myChart = echarts.init(document.getElementById("report"));
	common.ajax1({
		url : $("#basePath").val() + "/report/saleNumber",
		success : function (response) {
			if (response.code === common.pageCode.SUCCESS){
                //指定图表的配置项和数据
                var option = {
                    title : {
                        text : "销量统计折线图"
                    },
					tooltip : {
                        trigger: 'axis'
					},
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '3%',
                        containLabel: true
                    },
                    toolbox: {
                        feature: {
                            saveAsImage: {}
                        }
                    },
                    xAxis : {
                        type: 'category',
                        boundaryGap: false
                    },
                    yAxis:{
                        type: 'value'
                    }
            };
                //深度拷贝
                $.extend(true,option,response.option);
                //使用刚指定的配置项和数据显示图表
                myChart.setOption(option);
			} else {
			    common.showMessage(response.msg);
            }
        }
	});
})