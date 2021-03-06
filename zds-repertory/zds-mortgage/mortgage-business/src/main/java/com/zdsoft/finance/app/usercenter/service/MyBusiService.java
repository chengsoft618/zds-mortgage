package com.zdsoft.finance.app.usercenter.service;

import java.util.List;
import java.util.Map;

import com.zdsoft.finance.app.usercenter.MyBusiInfoDto;
import com.zdsoft.finance.busiform.entity.BusiForm;
import com.zdsoft.framework.core.common.page.Pageable;

/**
 * 
 * 版权所有：重庆正大华日软件有限公司
 * @Title:MyBusiService.java
 * @Package:com.zdsoft.finance.app.usercenter
 * @Description:我的申请处理Service
 * @author: jingyh
 * @date:2017年1月13日 下午7:57:12
 * @version:v1.0
 */
public interface MyBusiService {
	
	/**
	 * @Title: findMyBusiPageInfo 
	 * @Description: 分页查询我的申请业务信息
	 * @author jingyh 
	 * @param params
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<MyBusiInfoDto> findMyBusiPageInfo(Map<String,Object> params, Pageable pageInfo) throws Exception;
	
	/**
	 * 
	 * 启动案件申请主流程
	 *
	 * @author jingyh
	 * @param caseApplyId
	 * @return
	 * @throws Exception
	 */
	public BusiForm startCaseApplyMainProcess(String caseApplyId) throws Exception;
}
