package entity;

import java.util.List;

public class SimpleDirValue {
	
	private int id;
	private String text;
	private String node_type;
	private List<SimpleDirValue> children;
	private String iconCls;
	
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
	public List<SimpleDirValue> getChildren() {
		return children;
	}
	public void setChildren(List<SimpleDirValue> children) {
		this.children = children;
	}
	public String getNode_type() {
		return node_type;
	}
	public void setNode_type(String node_type) {
		this.node_type = node_type;
	}
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
}
