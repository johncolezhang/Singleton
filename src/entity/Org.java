package entity;

public class Org {
	
	private int org_id;
	private String org_name;
	private int org_par_id;
	private String descr;
	private String dis_order;
	
	public int getOrg_id() {
		return org_id;
	}
	public void setOrg_id(int org_id) {
		this.org_id = org_id;
	}
	public String getOrg_name() {
		return org_name;
	}
	public void setOrg_name(String org_name) {
		this.org_name = org_name;
	}
	public int getOrg_par_id() {
		return org_par_id;
	}
	public void setOrg_par_id(int org_par_id) {
		this.org_par_id = org_par_id;
	}
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	public String getDis_order() {
		return dis_order;
	}
	public void setDis_order(String dis_order) {
		this.dis_order = dis_order;
	}
	
	

}
