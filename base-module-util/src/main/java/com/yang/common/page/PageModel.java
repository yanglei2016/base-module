package com.yang.common.page;

import javax.servlet.http.HttpServletRequest;

public class PageModel {

	/**分页拦截标识*/
	public static final String PAGE_PARAMS = "PAGE_PARAMS";
	
	/**总记录条数*/
	private int count;
	/**页大小*/
	private int pageSize;
	/**当前页*/
	private int currentPage;
	/**总页数*/
	private int totalPage;
	/**排序*/
	private String orderBy;
	/**显示多少条记录，默认为10个*/
	public static final int SHOW_PAGE_NUMER = 10;
	/**初始页码  默认1*/
	public static final int INIT_PAGE_NUM = 1;

	public PageModel(){
		
	}
	
	public PageModel(int pageSize, int currentPage) {
		this.pageSize = pageSize;
		this.currentPage = currentPage;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getTotalPage() {
//		totalPage = count / pageSize;
//		if ((count % pageSize) != 0){
//			totalPage = totalPage + 1;
//		}
		
		// 逢一进一高效分页公式：总页数 = （总条数 - 1） / 每页查询条数 + 1
		totalPage = (count - 1) / pageSize + 1;
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	
	/**
	 * 初始化分页对象
	 * @param request
	 * @return
	 */
	public static PageModel initPage(HttpServletRequest request) {
        String currentPage = request.getParameter("currentPage"); // 当前页
        String pageSize = request.getParameter("pageSize"); // 显示条数
        
        int pageNumInt = INIT_PAGE_NUM;
        int pageSizeInt = SHOW_PAGE_NUMER;
        try{
        	pageNumInt = Integer.parseInt(currentPage);
        }catch(Exception e){}
        try{
        	pageSizeInt = Integer.parseInt(pageSize);
        }catch(Exception e){}
        if (pageNumInt < 1) {
            pageNumInt = 1;
        }
        if (pageSizeInt < 1) {
        	pageSizeInt = 20;
        }
        
        PageModel pageModel = new PageModel(pageSizeInt, pageNumInt-1);
        PageContext.setPageModel(pageModel);
        return pageModel;
    }
}
