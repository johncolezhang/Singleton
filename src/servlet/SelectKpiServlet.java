package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.BuildTree;

/**
 * 获取选择kpi树
 * @author Song Zeheng
 *
 *             日期：2016年8月8日 时间：下午5:22:47
 */
@WebServlet("/SelectKpiServlet")
public class SelectKpiServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=UTF-8");
		
		//后台获取窗口树json
		String result = BuildTree.buildKpiTree();
		
		PrintWriter out = null;
		try {
		    out = response.getWriter();
		    out.write(result);
		} catch (IOException e) {
		    e.printStackTrace();
		} finally {
		    if (out != null) {
		        out.close();
		    }
		}
	}

}
