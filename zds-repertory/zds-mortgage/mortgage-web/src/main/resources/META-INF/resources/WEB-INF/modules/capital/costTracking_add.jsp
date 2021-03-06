<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
<%@ include file='../common/common_js.jsp'%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<title>信托计划借方资金（非放款）跟踪</title>
</head>
<body>
<div class="page-box">
	<div class="p10">
		<form id="credit_entrust_form" class="zui-form " method="post"
			enctype="multipart/form-data">
			<input type="hidden" id="tempUuid" name="tempUuid" value="${tempUuid }"/>
			<input type="hidden" id="id" name="id" value="${creditCostTrackingVo.id }"/>
			<input type="hidden" id="creditEntrustId" name="creditEntrustId" value="${creditEntrustId}"/>
			<div class="page-box">
				<div class="page-title">基本信息</div>
				<dl class="form-item">
					<dt class="title"><b class="c-red mr5">*</b>支出类型：</dt>
					<dd class="detail">
						<input class="zui-combobox zui-validatebox" id="expenditureType" name="expenditureType" type="hidden" value="${creditCostTrackingVo.expenditureType }"
                              data-url="<z:res resource="public.simplecode.selector" isDefault="true"/>&jsoncallback=?&target=true&categoryCd=YWDM00133"
                              data-valuefield="fullcode" data-callback="getFeeItems"  data-textfield="name" validate-type="Require">
					</dd>
				</dl>
				<dl class="form-item">
					<dt class="title"><b class="c-red mr5">*</b>名称：</dt>
					<dd class="detail">
						<label> <input class="zui-input zui-validatebox" validate-type="Require,Length[1-256]"
							 id="costName" value="${creditCostTrackingVo.costName }" name="costName">
						</label>
					</dd>
				</dl>
				<dl class="form-item block">
                	<dt class="title">摘要：</dt>
	                <dd class="detail">
		                <label>
		                	<textarea class="zui-area zui-validatebox" id="summary"  name="summary" validate-type="Length[0-2000]" placeholder="最多可以输入2000个字符">${creditCostTrackingVo.summary }</textarea>
		                </label>
	                </dd>
                </dl>
               <div id="displayDiv" >
					<table class="table-index">
						<c:if test="${fn:length(feeItemVos)>0}">
							<tr><td>序号</td><td>费用项目</td><td>金额(元)</td></tr>
						</c:if>
						<c:forEach var="feeItemVo" items="${feeItemVos }" varStatus="status">
							<tr>
								<td>${status.index + 1 }</td>
								<td>${feeItemVo.feeItemNm }</td>
								<td><input type="hidden" id="feeItemCd" name="feeItemCd" value="${feeItemVo.feeItemCd }"/>
									<input type="hidden" id="feeItemNm" name="feeItemNm" value="${feeItemVo.feeItemNm }"/> 
									<input class="zui-input zui-validatebox" onchange="sumAmount();" validate-type="Require,Digital[18-2]" validate-false="请输入必填项|请输入正确的金额" value="${feeItemVo.feeAmount }"  name="feeAmount"/>
								</td>
							</tr>
						</c:forEach>
					</table>
				</div>
                <div class="m10">
	                <table class="table-index">
	                </table>
                </div>
				<dl class="form-item">
					<dt class="title">总计(元)：</dt>
					<dd class="detail">
						<label> <input class="zui-input zui-disabled zui-validatebox" readonly="readonly" validate-type="Require,Digital[18-12]"
							 id="totalAmount" value="${creditCostTrackingVo.totalAmount }" name="totalAmount">
						</label>
					</dd>
				</dl>
				<dl class="form-item"></dl>
				<dl class="form-item"></dl>
				<dl class="form-item">
					<dt class="title"><b class="c-red mr5">*</b>应付日期：</dt>
					<dd class="detail">
						 <label>
                            <input type="text" id="payDateLocal" class="zui-input strToDate  zui-validatebox " validate-type="Require" value="${creditCostTrackingVo.payDate }" onclick="WdatePicker({realDateFmt:'yyyyMMdd',vel:'payDate'})">
                            <input type="hidden" id="payDate" value="${creditCostTrackingVo.payDate }" name = "payDate" />
                         </label>
					</dd>
				</dl>
				<dl class="form-item block">
                	<dt class="title">备注：</dt>
	                <dd class="detail">
		                <label>
		                	<textarea class="zui-area zui-validatebox" id="remark" name="remark" validate-type="Length[0-2000]" placeholder="最多可以输入200个字符">${creditCostTrackingVo.remark }</textarea>
		                </label>
	                </dd>
                </dl>	
			</div>
			<div class="page-box">
				<div class="page-title">操作日志</div>
				<div class="p10">
					<div id="log_datagrid" class="zui-datagrid" zdata-options='{"url":"<z:ukey key='com.zdsoft.finance.capital.getCreditLogs' context='admin'/>&jsoncallback=?&businessId|E|S=${creditCostTrackingVo.id }","singleSelect":false,"pagination":true,"idField":"id","tableCls":"table-index"}'>
						<table>
							<thead>
			        			<tr>
			            			<th data-options="field:operationTypeName">操作类型</th>
			            			<th data-options="field:operationContent">操作内容</th>
			            			<th data-options="field:operationEmpName">处理人</th>
			            			<!-- <th data-options="field:remark">备注</th> -->
			            			<th data-options="field:operationDateName">操作时间</th>
						        </tr>
							</thead>
						</table>
					</div>
				</div>
			</div>
			
            <div class="form-btn">
               	<button id="saveBtn" type="button" class="btn-blue">提交</button>
            </div>
		</form>
	</div>
