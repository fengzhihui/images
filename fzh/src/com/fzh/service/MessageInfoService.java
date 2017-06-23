package com.fzh.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fzh.dao.MessageInfoDAO;
import com.fzh.entity.MessageInfo;

@Service
public class MessageInfoService {
	@Autowired
	private MessageInfoDAO messageInfoDAO;

	public void save(MessageInfo messageInfo) {
		messageInfoDAO.save(messageInfo);
	}
	
	public void update(MessageInfo messageInfo) {
		messageInfoDAO.update(messageInfo);
	}
	
	public List<Map<String, Object>> findByKeyword(String date,String keyword,String wxno){
		return messageInfoDAO.findByKeyword(date, keyword, wxno);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, Object> findUserMsg(String date,String wxno){
		List list = new ArrayList();
		String msgSum = "", usrSum = "", msgAvg = "", msgDate = "";
		Map<String, Object> dataMap = new HashMap<String, Object>();
		
		List<Map<String, Object>> resultList = messageInfoDAO.findUserMsg(date, wxno);
		
		for (int i = 0; resultList !=null && i < resultList.size(); i++) {
			Map<String, Object> result = resultList.get(i);
			if(result == null) continue;
			if(result.get("_sum") == null || result.get("_count") == null) continue;
			int t = Integer.parseInt(result.get("_sum").toString());
			int c = Integer.parseInt(result.get("_count").toString());
			if(c != 0){
				String dt = result.get("msg_send_time").toString();
				String avg = String.format("%.1f", (float)t/c);
				String[] strArr = new String[]{
					//活跃用户数      交互消息总量      日期      人均消息交互量
					result.get("_count").toString(), result.get("_sum").toString(), dt, avg
				};
				list.add(strArr);
				msgDate += "'" + dt + "', ";
				msgSum += t + ", ";
				usrSum += c + ", ";
				msgAvg += avg + ", ";
			}
		}
		dataMap.put("list", list);
		dataMap.put("msgSum", msgSum);
		dataMap.put("usrSum", usrSum);
		dataMap.put("msgAvg", msgAvg);
		dataMap.put("msgDate", msgDate);
		return dataMap;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List findMenuMsg(String date, String wxno, List<String> pids){
		List list = new ArrayList();
		String[] clickCountArr = new String[16]; 
		//MSG_SEND_TIME、MENUKEY、PARENT_MENU_ID、COUNT、ORDER_NO
		List<Map<String, Object>> resultList = messageInfoDAO.findMenuMsg(date, wxno);
		for (int i = 0; resultList !=null && i < resultList.size(); i++) {
			Map<String, Object> result = resultList.get(i);
			date = result.get("msg_send_time").toString();
			
			if(i == 0){
				setClickCountArr(clickCountArr, date);
				setClickCountArr(result, pids, clickCountArr);
				list.add(clickCountArr);
			}
			
			if(i == resultList.size() - 1) break;
			
			Map<String, Object> nextResult = resultList.get(i + 1);
			String nextDate = nextResult.get("msg_send_time").toString();
			if(date.equals(nextDate)){
				setClickCountArr(nextResult, pids, clickCountArr);
			} else {
				clickCountArr = new String[16];
				setClickCountArr(clickCountArr, nextDate);
				setClickCountArr(nextResult, pids, clickCountArr);
				list.add(clickCountArr);
			}
		}
		return list;
	}
	
	public void setClickCountArr(String[] clickCountArr, String date){
		for(int i = 0; i < clickCountArr.length; i++){
			clickCountArr[i] = (i == 0) ? date : "0";
		}
	}
	
	public void setClickCountArr(Map<String, Object> result, List<String> pids, String[] clickCountArr){
		Object orderNo = result.get("order_no");
		String count = result.get("_count").toString();
		String menu_key = result.get("menu_key").toString();
		//父级菜单
		if(orderNo == null){
			if(pids.get(0) !=null && menu_key.equals(pids.get(0))){
				clickCountArr[1] = count;
			}else if(pids.get(1) !=null && menu_key.equals(pids.get(1))){
				clickCountArr[6] = count;
			}else if(pids.get(2) !=null && menu_key.equals(pids.get(2))){
				clickCountArr[11] = count;
			}
		} else {
			//子级菜单
			clickCountArr[Integer.parseInt(orderNo.toString())] = count;
		}
	}
	
}
