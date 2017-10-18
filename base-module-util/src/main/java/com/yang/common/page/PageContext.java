package com.yang.common.page;

public class PageContext {

	private static ThreadLocal<PageModel> pageModelThreadLocal = new ThreadLocal<PageModel>();

	public static PageModel getPageModel() {
		return pageModelThreadLocal.get();
	}

	public static void setPageModel(PageModel pageModel) {
		pageModelThreadLocal.set(pageModel);
	}
	
	public static void remove(){
		pageModelThreadLocal.remove();
	}
}
