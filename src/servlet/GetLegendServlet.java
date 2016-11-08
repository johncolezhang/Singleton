package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.ChartDao;

/**
 * 获取图例
 * @author Song Zeheng
 *
 *             日期：2016年8月10日 时间：下午4:35:32
 */
@WebServlet("/GetLegendServlet")
public class GetLegendServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        
        String var[] = new String [2];
        //接受字符串数组
        var[0] = "dim1@" + request.getParameter("var0");
        var[1] = "dim2@" + request.getParameter("var1");
        //接受kpi_id和dim_id
        String kd_id = request.getParameter("var2");
        String temp[] = kd_id.split(",");
        int dim_id = Integer.parseInt(temp[0]);
        int kpi_id = Integer.parseInt(temp[1]);
        System.out.println(var[0]+" "+var[1]);
        System.out.println(dim_id);
        
        String result = ChartDao.getLegenddata(var, dim_id);
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
