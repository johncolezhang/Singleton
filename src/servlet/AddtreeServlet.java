package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.changetreeDao;
import util.BulidDirTree;
import util.UpdateDir_Cache;

/**
 * Servlet implementation class ChangetreeServlet
 */
@WebServlet("/AddtreeServlet")
public class AddtreeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=UTF-8");
		
		//前台传进来数据，后台插入
		//id和param1先不管
		String st1 = request.getParameter("par_id");
		int parId =Integer.parseInt(st1);
		String st2 = request.getParameter("name");
		String st3 = request.getParameter("node_type");
		
//		System.out.println(parId);
//		System.out.println(st2);
//		System.out.println(st3);		
		//在数据库中增加节点
		changetreeDao.addnode(parId, st2, st3);
//		//更新缓存前的生成树
//		String str=BulidDirTree.buildTreedir();;
//		System.out.println(str);
		
		try {
			//更新缓存并重新生成json
			UpdateDir_Cache.addnode(parId, st2, st3);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//更新缓存后生成树
		String s2=BulidDirTree.buildTreedir();
//		System.out.println(s2);
		
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
