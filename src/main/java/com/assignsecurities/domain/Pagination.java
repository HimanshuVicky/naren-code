package com.assignsecurities.domain;

import java.util.List;

public class Pagination<T> {
	
	public static Long DEFAULT_PAGE_SIZE=30L;
	
	public static String SORT_ORDER_ASC="asc";
	
	public static String SORT_ORDER_DESC="desc";

	private Long currPageNumber=0L;
	
	private Long pageSize=DEFAULT_PAGE_SIZE;
	
	private Long totalRecords;
	
	private List<T> list;
	
	private List<OrderBy> orderBies;
	

	/**
	 * @return the currPageNumber
	 */
	public Long getCurrPageNumber() {
		return currPageNumber;
	}

	/**
	 * @param currPageNumber the currPageNumber to set
	 */
	public void setCurrPageNumber(Long currPageNumber) {
		this.currPageNumber = currPageNumber;
	}

	/**
	 * @return the pageSize
	 */
	public Long getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(Long pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @return the totalRecords
	 */
	public Long getTotalRecords() {
		if(totalRecords==null) {
			return 0L;
		}
		return totalRecords;
	}

	/**
	 * @param totalRecords the totalRecords to set
	 */
	public void setTotalRecords(Long totalRecords) {
		this.totalRecords = totalRecords;
	}

	/**
	 * @return the list
	 */
	public List<T> getList() {
		return list;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(List<T> list) {
		this.list = list;
	}

	/**
	 * @return the orderBies
	 */
	public List<OrderBy> getOrderBies() {
		return orderBies;
	}

	/**
	 * @param orderBies the orderBies to set
	 */
	public void setOrderBies(List<OrderBy> orderBies) {
		this.orderBies = orderBies;
	}
	
}
