package com.assignsecurities.domain.dm;

import java.io.Serializable;

//@Entity
//@Table(name = "adhc_filters")
public class AdhocFilterModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2451068560854570393L;

//	@Id
//	@Column(name = "ID", nullable = false)
//	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	private Long id;

//	@Column(name = "NAME")
	private String name;

//	@Column(name = "CODE")
	private String code;

//	@Column(name = "FILTER_TYPE")
	private String filterType;

//	@Column(name = "QUERY")
	private String query;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the filterType
	 */
	public String getFilterType() {
		return filterType;
	}

	/**
	 * @param filterType
	 *            the filterType to set
	 */
	public void setFilterType(String filterType) {
		this.filterType = filterType;
	}

	/**
	 * @return the query
	 */
	public String getQuery() {
		return query;
	}

	/**
	 * @param query
	 *            the query to set
	 */
	public void setQuery(String query) {
		this.query = query;
	}

}