</div>
<div id="zds_btn_selecter"></div>
<div id="chooseMember"></div>
		<script type="text/javascript">
		seajs.use(['jquery','zd/jquery.zds.page.callback','zd/jquery.zds.form','zd/jquery.zds.message','zd/jquery.zds.dialog','zd/jquery.zds.combobox','zd/jquery.zds.table','zd/jquery.zds.seleter','zd/jquery.zds.validate'], function($, CALLBACK) {
			var tempData = '';
			
			// 获取费用列表
			CALLBACK.getFeeItems = function(index,rowData){
				if(index){
					$.ajax({
                        type: 'post',
                        url: '<z:ukey key="com.zdsoft.finance.capital.getFeeItems" context="admin"/>&jsoncallback=?&attribution='+index,
                        data: {},
                        dataType: 'json',
                        success: function (data) {
                            if (data.resultStatus == 0) {
                            	if(data.rows.length != 0){
                            		tempData = data.rows;
                            		var appendHtml = '<table class="table-index"><tr><td>序号</td><td>费用项目</td><td>金额(元)</td></tr>';
                            		for(var i=0;i<data.rows.length;i++){
										var rows = data.rows;
										appendHtml +=  '<tr><td>'+(i+1)+'</td><td>'+rows[i].feeItemNm+'</td><td><input type="hidden" id="feeItemCd" name="feeItemCd" value="'+rows[i].feeItemCd +'"/><input type="hidden" id="feeItemNm"  name="feeItemNm" value="'+rows[i].feeItemNm+'"/><dl class="form-item"><dd class="detail"><label> <input class="zui-input zui-validatebox" onchange="sumAmount();" validate-type="Require,Digital[18-2]" validate-false="请输入必填项|请输入正确的金额" value="" data-toggle="validate"  name="feeAmount"/></label></dd></dl></td></tr>';
                            		}
                            		appendHtml += '</table>';
                            		$('#displayDiv').html('');
                            		$('#displayDiv').html(appendHtml);
                            		$('[data-toggle="validate"]').ZDSValidatebox();
                            	}
                            }else{
                            	$.ZMessage.error("错误", data.msg, function () {
		                            $(".zd-message").ZWindow("close");
		                        });
                            }
                        },
                        error: function () {
                        	$.ZMessage.error("错误", "系统异常，请联系管理员", function () {
	                            $(".zd-message").ZWindow("close");
	                        });
                        }
                    }); 
				}else{
					$('#displayDiv').html('');
            	}
			};
			
			// 初始化
			$.ZUI.init();
			$.ZUI.strToDate();
			
			// 计算总金额
			window.sumAmount = function(){
				var totalAmount = 0;
				var feeAmounts = $('input[name="feeAmount"]');
			    for(var i = 0;i<feeAmounts.length;i ++){
					if(parseFloat($(feeAmounts[i]).val())){
						totalAmount += parseFloat($(feeAmounts[i]).val());
					}
				}
				$('#totalAmount').val(totalAmount);
			};
			
			// 保存按钮点击
			$('#saveBtn').click(function(){
				var isValidate = $.ZUI.validateForm($('#credit_entrust_form'));
				if(isValidate){
					var param = $('#credit_entrust_form').serialize();
					$.ajax({
	                    type: 'post',
	                    url: '<z:ukey key="com.zdsoft.finance.capital.saveCostTracking" context="admin"/>',
	                    data: param,
	                    dataType: 'json',
	                    success: function (data) {
	                        if (data.resultStatus == 0) {
	                        	$.ZMessage.success("成功", data.msg, function () {
	                        		setTimeout(function(){
	                                	ZDS_MESSAGE_CLIENT.refreshOpenner();
	                               	    ZDS_MESSAGE_CLIENT.closeSelf();
	                                },200);
		                        });
	                        }else{
	                        	$.ZMessage.error("错误", data.msg, function () {
		                            $(".zd-message").ZWindow("close");
		                        });
	                        }
	                    },
	                    error: function () {
	                    	$.ZMessage.error("错误", "系统异常，请联系管理员", function () {
	                            $(".zd-message").ZWindow("close");
	                        });
	                    }
	                }); 
				}
			});
		});
	</script>
</body>
</html>