package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import entity.GwnBaseDefaultData;
import entity.GwnUser;
import util.DataUtil;


public class LoginDao {
	Connection conn = null;
	Statement stat = null;
	ResultSet rs = null;

	/**
	 * 登录
	 * 
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	public GwnUser Login(GwnUser user) throws SQLException {
		String sql = "select * from gwn_user WHERE name='" + user.getName() + "' and pwd='" + user.getPwd() + "'";
		List<Object[]> list = DataUtil.query(GwnBaseDefaultData.sqlName, sql);
		GwnUser gwnUser = new GwnUser();
		if (list.size() != 0) {
			for (int i = 0; i < list.size(); i++) {
				Object[] row = list.get(i);
				gwnUser.setId(Integer.parseInt(row[0].toString()));
				gwnUser.setName(row[1].toString());
				gwnUser.setOrgId(Integer.parseInt(row[3].toString()));
			}
		}
		return gwnUser;
	}

	/**
	 * 修改or添加用户
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public boolean SaveEditUser(GwnUser user) throws Exception {
		String sql = "upsert into GWN_user (id,name,org_id) values (" + user.getId() + ",'" + user.getName() + "',"
				+ user.getOrgId() + ")";
		boolean result = DataUtil.update(GwnBaseDefaultData.sqlName, sql);
		if (result) {
			int roleId = getRoleIdId(GwnBaseDefaultData.sqlName);
			sql = "upsert into gwn_user_role (id,user_id,role_id) values (" + roleId + "," + user.getId() + ","
					+ user.getOrgId() + ")";
			result = DataUtil.update(GwnBaseDefaultData.sqlName, sql);
		}
		return result;
	}

	/**
	 * 修改用户密码
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public static boolean editUserPwd(String id, String pwd) throws Exception {
		String sql = "upsert into GWN_user (id,pwd) values (" + id + ",'" + pwd + "')";
		boolean result = DataUtil.update(GwnBaseDefaultData.sqlName, sql);
		return result;
	}

	/**
	 * 获取GWN_user id并且+1
	 * 
	 * @param dsname
	 * @return
	 * @throws Exception
	 */
	private static AtomicInteger userId;

	public synchronized static int getUserId(String dsname) throws Exception {
		List<Object[]> list = DataUtil.queryObjects(dsname, "select max(id) as id from GWN_user", false);
		if (list != null && list.size() > 0) {
			Object[] row = list.get(0);
			int id = Integer.parseInt(row[0].toString());
			userId = new AtomicInteger(id);
		}
		return userId.incrementAndGet();
	}

	/**
	 * 获取gwn_user_role id并且+1
	 * 
	 * @param dsname
	 * @return
	 * @throws Exception
	 */
	private static AtomicInteger roleId;

	public synchronized static int getRoleIdId(String dsname) throws Exception {
		List<Object[]> list = DataUtil.queryObjects(dsname, "select max(id) as id from gwn_user_role", false);
		if (list != null && list.size() > 0) {
			Object[] row = list.get(0);
			int id = Integer.parseInt(row[0].toString());
			roleId = new AtomicInteger(id);
		}
		return roleId.incrementAndGet();
	}

	/**
	 * 删除用户信息
	 * 
	 * @param user
	 * @return
	 */
	public boolean deleteUser(GwnUser user) throws Exception {
		boolean result = DataUtil.delete(GwnBaseDefaultData.sqlName, "GWN_user", "ID", user.getId());
		return result;
	}

	/**
	 * 判断密码是否相同
	 * 
	 * @param userId
	 * @param pwd
	 * @return
	 * @throws Exception
	 */
	public static boolean judgmentUserPw(String userId, String pwd) throws Exception {
		String sql = "select * from gwn_user WHERE id=" + userId + " and pwd='" + pwd + "'";
		List<Object[]> list = DataUtil.query(GwnBaseDefaultData.sqlName, sql);
		if (list.size() > 0) {
			return true;
		}
		return false;
	}
}
