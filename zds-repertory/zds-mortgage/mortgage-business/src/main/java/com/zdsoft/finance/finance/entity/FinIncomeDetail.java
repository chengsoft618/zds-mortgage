package com.zdsoft.finance.finance.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.zdsoft.framework.core.common.domain.BaseEntity;

/**
 * 版权所有：重庆正大华日软件有限公司
 * @Title: FinIncomeDetail.java 
 * @ClassName: FinIncomeDetail 
 * @Description: 收款单明细
 * @author longwei 
 * @date 2017年2月16日 下午2:31:45 
 * @version V1.0
 */
@Entity
@Table(name="fin_income_detail")
public class FinIncomeDetail extends BaseEntity{

	/**
	 * 描述
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 收款单id
	 */
	@Column(length=32)
	private String finIncomeId;
	
	
	/**
	 * 费用id
	 */
	@Column(length = 32)
	private String feeId;
	
	/**
	 * 费用类型
	 */
	@Column(length = 32)
	private String feeType;

	/**
	 * 费用类型名称
	 */
	@Column(length = 32)
	private String feeTypeName;
	
	/**
	 * 收费项目
	 */
	@Column(length=50)
	private String feeItem;
	
	/**
	 * 收费项目名称
	 */
	@Column(length=50)
	private String feeItemName;
	
	/**
	 * 收款主体
	 */
	@Column(length=32)
	private String incomeSubject;
	
	/**
	 * 收款主体姓名
	 */
	@Column(length=100)
	private String incomeSubjectText;
	
	/**
	 * 应收金额
	 */
	@Column(precision=18,scale=6)
	private BigDecimal expectedAmount=BigDecimal.ZERO;
	
	/**
	 * 实收金额
	 */
	@Column(precision=18,scale=6)
	private BigDecimal receivedAmount=BigDecimal.ZERO;
	
	/**
	 * 应付金额
	 */
	@Column(precision = 18, scale = 2)
	private BigDecimal expectedPayableAmount = BigDecimal.ZERO;
	
	/**
	 * 实付金额
	 */
	@Column(precision=18,scale=6)
	private BigDecimal paidAmount=BigDecimal.ZERO;
	
	/**
	 * 收费对象
	 */
	@Column(length = 512)
	private String feeObjectId;

	/**
	 * 收费对象名称
	 */
	@Column(length = 512)
	private String feeeObjectName;

	/**
	 * 收费对象类别
	 */
	@Column(length = 20)
	private String feeObjectType;
	
	/**
	 * 付费对象
	 */
	@Column(length = 512)
	private String payObjectId;

	/**
	 * 付费对象名称
	 */
	@Column(length = 512)
	private String payObjectName;

	/**
	 * 付费对象类别
	 */
	@Column(length = 20)
	private String payObjectType;
	
	
	public String getFeeId() {
		return feeId;
	}

	public void setFeeId(String feeId) {
		this.feeId = feeId;
	}

	public String getFinIncomeId() {
		return finIncomeId;
	}

	public void setFinIncomeId(String finIncomeId) {
		this.finIncomeId = finIncomeId;
	}

	public String getFeeType() {
		return feeType;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}

	public String getFeeTypeName() {
		return feeTypeName;
	}

	public void setFeeTypeName(String feeTypeName) {
		this.feeTypeName = feeTypeName;
	}

	public String getIncomeSubject() {
		return incomeSubject;
	}

	public void setIncomeSubject(String incomeSubject) {
		this.incomeSubject = incomeSubject;
	}

	public BigDecimal getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getFeeItem() {
		return feeItem;
	}

	public void setFeeItem(String feeItem) {
		this.feeItem = feeItem;
	}

	public BigDecimal getExpectedAmount() {
		return expectedAmount;
	}

	public void setExpectedAmount(BigDecimal expectedAmount) {
		this.expectedAmount = expectedAmount;
	}

	public BigDecimal getReceivedAmount() {
		return receivedAmount;
	}

	public void setReceivedAmount(BigDecimal receivedAmount) {
		this.receivedAmount = receivedAmount;
	}

	public String getIncomeSubjectText() {
		return incomeSubjectText;
	}


	public String getFeeItemName() {
		return feeItemName;
	}

	public void setFeeItemName(String feeItemName) {
		this.feeItemName = feeItemName;
	}

	public void setIncomeSubjectText(String incomeSubjectText) {
		this.incomeSubjectText = incomeSubjectText;
	}

	public BigDecimal getExpectedPayableAmount() {
		return expectedPayableAmount;
	}

	public void setExpectedPayableAmount(BigDecimal expectedPayableAmount) {
		this.expectedPayableAmount = expectedPayableAmount;
	}

	public String getFeeObjectId() {
		return feeObjectId;
	}

	public void setFeeObjectId(String feeObjectId) {
		this.feeObjectId = feeObjectId;
	}

	public String getFeeeObjectName() {
		return feeeObjectName;
	}

	public void setFeeeObjectName(String feeeObjectName) {
		this.feeeObjectName = feeeObjectName;
	}

	public String getFeeObjectType() {
		return feeObjectType;
	}

	public void setFeeObjectType(String feeObjectType) {
		this.feeObjectType = feeObjectType;
	}

	public String getPayObjectId() {
		return payObjectId;
	}

	public void setPayObjectId(String payObjectId) {
		this.payObjectId = payObjectId;
	}

	public String getPayObjectName() {
		return payObjectName;
	}

	public void setPayObjectName(String payObjectName) {
		this.payObjectName = payObjectName;
	}

	public String getPayObjectType() {
		return payObjectType;
	}

	public void setPayObjectType(String payObjectType) {
		this.payObjectType = payObjectType;
	}
	
}
