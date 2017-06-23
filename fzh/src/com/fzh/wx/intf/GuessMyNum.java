package com.fzh.wx.intf;

import java.util.Random;

import com.fzh.entity.UserInfo;

public class GuessMyNum {

	/**
	 * 随机生成一个不重复数字的四位数或两位数
	 * @param type
	 * @return
	 */
	public static String setAnswer() {
		Random rand = new Random();
		Integer result = 0;
        //高级猜数
		int[] array = {0,1,2,3,4,5,6,7,8,9};
		for (int i = 10; i > 1; i--) {
		  int index = rand.nextInt(i);
		  int tmp = array[index];
		  array[index] = array[i - 1];
		  array[i - 1] = tmp;
		}
		
		for(int i = 0; i < 4; i++){
			//result = result * 10 + array[i];//此处有个问题是: array数组的第一位为0时,最终结果只有三位数
			if(i==0 && array[0]==0)
				result = result * 10 + array[5];
			else 
				result = result * 10 + array[i];
		}
		return result.toString();
	}

	/**
	 * 获取答案、回答次数及猜测提示
	 * @param input
	 * @param userInfo
	 * @return
	 */
	public static String getAnswer(String input, UserInfo userInfo) {
		//猜测提示
		String content = userInfo.getContent();
		userInfo.setContent("wrong");
		String answer = checkIsEndOrValidateInput(userInfo.getIsend() == 1 ? "1" : input);
		if(answer != null){
			return answer;
		}
		answer = userInfo.getIds();          //答案
		//回答次数
		int guessCount = userInfo.getCount()==0 ? 1 : (userInfo.getCount() + 1);
		userInfo.setCount(guessCount);
		
		StringBuilder answersb = new StringBuilder();
		StringBuilder contentsb = new StringBuilder();
		
		//根据用户输入设置猜测提示
		String tips = setAB(answer, input);
		tips = String.format("第%s回合   %s   %s", guessCount, input, tips);
		
		//第一回合
		if(content == null){
			contentsb.append(tips);
		} else {
			contentsb.append(content).append("\n").append(tips);
		}
		userInfo.setContent(contentsb.toString());
		
		//最后返回提示内容
		if(equals(answer, input)){
			userInfo.setIsend(1);
			answersb.append(contentsb).append("\n[鼓掌]恭喜你，答对了！\n重新开始请回复14进入！");
		} else {
			int endCount = 10;
			if(guessCount == endCount) {
				userInfo.setIsend(1);
				answersb.append(contentsb).append("\n[难过]本局结束，正解答案是").append(answer);
			} else {
				answersb.append(contentsb);
			}
		}
		answersb.append("\n\n查看帮助，请回复【帮助】");
		return answersb.toString();
	}
	
	public static String checkIsEndOrValidateInput(String input) {
		if("1".equals(input)){
			return "本局已结束，重新开始请回复14！\n查看帮助，请回复【帮助】";
		}
		if(!isNumeric(input)){
			return "你输入的不是数字！\n查看帮助，请回复【帮助】";
		} else if (input.length() != 4){
			return "你输入的不是四位数！\n查看帮助，请回复【帮助】";
		}
		if(isExist(input)){
			return "不能包含重复数字哦！\n查看帮助，请回复【帮助】";
		}
		return null;
	}
	
	/**
	 * 判断输入的值和位置的正确性
	 */
	private static String setAB(String answer, String input) {
		int a = 0;
		int b = 0;
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<answer.length(); i++){
			if(answer.contains(input.charAt(i)+"")) {
				b ++;
			}
			if(answer.charAt(i) == input.charAt(i)){
				a ++;
				b --;
			}
		}
		return sb.append(a).append("A").append(b).append("B").toString();
	}
	
	/**
	 * 判断输入是否为数字
	 * @param inputStr
	 * @return
	 */
	public static boolean isNumeric(String inputStr) {
		final String number = "0123456789";
		for (int i = 0; i < inputStr.length(); i++) {
			if (number.indexOf(inputStr.charAt(i)) == -1) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 判断用户输入的数字是否有重复
	 * @param inputStr
	 * @return
	 */
	private static boolean isExist(String inputStr) {
		char [] tmpchar = inputStr.toCharArray();
		for (int i = 0; i < tmpchar.length; i++) {
			for (int j = i+1; j < tmpchar.length; j++) {
				if(tmpchar[i] == tmpchar[j]){
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 比较用户输入数字与随机结果
	 * @return
	 */
	private static boolean equals(String answer, String input) {
		return answer.equals(input) ? true : false;
	}
	
	/**
	 * 猜数游戏帮助
	 * @return
	 */
	public static String gameDesc() {
		return "《高级猜数游戏玩法》 \n系统设定一个没有重复数字的4位数，由玩家来猜，每局10次机会。每猜一次，系统会给出猜测结果xAyB，x表示数字与位置都正确的数的个数，y表示数字正确但位置不正确的数的个数，x+y等于你一共猜对的数字个数。玩家根据猜测结果xAyB一直猜，直到猜中(4A0B)为止。如果10次都没猜中，系统会公布答案，游戏结束。\n\n玩家输入14即重新开始游戏。";
	}
	
}
