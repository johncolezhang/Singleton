package servlet;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cache.Dir_cache;
import dao.changetreeDao;
import util.BulidDirTree;

/**
 * 初始化选项
 * @author Song Zeheng
 *
 *             日期：2016年7月29日 时间：下午12:13:51
 */
@WebServlet("/InitDirServlet")
public class InitDirServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        
        Dir_cache cache = new Dir_cache();
        //生成树，在这里会打印出json在控制台下面
		String st_area = BulidDirTree.buildTreedir();
		
		PrintWriter out = null;
		try {
		    out = response.getWriter();
		    out.write(st_area);
		} catch (IOException e) {
		    e.printStackTrace();
		} finally {
		    if (out != null) {
		        out.close();
		    }
		}
	}

}
