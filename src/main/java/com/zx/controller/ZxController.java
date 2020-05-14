package com.zx.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.zx.entity.ZxEntity;
import com.zx.form.CommonParam;
import com.zx.mapper.ZxMapper;
import com.zx.util.DateUtils;
import com.zx.vo.Dto;
import com.zx.vo.ShowVo;

/**
 * @author zhou.hao
 * @email zhouhao@ule.com
 * @createTime 2020年4月21日 下午1:58:12
 * @Description 
 */

@Controller
public class ZxController {

	@Autowired
	private ZxMapper mapper;

	@GetMapping("test")
	public ModelAndView test() {
		return new ModelAndView("test");
	}

	@GetMapping("baby/home")
	public ModelAndView home() {
		// 1:醒着 ； 0:睡觉
		// 0->1 睡觉； 1->0 活跃
		ZxEntity last = mapper.getLast();
		int currentStatus = 0;
		if (last != null) {
			currentStatus = last.getFlag();
		}
		ModelAndView mv = new ModelAndView("bady_home");
		mv.addObject("currentStatus", currentStatus);
		return mv;
	}

	@PostMapping("change")
	@ResponseBody
	public Map<String, Object> changeStatus(@RequestParam int flag) {
		Map<String, Object> res = new HashMap<>();
		ZxEntity entity = new ZxEntity();
		entity.setFlag(flag);
		entity.setTime(new Date());
		mapper.saveNew(entity);
		res.put("code", 0);
		return res;
	}

	@GetMapping("baby/report")
	public ModelAndView reportPage() {
		return new ModelAndView("baby_report");
	}


	@PostMapping("reportData")
	@ResponseBody
	public Map<String, Object> getReport(@RequestBody CommonParam param) {
		Date day = null;
		if (param == null || param.getDateStr() == null || "".equals(param.getDateStr())) {
			day = new Date();
		}else {
			day = DateUtils.strToDate(param.getDateStr(), "yyyy-MM-dd");
		}
		List<ShowVo> datas = getDataByDay(day);
		long total = 0;
		for (ShowVo showVo : datas) {
			if ("睡眠".equals(showVo.getStatus())) {
				total = total + (showVo.getEndTime() - showVo.getStartTime());
			}
		}
		String hour = String.valueOf((int) (total / 1000 / 60 / 60));
		String min = String.valueOf((int) (total / 1000 / 60 % 60));
		String totalSleep = "今日共睡:" + hour + "小时" + min + "分钟";
		Map<String, Object> result = new HashMap<>();
		result.put("code", "0");
		result.put("data1", datas);
		result.put("data2", totalSleep);
		return result;
	}

	@GetMapping("hisDiagram")
	@ResponseBody
	public Map<String, Object> getHisDiagram(/* @RequestBody */ CommonParam param) {
		DecimalFormat df = new DecimalFormat("0.00 ");
		int term = -7;
		try {
			term = Integer.parseInt(param.getTerm());
		} catch (Exception e) {
		}
		List<String> dateList = new ArrayList<>();
		List<Double> timeList = new ArrayList<>();
		while (term <= 0) {
			Date day = DateUtils.addDays(new Date(), term);
			String dayStr = DateUtils.dateToStr(day, "yyyy-MM-dd");
			List<ShowVo> datas = getDataByDay(day);
			long total = 0;
			for (ShowVo showVo : datas) {
				if ("睡眠".equals(showVo.getStatus())) {
					total = total + (showVo.getEndTime() - showVo.getStartTime());
				}
			}
			double d = (double) total / 1000 / 60 / 60;
			Double hour = Double.parseDouble(df.format(d));
			dateList.add(dayStr);
			timeList.add(hour);
			term++;
		}
		Map<String, Object> result = new HashMap<>();
		result.put("code", 0);
		result.put("dateList", dateList);
		result.put("timeList", timeList);
		return result;
	}



	private List<ShowVo> getDataByDay(Date date) {
		Date startOfDay = DateUtils.getDayStartTime(date);
		ZxEntity maxEntityOfLastDay = mapper.getMaxEntityOfLastDay(startOfDay);
		if (maxEntityOfLastDay == null) {
			return Collections.emptyList();
		}
		Date endOfDay = DateUtils.getDayEndTime(date);
		ZxEntity minEntityOfNextDay = mapper.getMinEntityOfNextDay(endOfDay);
		if (minEntityOfNextDay == null) {
			minEntityOfNextDay = mapper.getMaxEntityOfToday(endOfDay);
		}
		if (minEntityOfNextDay == null || minEntityOfNextDay.getId() <= maxEntityOfLastDay.getId()) {
			return Collections.emptyList();
		}
		int startId = maxEntityOfLastDay.getId();
		int endId = minEntityOfNextDay.getId();
		List<ZxEntity> list = mapper.queryBetweenIds(startId, endId);
		return calShowVos(list, date, endOfDay);
	}

	private List<ShowVo> calShowVos(List<ZxEntity> list, Date date, Date endOfDay) {
		List<ShowVo> vos = new ArrayList<>();
		Dto dto = new Dto(0);
		while (dto.getIndex() < list.size()) {
			dto = get(list, dto);
			ZxEntity entity1 = dto.getEntity();
			if (dto.getIndex() == 0) {
				entity1.setTime(DateUtils.getDayStartTime(date));
			}
			int flag = entity1.getFlag();
			dto = getNext(list, dto, flag);
			ZxEntity entity2 = dto.getEntity();
			if (entity2 == null) {
				if (dto.getIndex() >= list.size() - 1) {
					break;
				}
				entity2 = new ZxEntity();
				entity2.setTime(endOfDay);
			}
			ShowVo show = new ShowVo();
			if (flag == 1) {
				show.setStatus("活跃");
			} else {
				show.setStatus("睡眠");
			}
			show.setStart(DateUtils.dateToStr(entity1.getTime()));
			show.setStartTime(entity1.getTime().getTime());
			Date endTime = endOfDay.after(entity2.getTime()) ? entity2.getTime() : endOfDay;
			show.setEnd(DateUtils.dateToStr(endTime));
			show.setEndTime(endTime.getTime());
			vos.add(show);
		}
		return vos;
	}

	private Dto getNext(List<ZxEntity> list, Dto dto, int flag) {
		dto.setIndex(dto.getIndex() + 1);
		if (dto.getIndex() >= list.size()) {
			dto.setEntity(null);
			return dto;
		}
		ZxEntity entity = list.get(dto.getIndex());
		if (flag == entity.getFlag()) {
			return getNext(list, dto, flag);
		}
		dto.setEntity(entity);
		return dto;
	}

	private Dto get(List<ZxEntity> list, Dto dto) {
		dto.setEntity(list.get(dto.getIndex()));
		return dto;
	}

	@PostMapping("distinctDate")
	@ResponseBody
	public Map<String, Object> distinctDate(){
		Map<String, Object> datas = new HashMap<String, Object>();
		datas.put("code", "0");
		datas.put("data", mapper.distinctDate());
		return datas;
	}
}
