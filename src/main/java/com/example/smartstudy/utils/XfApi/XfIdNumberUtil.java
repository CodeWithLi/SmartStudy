package com.example.smartstudy.utils.XfApi;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * 讯飞身份证识别工具类
 */
public class XfIdNumberUtil {
	// 引擎类型
	private static final String ENGINE_TYPE = "idcard";
	// 是否返回头像图片
	private static final String HEAD_PORTRAIT = "1";

	// 是否返回身份证号码区域截图
	private static final String ID_NUMBER_IMAGE = "1";

	/**
	 * 组装http请求头
	 */
	public static Map<String, String> buildHttpHeader(String APPID) throws UnsupportedEncodingException {
		String API_KEY="ef84def06dc1d323c64e186ce021fa72";
		String curTime = System.currentTimeMillis() / 1000L + "";
		String param = "{\"engine_type\":\"" + ENGINE_TYPE + "\",\"head_portrait\":\"" + HEAD_PORTRAIT + "\",\"id_number_image\":\"" + ID_NUMBER_IMAGE +"\"}";
		String paramBase64 = new String(Base64.encodeBase64(param.getBytes("UTF-8")));
		String checkSum = DigestUtils.md5Hex(API_KEY + curTime + paramBase64);
		Map<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		header.put("X-Param", paramBase64);
		header.put("X-CurTime", curTime);
		header.put("X-CheckSum", checkSum);
		header.put("X-Appid", APPID);
		return header;
	}

	/**
	 * 发送post请求
	 * 
	 * @param url
	 * @param header
	 * @param body
	 * @return
	 */
	public static String doPost1(String url, Map<String, String> header, String body) {
		String result = "";
		BufferedReader in = null;
		PrintWriter out = null;
		try {
			// 设置 url
			URL realUrl = new URL(url);
			URLConnection connection = realUrl.openConnection();
			HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
			// 设置 header
			for (String key : header.keySet()) {
				httpURLConnection.setRequestProperty(key, header.get(key));
			}
			// 设置请求 body
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			out = new PrintWriter(httpURLConnection.getOutputStream());
			// 保存body
			out.print(body);
			// 发送body
			out.flush();
			if (HttpURLConnection.HTTP_OK != httpURLConnection.getResponseCode()) {
				System.out.println("Http 请求失败，状态码：" + httpURLConnection.getResponseCode());
				return null;
			}

			// 获取响应body
			in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			return null;
		}
		return result;
	}
}
