$(function() {
	$.ajax({
		url : "InitDirServlet",
		dataType:"json",
		success:function(data){
			$('#org_tree').tree('loadData',data);
		}
	})
	
	$('#org_tree').tree({
		onAfterEdit: function (node){
			//alert("0000");
			$.ajax({
				url : "ChangenameServlet?node_id="+node.id+"&name="+node.text,
				dataType:"json",
				success :function(data) {
					$('#org_tree').tree('loadData',data);
					//alert("123123123"+data);
				},
				error : function(){
					alert("错误");
				}
			});
		}
	});
	
	$('#org_tree').tree({
		onContextMenu: function(e,node){
            e.preventDefault();
            $(this).tree('select',node.target);
            
            var get_node = $('#org_tree').tree('getSelected');
            if (get_node.node_type=='00'){
                $('#mm').menu('show',{left: e.pageX,top: e.pageY});
                }
            else{
                if (get_node.node_type=='01'||get_node.node_type=='02'||get_node.node_type=='03'){
                    $('#nn').menu('show',{left: e.pageX,top: e.pageY});
                }
                else {
                   alert('其他类型');
                }
            }
        }
	});
	
})
















//弹出对话框增加目录节点
function addnode_disdlg(){
		$('#dlg_menuname').dialog({
			closed:false
		})
	}

function append_set_menunode(){
	var node_name=document.getElementById("setmenuname").value;
	if((node_name!='')&&(node_name.length<256)){
		//alert(node_name);
		var t = $('#org_tree');
		var node = t.tree('getSelected');
		var p_id=node.id;            //新建节点属性       id和param1不管
		var new_node_type="00";
		var new_text=node_name;
		//页面加入node，只是node的属性不同而已，调用的servlet是一样的。
		t.tree('append', {
			parent: (node?node.target:null),
			data: [{par_id:p_id,text: new_text,iconCls:"myicon-mydir",node_type:new_node_type}]
		});
		$.ajax({
			url : "AddtreeServlet?par_id="+p_id+"&name="+new_text+"&node_type="+new_node_type,
			dataType:"json",
			success :function(data) {
				$('#org_tree').tree('loadData',data);
				//alert("123123123"+data);
			},
			error : function(){
				alert("错误");
			}
		});		
		//执行完添加节点之后再把对话框给隐藏起来
		$('#dlg_menuname').dialog({
			closed:true
		})
	}
	else{
		alert('error');
	}
}

function cancel_menudlg(){
	$('#dlg_menuname').dialog({
		closed:true
	})
}



//弹出对话框增加子节点
function addnode_childdlg(){
	$('#dlg_childname').dialog({
		closed:false
	})
}

function append_set_childnode(){
	var node_name=document.getElementById("setchildname").value;
	if((node_name!='')&&(node_name.length<256)){
		//alert(node_name);
		var t = $('#org_tree');
		var node = t.tree('getSelected');
		var p_id=node.id;
		var new_text=node_name;
		var new_node_type="01";
		t.tree('append', {
			parent: (node?node.target:null),
			data: [{par_id:p_id,text: new_text,node_type:new_node_type}]
		});
		$.ajax({
			url : "AddtreeServlet?par_id="+p_id+"&name="+new_text+"&node_type="+new_node_type,
			dataType:"json",
			success :function(data) {
				$('#org_tree').tree('loadData',data);
				//alert("123123123"+data);
			},
			error : function(){
				alert("错误");
			}
		});		
		//执行完添加节点之后再把对话框给隐藏起来
		$('#dlg_childname').dialog({
			closed:true
		})
	}
	else{
		alert('error');
	}
}

function cancel_childdlg(){
	$('#dlg_childname').dialog({
	closed:true
	})
}


	//删除空目录
	function remove_nullcatalog(){
		var node = $('#org_tree').tree('getSelected');
	    var childrenNodes = $('#org_tree').tree('getChildren',node.target);
	    //alert(node.text+" "+node.node_type);
		if (childrenNodes=="")
			{
       			$('#org_tree').tree('remove', node.target);
       			var n_id = node.id;
       			//alert(n_id);
       			$.ajax({
       				url : "RemovetreeServlet?node_id="+n_id,
       				dataType:"json",
       				success :function(data) {
       					$('#org_tree').tree('loadData',data);
       					//alert("123123123");
       				},
       				error : function(){
       					alert("错误");
       				}
       			});
       			
			}
		else{
			alert_removemenu();
			//alert('该目录不为空，为避免误删，请先删除内容');
			 /*var msg = "该目录不为空，您真的确定要删除吗？"; 
			  if (confirm(msg)==true){ 
				  $('#org_tree').tree('remove', node.target); 
			  }else{ 
			  }*/ 
		}
	}
	
	function alert_removemenu(){
		$.messager.alert('提示','该目录不为空，请先删除内容');
	}
	
	
	//子节点菜单功能   ①编辑图形 暂时不做考虑   ②删除子节点
	function remove_node(){
		var node = $('#org_tree').tree('getSelected');
		$('#org_tree').tree('remove', node.target);
		var n_id = node.id;
		//alert(n_id);
		$.ajax({
			url : "RemovetreeServlet?node_id="+n_id,
			dataType:"json",
			success :function(data) {
				$('#org_tree').tree('loadData',data);
				//alert("123123123");
			},
			error : function(){
				alert("错误");
			}
		});
		
		
	}
	
	//修改名字    node.id  par.id node.type param1都不变，只改变name
	function changename(){
		var t = $('#org_tree');
		var node = t.tree('getSelected');
		//beginEdit
		$('#org_tree').tree('beginEdit',node.target);
		//alert(node.text);
	}
	
	