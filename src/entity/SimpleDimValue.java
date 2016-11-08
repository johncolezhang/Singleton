package entity;

import java.util.List;

public class SimpleDimValue {
	private int id;
	private String text;
	private List<SimpleDimValue> children;
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
	public List<SimpleDimValue> getChildren() {
		return children;
	}
	public void setChildren(List<SimpleDimValue> children) {
		this.children = children;
	}
}
