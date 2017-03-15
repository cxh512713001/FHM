package com.capacity.controller;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.capacity.entity.TransferInfo;
import com.capacity.response.BaseResponse;
import com.capacity.response.CapacityResponse;
import com.capacity.response.entity.TransferInfoResponseEntity;
import com.capacity.service.TransferInfoService;
import com.capacity.util.WorkerUtil;

@Controller
@RequestMapping(value = "/capacity")
public class TransferInfoController {
	
	Logger logger = Logger.getLogger(TransferInfoController.class);
	
	@Autowired
	TransferInfoService transferInfoService;
	
	@RequestMapping(value = "/capacity", method = RequestMethod.GET)
	@ResponseBody
	public Object findCapacity(){
		CapacityResponse response = new CapacityResponse();
		try{
			List<TransferInfo> list = transferInfoService.findAll();
			List<TransferInfoResponseEntity> entities = convertToTransferInfoResponseEntitiesFromList(list);
			response.setStatus(BaseResponse.OK);
			response.setMsg(entities);
		}catch(Exception e){
			logger.info(e.getMessage(),e);
			response.setStatus(BaseResponse.REFUSE);
		}
		return response;
	}
	
	@RequestMapping(value = "/select", method = RequestMethod.GET)
	@ResponseBody
	public Object findCapacityByStartAndEndDate(String startTime, String endTime,String transformerId, String name ){
		CapacityResponse response = new CapacityResponse();
		Map<String ,Object> map = new HashMap<String ,Object>();
		try{
			map.put("startTime", startTime);
			map.put("endTime", endTime);
			map.put("transformerId", transformerId);
			map.put("username", name);
			List<TransferInfo> list = transferInfoService.findByCondition(map);
			List<TransferInfoResponseEntity> entities = convertToTransferInfoResponseEntitiesFromList(list);
			response.setStatus(BaseResponse.OK);
			response.setMsg(entities);
		}catch(Exception e){
			logger.info(e.getMessage(),e);
			response.setStatus(BaseResponse.REFUSE);
			response.setMsg(e.getMessage());
		}
		return response;
	}
	
	@RequestMapping(value = "/deleteById", method = RequestMethod.GET)
	@ResponseBody
	public Object deleteById(Integer id){
		CapacityResponse response = new CapacityResponse();
		try {
			transferInfoService.deleteById(id);
			response.setStatus(BaseResponse.OK);
		} catch (Exception e) {
			response.setStatus(BaseResponse.REFUSE);
			response.setMsg(e.getMessage());
		}
		return response;
	}
	
	@RequestMapping(value = "/deleteByIds", method = RequestMethod.GET)
	@ResponseBody
	public Object deleteByIds(@RequestParam("ids")Integer[] ids){
		CapacityResponse response = new CapacityResponse();
		try {
			List<Integer> list = new ArrayList<Integer>();
			Collections.addAll(list, ids);
			if(!WorkerUtil.isNullOrEmpty(list)){
				transferInfoService.deleteByIds(list);
			}else {
				response.setMsg("ids不能为空");
			}
			response.setStatus(BaseResponse.OK);
		} catch (Exception e) {
			response.setStatus(BaseResponse.REFUSE);
			response.setMsg(e.getMessage());
		}
		return response;
	}
	
	private List<TransferInfoResponseEntity> convertToTransferInfoResponseEntitiesFromList(List<TransferInfo> list){
		List<TransferInfoResponseEntity> transferInfoResponseEntities = new LinkedList<TransferInfoResponseEntity>();
		for (TransferInfo transferInfo : list) {
			logger.info(transferInfo.toString());
			TransferInfoResponseEntity  entity = new TransferInfoResponseEntity();
			entity.setTestUser(transferInfo.getTestUser());
			entity.setTransformerId(String.valueOf(transferInfo.getTransformerId().intValue()));
			entity.setUserName(transferInfo.getUserName());
			entity.setUserAddress(transferInfo.getUserAddress());
			entity.setCreateTime(transferInfo.getCreateTime());
			transferInfoResponseEntities.add(entity);
		}
		return transferInfoResponseEntities;
	}

}
