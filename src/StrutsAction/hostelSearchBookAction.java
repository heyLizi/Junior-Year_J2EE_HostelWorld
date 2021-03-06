package StrutsAction;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.opensymphony.xwork2.ActionSupport;

import PersistenceModel.bookHostelBean;
import dao.Service.hostelSearchBookDao;

public class hostelSearchBookAction extends ActionSupport implements ServletResponseAware,ServletRequestAware{

	private HttpServletResponse response;
	private HttpServletRequest request;
	
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		// TODO Auto-generated method stub
		this.request = request;
		
	}

	@Override
	public void setServletResponse(HttpServletResponse response) {
		// TODO Auto-generated method stub
		this.response = response;
	}
	
	public String execute() throws Exception{
		
		response.setCharacterEncoding("UTF-8");	
		response.setContentType("text/html;charset=UTF-8");
		
		WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
		
		PrintWriter out = response.getWriter();
		
		hostelSearchBookDao hos = (hostelSearchBookDao) ctx.getBean("hostelSearchBookImpl");
		
		String id = String.valueOf(request.getSession(false).getAttribute("ID"));
		String membership = request.getParameter("membership");
		
		List<bookHostelBean> list = hos.getHostelBook(Integer.parseInt(id), Integer.parseInt(membership));
		
		if(list.size()==0){
			out.write("fail");
			out.close();
			return "success";
		}
		
		JSONArray array = new JSONArray();
		
		for(int i = 0 ; i <list.size();i++){
			JSONObject temp = new JSONObject();
			temp.put("bookID", list.get(i).getBookID());
			temp.put("bookTime", list.get(i).getBookTime().toLocalDate());
			
			String ids =String.valueOf(list.get(i).getHostelID());
			temp.put("hostelID",getStringID(ids));
			temp.put("category", list.get(i).getRoomCategory());
			temp.put("cost", list.get(i).getCost());
			temp.put("startDate", list.get(i).getStartDate().toString());
			temp.put("endDate", list.get(i).getEndDate().toString());
			array.put(temp);
		}
		
		out.write(array.toString());
		out.close();
		
		return "success";
	}
	
	public String getStringID(String id){
		String result = id;
		for(int i=id.length();i<7;i++){
			result = "0"+result;
		}
		return result;
	}

}
