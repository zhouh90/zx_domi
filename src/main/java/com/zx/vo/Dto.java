package com.zx.vo;

import com.zx.entity.ZxEntity;

/**
 * @author zhou.hao
 * @email zhouhao@ule.com
 * @createTime 2020年4月27日 上午11:17:30
 * @Description 
 */

public class Dto {

	public Dto(int index) {
		this.index = index;
	}

	private ZxEntity entity;
	private int index;

	public ZxEntity getEntity() {
		return entity;
	}

	public void setEntity(ZxEntity entity) {
		this.entity = entity;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
