package util;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.io.IOUtils;

//import com.szgwnet.bigdata.historystore.entity.CreateSql;

import entity.GwnBaseDefaultData;
import net.sf.json.JSONArray;

public class DataUtil {
	private static Map<String, BasicDataSource> dsmap;

	static {
		dsmap = new Hashtable<String, BasicDataSource>();
	}

	public static void createDataSource(String dsname, String driver, String name, String pwd, String uri) {
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(driver);
		ds.setUsername(name);
		ds.setPassword(pwd);
		ds.setUrl(uri);

		dsmap.put(dsname, ds);
	}

	public static boolean update(String dsname, String sql) throws SQLException {
		DataSource ds = dsmap.get(dsname);
		if (ds == null)
			throw new SQLException("No [" + dsname + "] data source!");
		Connection conn = null;
		Statement stmt = null;

		try {
			conn = ds.getConnection();
			stmt = conn.createStatement();
			stmt.execute(sql);
			conn.commit();
			return true;
		} catch (SQLException e) {
			if (stmt != null)
				stmt.close();
			if (conn != null)
				conn.close();
			return false;
		} finally {
			if (stmt != null)
				try {
					stmt.close();
				} catch (Exception e) {
				}
			if (conn != null)
				try {
					conn.close();
				} catch (Exception e) {
				}
		}
	}

