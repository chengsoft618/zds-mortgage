package com.zdsoft.finance.capital.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.zdsoft.finance.cooperator.entity.Capitalist;
import com.zdsoft.framework.core.common.domain.BaseEntity;

/**
 * 
 * 版权所有：重庆正大华日软件有限公司
 * @Title: InstitutionFunds.java 
 * @ClassName: InstitutionFunds 
 * @Description: 机构资金配置表
 * @author liuwei 
 * @date 2017年2月8日 上午10:24:45 
 * @version V1.0
 */
@Entity
@Table(name = "cptl_institution_funds")
public class InstitutionFunds extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 分公司编号
	 */
	@Column(length = 64)
	private String orgCd;

	/**
	 * 分公司名称
	 */
	@Column(length = 255)
	private String orgName;

	/**
	 * 资方
	 */
	@ManyToMany
	@JoinTable(name = "cptl_funds_capitalist")
	private List<Capitalist> capitalists;

	/**
	 * 备注
	 */
	@Column(length = 500)
	private String remark;

	public String getOrgCd() {
		return orgCd;
	}

	public void setOrgCd(String orgCd) {
		this.orgCd = orgCd;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public List<Capitalist> getCapitalists() {
		return capitalists;
	}

	public void setCapitalists(List<Capitalist> capitalists) {
		this.capitalists = capitalists;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
