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
 * 获取当前指标对应的维度
 * @author Song Zeheng
 *
 *             日期：2016年8月9日 时间：下午2:24:34
 */
@WebServlet("/GetDimValueServlet")
public class GetDimValueServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        
        String variables = request.getParameter("variables");
        //var_array[0]为查询的kpi_id var_array[1]为要查询的方式
        String var_array[] = variables.split("@");
		String results = ChartDao.getDimValue(var_array[0]);
		
		String result = "";
		
		//0则得到所有的维度id
		if(var_array[1].equals("0")){
				String res_array[] = results.split("@");
				result = res_array[0];
		}
		//1则得到所有维度的名称
		if(var_array[1].equals("1")){
				String res_array[] = results.split("@");
				result = res_array[1];
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
		
		
		//如果该指标维度大于1，说明除了特殊的时间维度外还有其他维度
//		if(results.length>1){
//			//遍历除了时间维度外的其他维度
//			for(int i=1; i<results.length; i++){
//				//array[0]为该维度id，array[1]为该维度值
//				String array[] = results[i].split("@");
//				
//			}
//		}
	}

}
