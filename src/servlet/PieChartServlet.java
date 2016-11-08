package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.ChartDao;

/**
 * 生成饼状图传参
 * @author Song Zeheng
 *
 *             日期：2016年8月10日 时间：上午10:00:32
 */
@WebServlet("/PieChartServlet")
public class PieChartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        
        String var[] = new String [2];
        //接受字符串数组
        var[0] = request.getParameter("var0");
        var[1] = request.getParameter("var1");
        String type = request.getParameter("type");
        //接受kpi_id和dim_id
        String kd_id = request.getParameter("var2");
        String temp[] = kd_id.split(",");
        int dim_id = Integer.parseInt(temp[0]);
        int kpi_id = Integer.parseInt(temp[1]);
        
        String result = ChartDao.chartDatajson(var, dim_id, kpi_id,type);
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
