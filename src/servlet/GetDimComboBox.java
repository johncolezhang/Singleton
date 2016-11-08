package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cache.Kpi_cache;
import util.BuildComboBox;


/**
 * 根据dim_id获取下拉框列表的json
 * @author Song Zeheng
 *
 *             日期：2016年8月17日 时间：上午10:24:30
 */
@WebServlet("/GetDimComboBox")
public class GetDimComboBox extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        
        String dim_id = request.getParameter("dim_id");
        
        String result = BuildComboBox.getDimValueCombo(Kpi_cache.kpis,Integer.parseInt(dim_id));
        
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
