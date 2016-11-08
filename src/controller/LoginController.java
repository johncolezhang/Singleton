package controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import dao.LoginDao;
import entity.GwnBaseDefaultData;
import entity.GwnUser;
import util.DataUtil;
import util.ResponseUtil;


@Controller
@RequestMapping("/user")
public class LoginController {
	
	/**
	 * 登陆
	 * 
	 * @param user
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/login")
	public String List(GwnUser user, HttpServletResponse response, HttpServletRequest request) throws Exception {
		LoginDao loginDao = new LoginDao();
		GwnUser gwnUser = loginDao.Login(user);
		if (gwnUser.getId() == 0) {
			HttpSession session = request.getSession();
			session.setAttribute("errorMsg", "用户名或密码错误");
			return "redirect:/login.jsp";
		} else {
			HttpSession session = request.getSession();
			session.setAttribute("currentUser", gwnUser);
			return "redirect:/Dir_tree.jsp";
		}
	}
	
	/**
	 * 添加OR修改用户信息
	 * 
	 * @param user
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/saveEditUser")
	public String saveEditUser(GwnUser user, HttpServletResponse response) throws Exception {
		System.out.println("111111133333333");
		LoginDao loginDao = new LoginDao();
		// 修改
		boolean result = false;
		if (user.getId() != 0) {
			result = loginDao.SaveEditUser(user);
		} else {
			user.setId(loginDao.getUserId(GwnBaseDefaultData.sqlName));
			result = loginDao.SaveEditUser(user);
		}
		ResponseUtil.write(response, result);
		return null;
	}
	
	
	/**
	 * 删除用户信息
	 * 
	 * @param user
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/deleteUser")
	public String deleteUser(GwnUser user, HttpServletResponse response) throws Exception {
		LoginDao loginDao = new LoginDao();
		// 删除
		boolean result = false;
		if (user.getId() != 0) {
			result = loginDao.deleteUser(user);
		}
		ResponseUtil.write(response, result);
		return null;
	}


}
