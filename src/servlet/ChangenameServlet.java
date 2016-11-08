package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.changetreeDao;
import util.BulidDirTree;
import util.UpdateDir_Cache;

/**
 * Servlet implementation class ChangenameServlet
 */
@WebServlet("/ChangenameServlet")
public class ChangenameServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=UTF-8");
		
		String st1 = request.getParameter("node_id");
		int nodeid =Integer.parseInt(st1);
		String st2 = request.getParameter("name");
		
		//在数据中更改名字
		changetreeDao.changename(nodeid, st2);
		//在缓存中更改名字
		UpdateDir_Cache.modifynodename(nodeid, st2);
		
		//从缓存中生成树
		String s2=BulidDirTree.buildTreedir();
		
		PrintWriter out = null;
		try {
		    out = response.getWriter();
		    out.write(s2);
		} catch (IOException e) {
		    e.printStackTrace();
		} finally {
		    if (out != null) {
		        out.close();
		    }
		}
		
		
	}

}
