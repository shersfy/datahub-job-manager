package org.shersfy.datahub.jobmanager.service;

import java.io.Serializable;
import java.util.List;

import org.shersfy.datahub.commons.beans.Page;
import org.shersfy.datahub.commons.beans.Result.ResultCode;
import org.shersfy.datahub.jobmanager.i18n.I18nCodes;
import org.shersfy.datahub.jobmanager.model.BaseEntity;

public interface BaseService<T extends BaseEntity, Id extends Serializable> extends I18nCodes {
    
    /**删除标志-正常**/
    public final int NOR    = 0;
    /**删除标志-已删除**/
    public final int DEL    = 1;
    /**删除标志-临时数据**/
    public final int TMP    = -1;
    /**处理成功**/
    public final int SUCESS = ResultCode.SUCESS;
    /**处理失败**/
    public final int FAIL   = ResultCode.FAIL;
	
	int deleteById(Id id);
	
	int deleteByIds(List<Id> ids);

	int insert(T entity);

	T findById(Id id);

	int updateById(T entity);
	
	
	long findListCount(T where);
	
	List<T> findList(T where);

	Page<T> findPage(T where, int pageNum, int pageSize);
}
