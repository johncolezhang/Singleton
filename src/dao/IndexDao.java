package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import entity.GwnUser;
import entity.PageBean;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import util.DataUtil;


public class IndexDao {
	Connection conn = null;
	Statement stat = null;
	ResultSet rs = null;

	/**
	 * 获取GwnUser日志
	 * 
	 * @param pageBean
	 * @param total
	 * @return
	 */
	public static JSONArray getGwnUser(PageBean pageBean) throws SQLException, Exception {
		{
			int limitMax = pageBean.getPage() * pageBean.getRows();
			int limitMin = (pageBean.getPage() - 1) * pageBean.getRows();
			String sBuffer = "select * from Gwn_User  order by id desc  LIMIT " + pageBean.getRows();
			JSONArray jsonArray = DataUtil.preparedStatement("Portal", sBuffer);
			JSONObject json = new JSONObject();
			JSONArray jsonArray1 = new JSONArray();
			if (jsonArray.size() > 0) {
				for (int i = limitMin; i <= limitMax; i++) {
					if (i == jsonArray.size()) {
						break;
					}
					GwnUser gwnUser = new GwnUser();
					json = (JSONObject) jsonArray.get(i);
					gwnUser.setId(Integer.parseInt(json.getString("ID")));
					gwnUser.setName(json.getString("NAME"));
					gwnUser.setOrgId(Integer.parseInt(json.getString("ORG_ID")));
					gwnUser.setPwd("********");
					jsonArray1.add(gwnUser);

				}

			}
			return jsonArray1;
		}
	}

}