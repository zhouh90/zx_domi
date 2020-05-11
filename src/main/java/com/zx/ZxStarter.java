package com.zx;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author zhou.hao
 * @email zhouhao@ule.com
 * @createTime 2020年4月21日 下午1:57:02
 * @Description 
 */

@SpringBootApplication
@MapperScan(basePackages = "com.zx.mapper")
public class ZxStarter {

	public static void main(String[] args) {
		SpringApplication.run(ZxStarter.class, args);
	}

}
