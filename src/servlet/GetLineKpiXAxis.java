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
 * 获取折线图横坐标
 * @author Song Zeheng
 *
 *             日期：2016年8月30日 时间：下午3:41:43
 */
@WebServlet("/GetLineKpiXAxis")
public class GetLineKpiXAxis extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        
        String dim_id = request.getParameter("dim_id");
        
        String dims = request.getParameter("dims");
        dims = dims.substring(0, dims.length()-1);
        
        System.out.println(dims);
        
        String[] dims_array = dims.split("\\^");
        String result = ChartDao.getLegenddata(dims_array, Integer.parseInt(dim_id));
        
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
