package entity;

import java.util.List;

public class SimpleOrgValue {
	
	private int id;
	private String text;
	private List<SimpleOrgValue> children;
	//private String node_type;
	//private String iconCls;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public List<SimpleOrgValue> getChildren() {
		return children;
	}
	public void setChildren(List<SimpleOrgValue> children) {
		this.children = children;
	}
	
//	public String getIconCls() {
//		return iconCls;
//	}
//	public void setIconCls(String iconCls) {
//		this.iconCls = iconCls;
//	}
//	public String getNode_type() {
//	return node_type;
//}
//public void setNode_type(String node_type) {
//	this.node_type = node_type;
//}
	

}
