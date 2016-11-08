package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.ChartDao;

/**
 * 用来查找当前指标的Kpi值
 * @author Song Zeheng
 *
 *             日期：2016年8月30日 时间：上午11:31:09
 */
@WebServlet("/GetLineKpiValueServlet")
public class GetLineKpiValueServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        
        String kpi_id = request.getParameter("kpi_id");
        
        String dim_id = request.getParameter("dim_id");
        
        String dims = request.getParameter("dims");
        dims = dims.substring(0, dims.length()-1);
        
        String[] dims_array = dims.split("\\^");
        
        String result = ChartDao.chartDatajson(dims_array, Integer.parseInt(dim_id), Integer.parseInt(kpi_id), "chartline");
        
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
