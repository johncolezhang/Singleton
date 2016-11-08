package entity;

import java.util.List;

public class TableValueAttr {

	private String dim2;//地区名称
	private String dim1;//时间
	private String dim3;//品牌
	private String dim4;
	private String ext4;//同期值
	private String ext1;//上期值
	private String ext2;//比同期值
	private String ext3;//比上期值
	private String kpi_value;//指标值
	private String kpi_name;//指标名称
	private String kpi_id;
	private String dim2_id;
	private List<TableValueAttr> children;
	

	public List<TableValueAttr> getChildren() {
		return children;
	}
	public void setChildren(List<TableValueAttr> children) {
		this.children = children;
	}
	public String getKpi_id() {
		return kpi_id;
	}
	public void setKpi_id(String kpi_id) {
		this.kpi_id = kpi_id;
	}
	public String getDim2_id() {
		return dim2_id;
	}
	public void setDim2_id(String dim2_id) {
		this.dim2_id = dim2_id;
	}
	public String getKpi_name() {
		return kpi_name;
	}
	public void setKpi_name(String kpi_name) {
		this.kpi_name = kpi_name;
	}
	public String getDim2() {
		return dim2;
	}
	public void setDim2(String dim2) {
		this.dim2 = dim2;
	}
	public String getDim1() {
		return dim1;
	}
	public void setDim1(String dim1) {
		this.dim1 = dim1;
	}
	public String getDim3() {
		return dim3;
	}
	public void setDim3(String dim3) {
		this.dim3 = dim3;
	}
	public String getDim4() {
		return dim4;
	}
	public void setDim4(String dim4) {
		this.dim4 = dim4;
	}
	public String getExt4() {
		return ext4;
	}
	public void setExt4(String ext4) {
		this.ext4 = ext4;
	}
	public String getExt1() {
		return ext1;
	}
	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}
	public String getExt2() {
		return ext2;
	}
	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}
	public String getExt3() {
		return ext3;
	}
	public void setExt3(String ext3) {
		this.ext3 = ext3;
	}
	public String getKpi_value() {
		return kpi_value;
	}
	public void setKpi_value(String kpi_value) {
		this.kpi_value = kpi_value;
	}


	
}
