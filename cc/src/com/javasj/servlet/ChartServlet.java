package com.javasj.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.bean.ComBean;
import com.javasj.entity.Chart;

public class ChartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		this.doPost(request, response);
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		ComBean cBean = new ComBean();
		String method=request.getParameter("method");
		if(method.equals("bing")){
			String ksj = request.getParameter("ksj");
			String esj = request.getParameter("esj");
			List<Chart> chartlist =new ArrayList<Chart>();
			List swlist =cBean.getCom("select * from fl order by id desc",2);
			if(!swlist.isEmpty()){
				for(int i=0;i<swlist.size();i++){
					List list2=(List)swlist.get(i);
					Chart chart=new Chart();
					float cs=cBean.getFloat("select count(*) from jy where fl='"+list2.get(1).toString()+"' and jysj>='"+ksj+"' and jysj<='"+esj+"' ") ;
					chart.setTime(list2.get(1).toString());
					chart.setSumCount(cs);
					chart.setIpCount((i+1)*100);
					chartlist.add(chart);
				}
				JSONObject param=new JSONObject();
				JSONArray json = JSONArray.fromObject(chartlist);
				param.put("RowCount", chartlist.size());
				param.put("Rows", json);
				response.setContentType("text/html;charset=utf-8");
				PrintWriter out=response.getWriter();
				out.println(param.toString());
				out.flush();
				out.close();
			}
		}
		else{
			List<Chart> chartlist =new ArrayList<Chart>();//SELECT sum(zj),sj from dd group by sj order by sj asc
			String sql="select rkbh,kc from rk order by sj asc";
			List swlist =cBean.getCom(sql,2);
			if(!swlist.isEmpty()){
				for(int i=0;i<swlist.size();i++){
					List list2=(List)swlist.get(i);
					Chart chart=new Chart();
					chart.setTime(list2.get(0).toString());
					chart.setSumCount(Float.parseFloat(list2.get(1).toString()));
					chart.setIpCount((i+1)*100);
					chartlist.add(chart);
				}
				JSONObject param=new JSONObject();
				JSONArray json = JSONArray.fromObject(chartlist);
				param.put("RowCount", chartlist.size());
				param.put("Rows", json);
				response.setContentType("text/html;charset=utf-8");
				PrintWriter out=response.getWriter();
				out.println(param.toString());
				out.flush();
				out.close();
			}
		}
	}

}
