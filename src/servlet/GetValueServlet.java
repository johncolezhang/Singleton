package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cache.Kpi_cache;
import dao.TableDao;
import util.BuildComboBox;
import util.BuildTree;

/**
 * 按要求查询指标的kpi值以及扩展值
 * @author Song Zeheng
 *
 *             日期：2016年7月29日 时间：下午12:13:31
 */
@WebServlet("/GetValueServlet")
public class GetValueServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=UTF-8");
		
		String variable = request.getParameter("variable");
		String result = TableDao.searchData(variable);
		result = "["+result+"]";
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
//		request.getRequestDispatcher("demo3.jsp").forward(request, response);
	}

}
