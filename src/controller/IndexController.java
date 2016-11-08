package controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import dao.IndexDao;
import entity.GwnBaseDefaultData;
import entity.PageBean;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import util.DataUtil;
import util.ResponseUtil;

@Controller
@RequestMapping("/index")
public class IndexController {
	
	/**
	 * 获取GwnUser 用户表
	 * 
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getGwnUser")
	public String getGwnUser(HttpServletResponse response, HttpServletRequest request) throws Exception {
		DataUtil.createDataSource(GwnBaseDefaultData.sqlName, GwnBaseDefaultData.Phoenix_JDBC, "", "",
				GwnBaseDefaultData.Phoenix_URL);
		//System.out.println("11111233344");
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		//System.out.println("123456");
		int total = DataUtil.GetTotal(GwnBaseDefaultData.sqlName, "Gwn_User");
		//System.out.println("123456789");
		PageBean pageBean = new PageBean(Integer.parseInt(page), Integer.parseInt(rows));
		JSONArray jsonArray = IndexDao.getGwnUser(pageBean);
		JSONObject result = new JSONObject();
		result.put("total", total);
		result.put("rows", jsonArray);
		ResponseUtil.write(response, result);
		return null;
	}

}
