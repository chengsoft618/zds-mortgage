package com.zdsoft.finance.customer.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zdsoft.essential.client.service.CED;
import com.zdsoft.essential.dto.emp.EmpDto;
import com.zdsoft.finance.base.service.impl.BaseServiceImpl;
import com.zdsoft.finance.common.exception.AppExistMainCustomerException;
import com.zdsoft.finance.common.exception.AppOntExistMainCustomerException;
import com.zdsoft.finance.customer.entity.BeforeAddress;
import com.zdsoft.finance.customer.entity.BeforeContact;
import com.zdsoft.finance.customer.entity.BeforePersonalAssociation;
import com.zdsoft.finance.customer.entity.BeforePersonalCustomer;
import com.zdsoft.finance.customer.entity.BeforeWorkUnit;
import com.zdsoft.finance.customer.entity.CustomerCreateType;
import com.zdsoft.finance.customer.repository.BeforePersonalCusomerRepository;
import com.zdsoft.finance.customer.service.BeforeAddressService;
import com.zdsoft.finance.customer.service.BeforeContactService;
import com.zdsoft.finance.customer.service.BeforePersonalAssociationService;
import com.zdsoft.finance.customer.service.BeforePersonalCustomerService;
import com.zdsoft.finance.customer.service.BeforeWorkUnitService;
import com.zdsoft.finance.marketing.entity.CaseApply;
import com.zdsoft.finance.marketing.entity.CaseApplyBeforeCustomer;
import com.zdsoft.finance.marketing.service.CaseApplyBeforeCustomerService;
import com.zdsoft.finance.marketing.service.CaseApplyService;
import com.zdsoft.framework.core.common.util.ObjectHelper;
import com.zdsoft.framework.cra.client.service.CRA;
import com.zdsoft.framework.cra.dto.AccountDTO;
/**
 * 
 * 版权所有：重庆正大华日软件有限公司
 * @Title: BeforePersonalCustomerServiceImpl.java 
 * @ClassName: BeforePersonalCustomerServiceImpl 
 * @Description: 个人客户service实现类
 * @author xj 
 * @date 2017年3月6日 下午6:15:11 
 * @version V1.0
 */
@Service("BeforePersonalCustomerService")
public class BeforePersonalCustomerServiceImpl  extends BaseServiceImpl<BeforePersonalCustomer, BeforePersonalCusomerRepository>  implements BeforePersonalCustomerService  {


	@Autowired
	private BeforePersonalAssociationService beforePersonalAssociationService;
	@Autowired
	private CaseApplyBeforeCustomerService caseApplyBeforeCustomerService;
	@Autowired
	private CaseApplyService caseApplyService;
	@Autowired
	private BeforeAddressService beforeAddressService;
	@Autowired
	private BeforeWorkUnitService beforeWorkUnitService;
	@Autowired
	private BeforeContactService beforeContactService;
	@Autowired
	private CRA CRA;
	@Autowired
	private CED CED;
	@Override 
	public List<BeforePersonalCustomer> loadCustomerByCredentiaTypeAndCredentialNo(String credentialType,
			String credentialNo) {
		List<BeforePersonalCustomer> beforePersonalCustomer = this.customReposity.findByCredentialTypeAndCredentialNoOrderByUpdateTimeDesc(credentialType, credentialNo);
		return beforePersonalCustomer;
	}

