package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cache.Kpi_cache;
import dao.ChartDao;
import util.BuildComboBox;

/**
 * 窗口查找Kpi
 * 
 * @author Song Zeheng
 *
 *         日期：2016年8月8日 时间：下午5:38:49
 */
@WebServlet("/SearchKpiServlet")
public class SearchKpiServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=UTF-8");

		String variable = request.getParameter("variable");
		String result = "";
		if (variable == null) {
			result = BuildComboBox.getKpiCombo(Kpi_cache.kpis);
		} else {
			result = ChartDao.getSearchKpi(variable);
		}


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
