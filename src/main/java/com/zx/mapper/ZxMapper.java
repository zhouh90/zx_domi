package com.zx.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zx.entity.ZxEntity;

/**
 * @author zhou.hao
 * @email zhouhao@ule.com
 * @createTime 2020年4月21日 下午2:00:03
 * @Description 
 */

public interface ZxMapper {

	ZxEntity getLast();

	void saveNew(ZxEntity entity);

	ZxEntity getMaxEntityOfLastDay(@Param("startOfDay") Date startOfDay);

	ZxEntity getMinEntityOfNextDay(@Param("endOfDay") Date endOfDay);

	ZxEntity getMaxEntityOfToday(@Param("endOfDay") Date endOfDay);

	List<ZxEntity> queryBetweenIds(@Param("startId") int startId, @Param("endId") int endId);

	List<String> distinctDate();

	List<ZxEntity> getEntitysByStartDay(@Param("startDay") Date startDay);

}
