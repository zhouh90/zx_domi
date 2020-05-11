package com.zx.entity;

import java.util.Date;

/**
 * @author zhou.hao
 * @email zhouhao@ule.com
 * @createTime 2020年4月21日 下午3:19:42
 * @Description 
 */

public class ZxEntity {

	private int id;
	private int flag;
	private Date time;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "ZxEntity [id=" + id + ", flag=" + flag + ", time=" + time + "]";
	}

}