	public static List<Object[]> query(String dsname, String sql) throws SQLException {
		DataSource ds = dsmap.get(dsname);
		if (ds == null)
			throw new SQLException("No [" + dsname + "] data source!");

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			conn = ds.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			List<Object[]> list = new ArrayList<Object[]>();
			int cols = rs.getMetaData().getColumnCount();
			int[] types = new int[cols];
			for (int i = 0; i < cols; i++) {
				types[i] = rs.getMetaData().getColumnType(i + 1);
			}
			while (rs.next()) {
				Object[] os = new Object[cols];
				for (int i = 0; i < cols; i++) {
					os[i] = rs.getString(i + 1);
				}
				list.add(os);
			}
			return list;
		} catch (SQLException e) {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
			if (conn != null)
				conn.close();
			throw e;
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (Exception e) {
				}
			if (stmt != null)
				try {
					stmt.close();
				} catch (Exception e) {
				}
			if (conn != null)
				try {
					conn.close();
				} catch (Exception e) {
				}
		}
	}

	public static List queryBeans(String dsname, String sql, Class clazz) throws SQLException, NoSuchFieldException,
			SecurityException, IllegalArgumentException, IllegalAccessException, InstantiationException {
		DataSource ds = dsmap.get(dsname);
		if (ds == null)
			throw new SQLException("No [" + dsname + "] data source!");

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			conn = ds.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			List<Object[]> list = new ArrayList<Object[]>();
			int cols = rs.getMetaData().getColumnCount();
			Field[] fs = new Field[cols];

			for (int i = 0; i < cols; i++) {
				String name = rs.getMetaData().getColumnLabel(i + 1);
				name = name.toLowerCase();
				fs[i] = clazz.getDeclaredField(name);
			}

			while (rs.next()) {
				Object bean = clazz.newInstance();
				for (int i = 0; i < cols; i++) {
					if (fs[i] != null)
						fs[i].set(bean, rs.getString(i + 1));
				}
			}
			return list;
		} catch (SQLException e) {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
			if (conn != null)
				conn.close();
			throw e;
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (Exception e) {
				}
			if (stmt != null)
				try {
					stmt.close();
				} catch (Exception e) {
				}
			if (conn != null)
				try {
					conn.close();
				} catch (Exception e) {
				}
		}
	}

	private static void _row(ResultSet rs, int count, int[] types, Object[] os) throws SQLException {
		for (int i = 0; i < count; i++) {
			switch (types[i]) {
			case Types.CHAR:
			case Types.VARCHAR:
			case Types.LONGVARCHAR:
				os[i] = rs.getString(i + 1);
				break;
			case Types.BINARY:
			case Types.VARBINARY:
			case Types.LONGVARBINARY:
				os[i] = rs.getBytes(i + 1);
				break;
			case Types.BIT:
				os[i] = rs.getBoolean(i + 1);
				break;
			case Types.TINYINT:
				os[i] = rs.getByte(i + 1);
				break;
			case Types.SMALLINT:
				os[i] = rs.getShort(i + 1);
				break;
			case Types.INTEGER:
				os[i] = rs.getInt(i + 1);
				break;
			case Types.BIGINT:
				os[i] = rs.getLong(i + 1);
				break;
			case Types.REAL:
				os[i] = rs.getFloat(i + 1);
				break;
			case Types.FLOAT:
			case Types.DOUBLE:
				os[i] = rs.getDouble(i + 1);
				break;
			case Types.DECIMAL:
			case Types.NUMERIC:
				os[i] = rs.getBigDecimal(i + 1);
				break;
			default:
				os[i] = rs.getString(i + 1);
				break;
			}
		}
	}

	public static class ObjectIterator {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		int cols;
		int[] types;
		int count;
		boolean withtitle;
		Object[] titles;

		public ObjectIterator(Connection c, Statement s, ResultSet r, int cs, int[] ts, boolean with, Object[] tls) {
			conn = c;
			stmt = s;
			rs = r;
			cols = cs;
			types = ts;
			count = 0;
			withtitle = with;
			titles = tls;
		}

		public int getColumnCount() {
			return cols;
		}

		public int next(Object[] buf) throws SQLException {
			if (buf != null && buf.length >= cols) {
				if (count == 0) {
					count++;
					for (int i = 0; i < cols; i++) {
						buf[i] = titles[i];
					}
					return 1;
				} else if (rs != null && types != null && rs.next()) {
					count++;
					_row(rs, cols, types, buf);
					return 1;
				}
			}
			return 0;
		}

		public void close() {
			if (rs != null)
				try {
					rs.close();
				} catch (Exception e) {
				}
			if (stmt != null)
				try {
					stmt.close();
				} catch (Exception e) {
				}
			if (conn != null)
				try {
					conn.close();
				} catch (Exception e) {
				}
		}
	}

	public static ObjectIterator getObjectIterator(String dsname, String sql, boolean withtitle) throws SQLException {
		DataSource ds = dsmap.get(dsname);
		if (ds == null)
			throw new SQLException("No [" + dsname + "] data source!");

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			conn = ds.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			int cols = rs.getMetaData().getColumnCount();
			int[] types = new int[cols];
			Object[] titles = null;
			if (withtitle) {
				titles = new Object[cols];
			}
			for (int i = 0; i < cols; i++) {
				types[i] = rs.getMetaData().getColumnType(i + 1);
				if (withtitle) {
					titles[i] = rs.getMetaData().getColumnLabel(i + 1);
				}
			}
			return new ObjectIterator(conn, stmt, rs, cols, types, withtitle, titles);
		} catch (SQLException e) {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
			if (conn != null)
				conn.close();
			throw e;
		}

	}

	public static List<Object[]> queryObjects(String dsname, String sql, boolean withtitle) throws SQLException {
		//System.out.println("hy?????????");
		DataSource ds = dsmap.get(dsname);
		if (ds == null)
			throw new SQLException("No [" + dsname + "] data source!");
		//System.out.println("hy?????????");
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			conn = ds.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			List<Object[]> list = new ArrayList<Object[]>();
			int cols = rs.getMetaData().getColumnCount();
			int[] types = new int[cols];
			Object[] titles = null;
			if (withtitle) {
				titles = new Object[cols];
			}
			for (int i = 0; i < cols; i++) {
				types[i] = rs.getMetaData().getColumnType(i + 1);
				if (withtitle) {
					titles[i] = rs.getMetaData().getColumnLabel(i + 1);
				}
			}
			if (withtitle)
				list.add(titles);
			while (rs.next()) {
				Object[] os = new Object[cols];
				_row(rs, cols, types, os);
				list.add(os);
			}
			return list;
		} catch (SQLException e) {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
			if (conn != null)
				conn.close();
			throw e;
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (Exception e) {
				}
			if (stmt != null)
				try {
					stmt.close();
				} catch (Exception e) {
				}
			if (conn != null)
				try {
					conn.close();
				} catch (Exception e) {
				}
		}
	}

	
	
	
	public static List<Object[]> queryRel(String dsname, String table, String infield, String value, String outfield)
			throws SQLException {
		String sql = "select " + outfield + " from " + table + " where " + infield + "=" + value;
		return query(dsname, sql);
	}

	/**
	 * 关联表�?�过ID关联查找
	 * 
	 * @param table
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public static String[] SearchSQLByID(String table, String id, String Columu, String Option) throws SQLException {
		int f = 0;
		String sql = "select * from " + table + " WHERE " + Columu + "=" + id;
		if (Option != null) {
			sql = sql + Option;
		}
		List<Object[]> list = DataUtil.query(GwnBaseDefaultData.sqlName, sql);
		if (list.size() != 0) {
			String[] Id = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				Object[] row = list.get(i);
				Id[f] = row[2].toString();
				f++;
			}
			return Id;
		}
		return null;
	}
	
	/**
	 * preparedStatement返回 JsonArray
	 * 
	 * @param string
	 * @param sBuffer
	 * @return
	 * @throws Exception
	 */
	public static JSONArray preparedStatement(String dsname, String sBuffer) throws Exception {
		Connection conn = null;
		DataSource ds = dsmap.get(dsname);
		conn = ds.getConnection();
		PreparedStatement preparedStatement = conn.prepareStatement(sBuffer);
		JSONArray jsonArray = JsonUtil.formatRsToJsonArray(preparedStatement.executeQuery());
		if (conn != null) {
			conn.close();
		}
		return jsonArray;
	}

	
	/**
	 * 保存日志Log日志
	 * 
	 * @param dsname
	 * @param task_id
	 * @param op_id
	 * @param op_user_id
	 * @throws Exception
	 */
	public static void SetLog(String dsname, String task_id, int op_id, int op_user_id, String error_Msg,
			String fileName) throws Exception {
		long dataTime = System.currentTimeMillis();
		int id = DataUtil.logIdGet(dsname);
		error_Msg = error_Msg.replace("\\", "\\\\");
		error_Msg = error_Msg.replace("'", "\\");
		String Updata = "upsert into gwn_asy_task_log(id,task_id,op_id,op_user_id,op_time,param1,param2) values(" + id
				+ "," + task_id + "," + op_id + "," + op_user_id + "," + dataTime + ",'" + error_Msg + "','" + fileName
				+ "')";
		DataUtil.update("Portal", Updata);
	}

	/**
	 * 保存任务到gwn_asy_task
	 * 
	 * @param dsname
	 * @param task_id
	 * @param op_id
	 * @param op_user_id
	 * @throws Exception
	 */
	/*
	public static void SetTask(String dsname, CreateSql createSql, String state) throws Exception {
		long dataTime = System.currentTimeMillis();
		String Updata = "upsert into gwn_asy_task(id,name,description,param1,state,type_id,last_time) values("
				+ createSql.getTaskId() + ",'" + createSql.getName() + "','" + createSql.getDescription() + "','"
				+ createSql.getParam1() + "','" + state + "'," + 1 + "," + dataTime + ")";
		DataUtil.update(dsname, Updata);
	}
	 */
	/**
	 * 获取gwn_asy_task id并且+1
	 * 
	 * @param dsname
	 * @return
	 * @throws Exception
	 */
	private static AtomicInteger idgen;

	public synchronized static int idGet(String dsname) throws Exception {
		List<Object[]> list = DataUtil.queryObjects(dsname, "select max(id) as id from gwn_asy_task", false);
		if (list != null && list.size() > 0) {
			Object[] row = list.get(0);
			int id = Integer.parseInt(row[0].toString());
			idgen = new AtomicInteger(id);
		}
		return idgen.incrementAndGet();
	}

	/**
	 * 获取gwn_asy_task_log id并且+1
	 * 
	 * @param dsname
	 * @return
	 * @throws Exception
	 */
	private static AtomicInteger logId;

	public synchronized static int logIdGet(String dsname) throws Exception {
		List<Object[]> list = DataUtil.queryObjects(dsname, "select max(id) as id from gwn_asy_task_log", false);
		if (list != null && list.size() > 0) {
			Object[] row = list.get(0);
			int id = Integer.parseInt(row[0].toString());
			logId = new AtomicInteger(id);
		}
		return logId.incrementAndGet();
	}

	/**
	 * 返回table数量
	 * 
	 * @param dsname
	 * @param table
	 * @return
	 * @throws Exception
	 */
	public static int GetTotal(String dsname, String table) throws Exception {
		//System.out.println("datautil");
		List<Object[]> list = DataUtil.queryObjects(dsname, "select count(1) from " + table, false);
		//System.out.println("1111111");
		int MaxId = 0;
		if (list != null && list.size() > 0) {
			Object[] row = list.get(0);
			MaxId = Integer.parseInt(row[0].toString());
		}
		return MaxId;
	}

	/**
	 * 删除数据
	 * 
	 * @param dsname
	 * @param task_id
	 * @param op_id
	 * @param op_user_id
	 * @throws Exception
	 */
	public static boolean delete(String dsname, String tableName, String idName, int id) throws Exception {
		boolean result = false;
		String deleteSql = "delete from " + tableName + " where  " + idName + "=" + id;
		result = DataUtil.update(dsname, deleteSql);
		return result;
	}

	/**
	 * 
	 * 获得�?小的id
	 * 
	 * @param dsname
	 * @param id
	 * @param tableName
	 * @param idName
	 * @param min
	 * @param minNumber
	 * @param orderby
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public static int getMin(String dsname, String id, String tableName, String idName, String min, String minNumber,
			String orderby, String limit) throws Exception {
		String sql = "select  min(" + id + ") as id from " + tableName + " where  " + min + "<" + minNumber
				+ "order by " + orderby + " desc   limit" + limit;
		List<Object[]> list = DataUtil.query("Portal", sql);
		int minId = 0;
		if (list.size() != 0) {
			for (int l = 0; l < list.size(); l++) {
				Object[] row = list.get(l);
				minId = Integer.parseInt(row[0].toString());
			}
		}
		return minId;
	}

	/**
	 * hadoop 获取二进制流
	 * 
	 * @param string
	 * @param string2
	 * @return
	 */
	public static boolean QueryByConditionByHadoop(String url, HttpServletResponse response) throws IOException {
		System.setProperty("HADOOP_USER_NAME", GwnBaseDefaultData.HADOOP_USER_NAME);
		String dest = GwnBaseDefaultData.HADOOP_IP + url;
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(dest), conf);
		if (fs.exists(new Path(dest))) {
			FSDataInputStream fsdi = fs.open(new Path(dest));
			OutputStream outputStream = response.getOutputStream();
			IOUtils.copyBytes(fsdi, outputStream, 4096, true);
			return true;
		}
		return false;
	}

	/**
	 * Hbase插入数据
	 * 
	 * @param tableName
	 * @return
	 */
	public static boolean insertData(String tableName, String rowkey, byte[] value) throws Exception {
		Configuration conf = null;
		Configuration HBASE_CONFIG = new Configuration();
		HBASE_CONFIG.set("hbase.master", GwnBaseDefaultData.Hbase_Master);
		HBASE_CONFIG.set("hbase.zookeeper.quorum", GwnBaseDefaultData.Hbase_Quorum);
		HBASE_CONFIG.set("hbase.zookeeper.property.clientPort", GwnBaseDefaultData.Hbase_ClientPort);
		conf = HBaseConfiguration.create(HBASE_CONFIG);
		HTable table = new HTable(conf, tableName);
		Put put = new Put(rowkey.getBytes());
		put.add("value".getBytes(), null, value);// 本行数据的第�?�?
		try {
			table.put(put);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Hbase 查找�?行记�?
	 * 
	 * @param tableName
	 * @param rowKey
	 * @return
	 * @throws IOException
	 */
	public static byte[] QueryByConditionByHbabe(String tableName, String rowKey) throws IOException {
		Configuration conf = null;
		Configuration HBASE_CONFIG = new Configuration();
		HBASE_CONFIG.set("hbase.master", GwnBaseDefaultData.Hbase_Master);
		HBASE_CONFIG.set("hbase.zookeeper.quorum", GwnBaseDefaultData.Hbase_Quorum);
		HBASE_CONFIG.set("hbase.zookeeper.property.clientPort", GwnBaseDefaultData.Hbase_ClientPort);
		conf = HBaseConfiguration.create(HBASE_CONFIG);
		HTable table = new HTable(conf, tableName);
		Get get = new Get(rowKey.getBytes());
		Result rs = table.get(get);
		for (KeyValue kv : rs.raw()) {
			byte[] bytes = kv.getValue();
			return bytes;
		}
		return null;
	}

	/**
	 * 删除Hbase�?行记�?
	 */
	public static void deleteHbaseRecord(String tableName, String rowKey) throws IOException {
		Configuration conf = null;
		Configuration HBASE_CONFIG = new Configuration();
		HBASE_CONFIG.set("hbase.master", GwnBaseDefaultData.Hbase_Master);
		HBASE_CONFIG.set("hbase.zookeeper.quorum", GwnBaseDefaultData.Hbase_Quorum);
		HBASE_CONFIG.set("hbase.zookeeper.property.clientPort", GwnBaseDefaultData.Hbase_ClientPort);
		conf = HBaseConfiguration.create(HBASE_CONFIG);
		HTable table = new HTable(conf, tableName);
		List list = new ArrayList();
		Delete del = new Delete(rowKey.getBytes());
		list.add(del);
		table.delete(list);
	}

	/**
	 * 获取gwn_visl_dir id并且+1
	 * 
	 * @param dsname
	 * @return
	 * @throws Exception
	 */
	private static AtomicInteger visl_dir_id;

	public synchronized static int vislDirIdGet(String dsname) throws Exception {
		if (visl_dir_id == null) {
			List<Object[]> list = DataUtil.queryObjects(dsname, "select max(id) as id from gwn_visl_dir", false);
			if (list != null && list.size() > 0) {
				Object[] row = list.get(0);
				int id = Integer.parseInt(row[0].toString());
				visl_dir_id = new AtomicInteger(id);
			}
		}
		return visl_dir_id.incrementAndGet();
	}

	/**
	 * 获取visl_user_dir id并且+1
	 * 
	 * @param dsname
	 * @return
	 * @throws Exception
	 */
	private static AtomicInteger visl_user_id;

	public synchronized static int vislUserId(String dsname) throws Exception {
		if (visl_user_id == null) {
			List<Object[]> list = DataUtil.queryObjects(dsname, "select max(id) as id from visl_user_dir", false);
			if (list != null && list.size() > 0) {
				Object[] row = list.get(0);
				int id = Integer.parseInt(row[0].toString());
				visl_user_id = new AtomicInteger(id);
			}
		}
		return visl_user_id.incrementAndGet();
	}

	/**
	 * 删除hadoop上的文件
	 * 
	 * @param string
	 */
	public static boolean deleteHDFSFile(String path) throws Exception {
		System.setProperty("HADOOP_USER_NAME", GwnBaseDefaultData.HADOOP_USER_NAME);
		String dest = GwnBaseDefaultData.HADOOP_IP + path;
		Configuration conf = new Configuration();
		Path Newpath = new Path(dest);
		FileSystem hdfs = Newpath.getFileSystem(conf);
		return hdfs.delete(Newpath, true);
	}

	/**
	 * 判断是否重复命名
	 * 
	 * @param fileName
	 * @return
	 */
	/*
	public static boolean JudgeRepeatName(String id, String fileName) throws Exception {

		String Sql = "select *  FROM gwn_visl_dir visl where  visl.par_id=" + id + " and visl.NAME='" + fileName + "'";
		JSONArray jarray = DataUtil.preparedStatement(GwnBaseDefaultData.sqlName, Sql);
		if (jarray.size() > 0) {
			return true;
		}
		return false;
	}
	 */
}