	@Override
	public List<BeforePersonalCustomer> queryRelationCustomer(String id,String relationship) {
		if(ObjectHelper.isEmpty(id) || ObjectHelper.isEmpty(relationship)){
			return null;
		}
		List<BeforePersonalCustomer> beforePersonalCustomers = new ArrayList<BeforePersonalCustomer>();
		//客户关系中间表
		List<BeforePersonalAssociation> beforePersonalAssociations = beforePersonalAssociationService.findByRelationshipAndCustomerId(relationship, id);
		if(ObjectHelper.isNotEmpty(beforePersonalAssociations)){
			for (BeforePersonalAssociation beforePersonalAssociation : beforePersonalAssociations) {
				beforePersonalCustomers.add(beforePersonalAssociation.getBeforePersonalCusomer());
			}
		}
		return beforePersonalCustomers;
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public BeforePersonalCustomer saveOrUpdateCustomer(BeforePersonalCustomer beforePersonalCustomer,String caseApplyId) throws Exception {
			CaseApply caseApply = caseApplyService.findOne(caseApplyId);
		
			//保存客户信息
			BeforePersonalCustomer cust = this.saveOrUpdate(beforePersonalCustomer);
			//建立关系
			caseApplyBeforeCustomerService.setJoinType(cust, caseApply, beforePersonalCustomer.getJoinType());
			//保存工作单位
			List<BeforeWorkUnit> beforeWorkUnits = beforePersonalCustomer.getBeforeWorkUnits();
			beforeWorkUnitService.saveOrUpdateWorkUnit(beforeWorkUnits, cust.getId(),null);
			
			//保存或者修改联系方式
			List<BeforeContact> beforeContacts = beforePersonalCustomer.getBeforeContacts();
			beforeContactService.saveOrUpdateContact(beforeContacts, cust.getId(),null);
			
			//保存地址
			List<BeforeAddress> homeAddress = beforePersonalCustomer.getHomeAddress();
			beforeAddressService.saveOrUpdateAddress(homeAddress, cust.getId(),null);
			//删除他和配偶之间的关系
			//首先先配偶删除以前的关系
			 List<BeforePersonalAssociation> spouses = this.beforePersonalAssociationService.findByRelationshipAndCustomerId(BeforePersonalAssociation.SPOUSE, cust.getId());
			if(ObjectHelper.isNotEmpty(spouses)){
					beforePersonalAssociationService.delete(spouses);
					//删除参与类型
					caseApplyBeforeCustomerService.deleteByCustomerIdAndCaseApplyId(spouses.get(0).getBeforePersonalCusomer().getId(), caseApply.getId());
			}
			
			//配偶
			BeforePersonalCustomer spouse = beforePersonalCustomer.getSpouse();
			if(ObjectHelper.isNotEmpty(spouse)){
				//设置配偶婚姻状况
				spouse.setMaritalStatus(beforePersonalCustomer.getMaritalStatus());
				//保存配偶
				BeforePersonalCustomer spCust = this.saveOrUpdate(spouse);
				//把配偶信息返回回去
				cust.setSpouse(spCust);
				//保存工作单位
				List<BeforeWorkUnit> spBeforeWorkUnits = spouse.getBeforeWorkUnits();
				beforeWorkUnitService.saveOrUpdateWorkUnit(spBeforeWorkUnits, spCust.getId(),null);
				
				//保存或者修改联系方式
				List<BeforeContact> spReforeContacts = spouse.getBeforeContacts();
				beforeContactService.saveOrUpdateContact(spReforeContacts, spCust.getId(),null);
				
				//保存地址
				List<BeforeAddress> has = spouse.getHomeAddress();
				beforeAddressService.saveOrUpdateAddress(has,spCust.getId(),null);
				
				//建立配偶关系
				beforePersonalAssociationService.setCustomerAssociation(cust.getId(), spCust, BeforePersonalAssociation.SPOUSE);
				//保存参与类型
				//配偶参与类型
				String spJoinType = spouse.getJoinType();
				if(ObjectHelper.isNotEmpty(spJoinType)){
					caseApplyBeforeCustomerService.setJoinType(spCust, caseApply, spJoinType);
				}
				
		}
		return cust;
	}

	@Override
	public List<BeforePersonalCustomer> queryCustomerByCaseApplyIdAndJoinType(String caseApplyId, String joinType) {
		List<CaseApplyBeforeCustomer> caseApplyBeforeCustomers = caseApplyBeforeCustomerService.queryByCaseApplyIdAndJoinType(caseApplyId, joinType);
		if(ObjectHelper.isNotEmpty(caseApplyBeforeCustomers)){
			List<BeforePersonalCustomer> customers = new ArrayList<BeforePersonalCustomer>();
			for (CaseApplyBeforeCustomer caseApplyBeforeCustomer : caseApplyBeforeCustomers) {
				customers.add((BeforePersonalCustomer)caseApplyBeforeCustomer.getBeforeCustomer());
			}
			return customers;
		}
		return null;
	}

	/**
	 * 
	 * 保存或者修改客户
	 *
	 * @author xj
	 * @param beforePersonalCustomer
	 * @return
	 * @throws Exception 
	 */
	private BeforePersonalCustomer saveOrUpdate(BeforePersonalCustomer beforePersonalCustomer) throws Exception{
		BeforePersonalCustomer customer = new BeforePersonalCustomer();
		//保存主客户
		if(ObjectHelper.isNotEmpty(beforePersonalCustomer.getId())){
			//更新
			customer = this.customReposity.findOne(beforePersonalCustomer.getId());
		}
		this.copy(beforePersonalCustomer, customer);
		EmpDto loginUser = CED.getLoginUser();
		//创建人姓名
		String empNm = loginUser.getEmpNm();
		//创建人代码
		String empCd = loginUser.getEmpCd();
		//创建人公司代码
		String companyCd = loginUser.getCompanyCd();
		//创建人公司
		String companyNm = loginUser.getCompanyNm();
		//创建人部门
		String departmentName = loginUser.getDepartmentName();
		//创建人部门代码
		String departmentCd = loginUser.getDepartmentCd();
		if(ObjectHelper.isEmpty(customer.getId())){
			customer.setCreateType(customer.getCreateType());
			customer.setCompanyCode(companyCd);
			customer.setCompanyName(companyNm);
			customer.setDepartmentCode(departmentCd);
			customer.setDepartmentName(departmentName);
			customer.setCreatorName(empNm);
			customer.setCreateBy(empCd);
			
		}else{
			customer.setUpdateBy(empCd);
		}
		if(ObjectHelper.isNotEmpty(customer.getId())){
			return this.customReposity.updateEntity(customer);
		}
		return this.customReposity.saveEntity(customer);
		
	}
	/**
	 * 
	 * @Title: copy 
	 * @Description: 复制客户
	 * @author xj 
	 * @param beforePersonalCustomer 被复制客户
	 * @param customer 复制客户
	 * @throws Exception
	 */
	private void copy(BeforePersonalCustomer beforePersonalCustomer, BeforePersonalCustomer customer) throws Exception {
		//姓名
		customer.setCustomerName(beforePersonalCustomer.getCustomerName());
		//证件类型
		customer.setCredentialType(beforePersonalCustomer.getCredentialType());
		//证件号码
		customer.setCredentialNo(beforePersonalCustomer.getCredentialNo());
		//曾用名
		customer.setFormerName(beforePersonalCustomer.getFormerName());
		//出生日期
		customer.setBirthdayDate(beforePersonalCustomer.getBirthdayDate());
		//性别
		customer.setGender(beforePersonalCustomer.getGender());
		//个人年收入
		customer.setAnnualIncomeAmmount(beforePersonalCustomer.getAnnualIncomeAmmount());
		//婚姻状况
		customer.setMaritalStatus(beforePersonalCustomer.getMaritalStatus());
		//职业类型
		customer.setCareerType(beforePersonalCustomer.getCareerType());
		//受教育程度
		customer.setDegree(beforePersonalCustomer.getDegree());
		//居住年限
		customer.setLiveAge(beforePersonalCustomer.getLiveAge());
		//邮箱地址
		customer.setEmail(beforePersonalCustomer.getEmail());
		//是否是实际用款人
		customer.setActualUsePerson(beforePersonalCustomer.getActualUsePerson());
		// 参与类型 共借人、担保人
		customer.setJoinType(beforePersonalCustomer.getJoinType());
		//头像
		if(ObjectHelper.isNotEmpty(beforePersonalCustomer.getAttachmentId())){
			customer.setAttachmentId(beforePersonalCustomer.getAttachmentId());
		}
	}

	/**
	 * 
	 * 保存或者修改客户app
	 *
	 * @author xj
	 * @param beforePersonalCustomer 客户
	 * @param token app登录token
	 * @return
	 * @throws Exception 
	 */
	private BeforePersonalCustomer saveOrUpdateApp(BeforePersonalCustomer beforePersonalCustomer,String token) throws Exception{
		BeforePersonalCustomer customer = beforePersonalCustomer;
		//保存主客户
		if(ObjectHelper.isNotEmpty(beforePersonalCustomer.getId())){
			//更新
			customer = this.customReposity.getOne(beforePersonalCustomer.getId());
		}
		AccountDTO account = CRA.getAccount(token);
		EmpDto loginUser = CED.getLoginUser(account.getId());
		String companyCd = loginUser.getCompanyCd();
		String companyNm = loginUser.getCompanyNm();
		String departmentCd = loginUser.getDepartmentCd();
		String departmentName = loginUser.getDepartmentName();
		String empCd = loginUser.getEmpCd();
		String empNm = loginUser.getEmpNm();
		
		this.copy(beforePersonalCustomer, customer);
		if(ObjectHelper.isNotEmpty(customer.getId())){
			customer.setUpdateBy(empCd);
			return this.customReposity.updateEntity(customer);
		}
		customer.setCreateBy(empCd);
		customer.setCreatorName(empNm);
		customer.setDepartmentCode(departmentCd);
		customer.setDepartmentName(departmentName);
		customer.setCompanyCode(companyCd);
		customer.setCompanyName(companyNm);
		return this.customReposity.saveEntity(customer);
		
	}
	
	@Transactional(rollbackFor=Exception.class)
	@Override
	public String saveOrUpdateAppCustomer(String caseApplyId,BeforePersonalCustomer customer,
			List<BeforeAddress> allAddress, List<BeforeWorkUnit> beforeWorkUnits, List<BeforeContact> contacts,boolean isSpouse,
			String token) throws Exception{
		if(ObjectHelper.isEmpty(caseApplyId) ||  ObjectHelper.isEmpty(token)){
			logger.error(" caseApplyId or token is null ");
			throw new Exception("caseApplyId or token is null");
		}
		//判断是否是主借人
		String joinType = customer.getJoinType();
		List<BeforePersonalCustomer> beforePersonalCustomers = this.queryCustomerByCaseApplyIdAndJoinType(caseApplyId, CaseApplyBeforeCustomer.MAIN_BORROW);
		//要添加为主借人,如果是则判断是否存在主借人，如果存在则不能添加
		if(CaseApplyBeforeCustomer.MAIN_BORROW.equals(joinType)){
			//判断是否有主借人且不是他自己
			if(ObjectHelper.isNotEmpty(beforePersonalCustomers) && !beforePersonalCustomers.get(0).getId().equals(customer.getId())){
				logger.error(" 添加主借人失败，该案件已经存在主借人 ");
				throw new AppExistMainCustomerException();
			}
		}else if(ObjectHelper.isEmpty(beforePersonalCustomers) && !isSpouse){
			logger.error(" 添加主借人失败，该案件不存在主借人 ");
			throw new AppOntExistMainCustomerException();
		}
		
		//案件
		CaseApply caseApply = caseApplyService.findOne(caseApplyId);
		//创建类型
		customer.setCreateType(CustomerCreateType.APP.value());
		//保存的客户
		BeforePersonalCustomer personalCustomer = this.saveOrUpdateApp(customer,token);
		//设置该客户的参与类型
		if(ObjectHelper.isNotEmpty(joinType)){
			caseApplyBeforeCustomerService.setJoinType(personalCustomer, caseApply, joinType);
		}
		
		//案件的主借人
		if(CaseApplyBeforeCustomer.MAIN_BORROW.equals(joinType)){
			BeforePersonalCustomer mainCustomer = this.queryCustomerByCaseApplyIdAndJoinType(caseApplyId, CaseApplyBeforeCustomer.MAIN_BORROW).get(0);
			//如果这个客户不是主借人
			if((personalCustomer!=mainCustomer)){
				//设置客户与主借的关系
				beforePersonalAssociationService.setCustomerAssociation(mainCustomer.getId(), personalCustomer, personalCustomer.getRelationship());
			}
		}
		
		//新增客户的配偶id，用于建立关系
		String spouseId = customer.getSpouseId();
		//设置配偶关系
		if(ObjectHelper.isNotEmpty(spouseId)){
			//配偶
			BeforePersonalCustomer findOne = this.customReposity.findOne(spouseId);
			//先删除配偶关系
			List<BeforePersonalAssociation> beforePersonalAssociation1 = beforePersonalAssociationService.findByRelationshipAndCustomerId(spouseId, BeforePersonalAssociation.SPOUSE);
			List<BeforePersonalAssociation> beforePersonalAssociation2 = beforePersonalAssociationService.findByRelationshipAndCustomerId(personalCustomer.getId(), BeforePersonalAssociation.SPOUSE);
			if(ObjectHelper.isNotEmpty(beforePersonalAssociation1)){
				beforePersonalAssociationService.delete(beforePersonalAssociation1);
			}
			if(ObjectHelper.isNotEmpty(beforePersonalAssociation2)){
				beforePersonalAssociationService.delete(beforePersonalAssociation2);
			}
			if(isSpouse){
				//保存的是配偶
				beforePersonalAssociationService.setCustomerAssociation(spouseId, personalCustomer, BeforePersonalAssociation.SPOUSE);
			}else{
				beforePersonalAssociationService.setCustomerAssociation(personalCustomer.getId(), findOne, BeforePersonalAssociation.SPOUSE);
			}
		}
		
		//设置地址
		beforeAddressService.saveOrUpdateAddress(allAddress, personalCustomer.getId(),token);
		//设置工作单位
		beforeWorkUnitService.saveOrUpdateWorkUnit(beforeWorkUnits, personalCustomer.getId(),token);
		//设置联系方式
		beforeContactService.saveOrUpdateContact(contacts, personalCustomer.getId(),token);
		logger.info("贷款申请-添加/修改客户成功");
		return personalCustomer.getId();
	}

	@Override
	public List<BeforePersonalCustomer> queryByCaseApplyId(String caseApplyId) {
		
		List<BeforePersonalCustomer> result = new ArrayList<BeforePersonalCustomer>();
		//所有的参与人
		List<BeforePersonalCustomer> cts = new ArrayList<BeforePersonalCustomer>();
		//所有参与类型
		List<CaseApplyBeforeCustomer> cabCustomers = caseApplyBeforeCustomerService.queryByCaseApplyId(caseApplyId);
		if(ObjectHelper.isNotEmpty(cabCustomers)){
			for (CaseApplyBeforeCustomer cc : cabCustomers) {
				BeforePersonalCustomer ct = (BeforePersonalCustomer) cc.getBeforeCustomer();
				ct.setJoinType(cc.getJoinType());
				cts.add(ct);
			}
		}
		//主借人
		List<BeforePersonalCustomer> mainBorrow = this.queryCustomerByCaseApplyIdAndJoinType(caseApplyId, CaseApplyBeforeCustomer.MAIN_BORROW);
		//与主借人关系
		if(ObjectHelper.isNotEmpty(mainBorrow)){
			BeforePersonalCustomer mainCustomer = mainBorrow.get(0);
			for (BeforePersonalCustomer ct : cts) {
				//和主借人关系对象
				BeforePersonalAssociation pa = beforePersonalAssociationService.findByCustomerIdAndBeforePersonalCusomerId(mainCustomer.getId(), ct.getId());
				if(ObjectHelper.isNotEmpty(pa)){
					ct.setRelationship(pa.getRelationship());
				}
				
			}
		}
		result.addAll(cts);
		//配偶单独提出，保持和页面一样
		for (BeforePersonalCustomer ct : cts) {
			//查询配偶
			List<BeforePersonalAssociation> bas = beforePersonalAssociationService.findByRelationshipAndCustomerId(BeforePersonalAssociation.SPOUSE, ct.getId());
			if(ObjectHelper.isNotEmpty(bas)){
				BeforePersonalAssociation ba = bas.get(0);
				BeforePersonalCustomer bc = ba.getBeforePersonalCusomer();
				result.remove(bc);
				ct.setSpouse(bc);
			}
		}
		
		//List 按照 主借人,共借人,担保人排序 add By Caixiekang
		List<BeforePersonalCustomer> sortList = new ArrayList<>();
		for (BeforePersonalCustomer beforePersonalCustomer : result) {
			if(CaseApplyBeforeCustomer.MAIN_BORROW.equals(beforePersonalCustomer.getJoinType())){
				sortList.add(beforePersonalCustomer);
			}
		}
		for (BeforePersonalCustomer beforePersonalCustomer : result) {
			if(CaseApplyBeforeCustomer.JOINTLY_PARTY.equals(beforePersonalCustomer.getJoinType())){
				sortList.add(beforePersonalCustomer);
			}
		}
		for(BeforePersonalCustomer beforePersonalCustomer : result){
			if(CaseApplyBeforeCustomer.GUARANTEE_PARTY.equals(beforePersonalCustomer.getJoinType())){
				sortList.add(beforePersonalCustomer);
			}
		}
		return sortList;
	}

	@Override
	public BeforePersonalCustomer findById(String id){
		return this.customReposity.findOne(id);
	}


	

}
