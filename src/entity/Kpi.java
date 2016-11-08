package entity;

/**
 * 指标成员类
 * 
 * @author Song Zeheng
 *
 *         日期：2016年7月27日 时间：下午2:58:28
 */
public class Kpi {
	private int kpi_id; // 指标ID
	private String kpi_name; // 指标名称
	private int par_kpi_id; // 父指标ID
	private String kpi_type; // 指标类型
	private int kpi_ord; // 顺序
	private int kpi_unit_id1;//指标单位id
	
	private int kpi_unit_id2;//指标单位id
	private String kpi_unit_name;//指标单位name

	private int dim_id1; //维度ID
	private String dim_name; //维度名称
	private String dim_type; //维度类型
	private String dim_desc; //维度描述
	
	private int dim_value_id;//维度值id
	private int dim_id2;//维度id
	private String dim_value; //维度值
	private String dim_value_name; //维度值名称
	private int dim_par_id; //父维度ID
	private int disp_ord; //顺序
	
	private int ext_id; //扩展字段ID
	private String field_name; //字段名称
	private String field_type; //字段类型
	private String ext_name; //名称
	private String ext_desc; //扩展字段描述
	
	
	public int getKpi_id() {
		return kpi_id;
	}


	public void setKpi_id(int kpi_id) {
		this.kpi_id = kpi_id;
	}


	public String getKpi_name() {
		return kpi_name;
	}


	public void setKpi_name(String kpi_name) {
		this.kpi_name = kpi_name;
	}


	public int getPar_kpi_id() {
		return par_kpi_id;
	}


	public void setPar_kpi_id(int par_kpi_id) {
		this.par_kpi_id = par_kpi_id;
	}


	public String getKpi_type() {
		return kpi_type;
	}


	public void setKpi_type(String kpi_type) {
		this.kpi_type = kpi_type;
	}


	public int getKpi_ord() {
		return kpi_ord;
	}


	public void setKpi_ord(int kpi_ord) {
		this.kpi_ord = kpi_ord;
	}


	public int getKpi_unit_id1() {
		return kpi_unit_id1;
	}


	public void setKpi_unit_id1(int kpi_unit_id1) {
		this.kpi_unit_id1 = kpi_unit_id1;
	}


	public int getKpi_unit_id2() {
		return kpi_unit_id2;
	}


	public void setKpi_unit_id2(int kpi_unit_id2) {
		this.kpi_unit_id2 = kpi_unit_id2;
	}


	public String getKpi_unit_name() {
		return kpi_unit_name;
	}


	public void setKpi_unit_name(String kpi_unit_name) {
		this.kpi_unit_name = kpi_unit_name;
	}


	public int getDim_id1() {
		return dim_id1;
	}


	public void setDim_id1(int dim_id1) {
		this.dim_id1 = dim_id1;
	}
	

	public String getDim_name() {
		return dim_name;
	}


	public void setDim_name(String dim_name) {
		this.dim_name = dim_name;
	}


	public String getDim_type() {
		return dim_type;
	}


	public void setDim_type(String dim_type) {
		this.dim_type = dim_type;
	}


	public String getDim_desc() {
		return dim_desc;
	}


	public void setDim_desc(String dim_desc) {
		this.dim_desc = dim_desc;
	}


	public int getDim_value_id() {
		return dim_value_id;
	}


	public void setDim_value_id(int dim_value_id) {
		this.dim_value_id = dim_value_id;
	}


	public int getDim_id2() {
		return dim_id2;
	}


	public void setDim_id2(int dim_id2) {
		this.dim_id2 = dim_id2;
	}


	public String getDim_value() {
		return dim_value;
	}


	public void setDim_value(String dim_value) {
		this.dim_value = dim_value;
	}


	public String getDim_value_name() {
		return dim_value_name;
	}


	public void setDim_value_name(String dim_value_name) {
		this.dim_value_name = dim_value_name;
	}


	public int getDim_par_id() {
		return dim_par_id;
	}


	public void setDim_par_id(int dim_par_id) {
		this.dim_par_id = dim_par_id;
	}


	public int getDisp_ord() {
		return disp_ord;
	}


	public void setDisp_ord(int disp_ord) {
		this.disp_ord = disp_ord;
	}


	public int getExt_id() {
		return ext_id;
	}


	public void setExt_id(int ext_id) {
		this.ext_id = ext_id;
	}


	public String getField_name() {
		return field_name;
	}


	public void setField_name(String field_name) {
		this.field_name = field_name;
	}


	public String getField_type() {
		return field_type;
	}


	public void setField_type(String field_type) {
		this.field_type = field_type;
	}


	public String getExt_name() {
		return ext_name;
	}


	public void setExt_name(String ext_name) {
		this.ext_name = ext_name;
	}


	public String getExt_desc() {
		return ext_desc;
	}


	public void setExt_desc(String ext_desc) {
		this.ext_desc = ext_desc;
	}


	public Kpi() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
