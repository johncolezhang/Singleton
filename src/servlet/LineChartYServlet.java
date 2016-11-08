package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.TableDao;

/**
 * 折线图对应的Value值
 * @author Song Zeheng
 *
 *             日期：2016年8月5日 时间：上午8:46:36
 */
@WebServlet("/LineChartYServlet")
public class LineChartYServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=UTF-8");
		
		String variable = request.getParameter("variable");
		String results[] = TableDao.getChart(variable);
		for(int i=0; i<results.length; i++){
			System.out.println(results[i]+" "+i);
		}
		
		PrintWriter out = null;
		try {
		    out = response.getWriter();
		    out.write(results[1]);
		} catch (IOException e) {
		    e.printStackTrace();
		} finally {
		    if (out != null) {
		        out.close();
		    }
		}
	}

}
