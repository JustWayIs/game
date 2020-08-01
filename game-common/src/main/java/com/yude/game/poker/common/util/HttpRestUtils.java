package com.yude.game.poker.common.util;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Rest调用工具类
 * @author
 *
 */

public class HttpRestUtils {

	private static final int HTTP_TIME_OUT = 20*1000;
	
	private static final Logger log = LoggerFactory.getLogger(HttpRestUtils.class);
	
	public static String BASE_URL;
	
	
	public static <T> T get(String url,Class<T> clas,Object... parms) {
		try {
			url = formatUrlParms(url,true,parms);
			String context = get(url);
			T result = JSONObject.parseObject(context, clas);
			if (result == null) {
	            log.error(url + "\t响应内容为空\t" + JSONObject.toJSONString(parms));
	        }
			return result;
		} catch (Exception e) {
			log.error("get rest url: {} error:",url,e);
		}
		return null;
	}
	
	public static <T> List<T> getList(String url,Class<T> clas) {
		try {
			String context = get(url);
			List<T> result = JSONArray.parseArray(context, clas);
			return result;
		} catch (Exception e) {
			log.error("getList rest url: {} error:",url,e);
		}
		return null;
		}
	
	public static <T> List<T> getList(String url,Class<T> clas,Object... parms) {
		try {
			url = formatUrlParms(url,true,parms);
			String context = get(url);
			List<T> result = JSONArray.parseArray(context, clas);
			if (result == null) {
	            log.error(url + "\t响应内容为空\t" + JSONObject.toJSONString(parms));
	        }
			return result;
		} catch (Exception e) {
			log.error("get rest url: {} error:",url,e);
		}
		return null;
	}
	
	
	public static <T> T post(String url,Class<T> clas,Object... parms) {
		try {
			String body = formatUrlParms("",false,parms);
			String context = post(url,body);
			T result = JSONObject.parseObject(context, clas);
			if (result == null) {
	            log.error(url + "\t响应内容为空\t" + JSONObject.toJSONString(parms));
	        }
			return result;
		} catch (Exception e) {
			log.error("post rest url: {} error:",url,e);
		}
		return null;
	}
	
	public static <T> T post(String url,String body,Class<T> clas) {
		try {
			String context = post(url,body);
			T result = JSONObject.parseObject(context, clas);
			return result;
		} catch (Exception e) {
			log.error("post rest url: {} , body:{},error:",url,body,e);
		}
		return null;
	}

	
	private static String get(String url) {
		String context = null;
		try {
            log.debug("get url:{}",url);
			context = HttpUtil.get(url, HTTP_TIME_OUT);
			log.debug("get url:{} result:{}",url,context);
		} catch (Exception e) {
			log.error("get url["+url+"] Error:",e);
		}
		return context;
	}
	
	private static String post(String url,String body) {
		String context = null;
		try {
			log.debug("post url:{},data:{}",url,body);
			context = HttpUtil.post(url, body,HTTP_TIME_OUT);
			log.debug("post url:{}, data:{}  result:{}",url,body,context);
		} catch (Exception e) {
			log.error("post url["+url+"] Error:",e);
		}
		return context;
	}
	
	private static String patch(String url) {
		String body = HttpUtil.get(url, HTTP_TIME_OUT);
		return body;
	}
	
	private static String delete(String url) {
		String body = HttpUtil.get(url, HTTP_TIME_OUT);
		return body;
	}
	
	private static String formatUrlParms(String base,boolean get,Object... parms) {
		String result = base + (get?"?":"");
		List<String> tmpList = Lists.newArrayList();
		for(int i =0 ; i < parms.length ; ) {
			tmpList.add(parms[i]+"="+parms[i+1]);
			i+=2;
		}
		result += tmpList.stream().collect(Collectors.joining("&")).toString();
		return result;
	}
	
//	//异步调用
//	class HttpResponseThread implements Runnable {
//		
//		@Override
//		public void run() {
//			while(true) {
//				HttpResponse response = responseList.poll();
////				if(response.bodyStream() == null || response.bodyStream().available())
////				HttpUtil.createPost("").executeAsync().bodyStream()
//			}
//			
//		}
//		
//	}
	
	
}
