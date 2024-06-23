package com.example.smartstudy.utils.XfApi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.smartstudy.utils.ImageUtil;
import com.example.smartstudy.exception.BaseException;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import okhttp3.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

public class XfImageUnderstandUtil extends WebSocketListener{

    public static final String appid = "5a462c52";

    public static List<RoleContent> historyList = new ArrayList<>(); // 对话历史存储集合

    public static String totalAnswer = ""; // 大模型的答案汇总

    // 环境治理的重要性  环保  人口老龄化  我爱我的祖国
    public static String NewQuestion = "";

    public static Boolean ImageAddFlag = false; // 判断是否添加了图片信息

    public static final Gson gson = new Gson();

    public static Map<Object,Object> resultMap=new HashMap<>();

    public XfImageUnderstandUtil() {
    }

    public static boolean canAddHistory() {  // 由于历史记录最大上线1.2W左右，需要判断是能能加入历史
        int history_length = 0;
        for (RoleContent temp : historyList) {
            history_length = history_length + temp.content.length();
        }
        if (history_length > 1200000000) { // 这里限制了总上下文携带，图片理解注意放大 ！！！
            historyList.remove(0);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
        try {
            JSONObject requestJson = new JSONObject();

            JSONObject header = new JSONObject();  // header参数
            header.put("app_id", appid);
            header.put("uid", UUID.randomUUID().toString().substring(0, 10));

            JSONObject parameter = new JSONObject(); // parameter参数
            JSONObject chat = new JSONObject();
            chat.put("domain", "general");
            chat.put("temperature", 0.5);
            chat.put("max_tokens", 4096);
            chat.put("auditing", "default");
            parameter.put("chat", chat);

            JSONObject payload = new JSONObject(); // payload参数
            JSONObject message = new JSONObject();
            JSONArray text = new JSONArray();

            // 历史问题获取
            if (historyList.size() > 0) { // 保证首个添加的是图片
                for (RoleContent tempRoleContent : historyList) {
                    if (tempRoleContent.content_type.equals("image")) { // 保证首个添加的是图片
                        text.add(JSON.toJSON(tempRoleContent));
                        ImageAddFlag = true;
                    }
                }
            }
            if (historyList.size() > 0) {
                for (RoleContent tempRoleContent : historyList) {
                    if (!tempRoleContent.content_type.equals("image")) { // 添加费图片类型
                        text.add(JSON.toJSON(tempRoleContent));
                    }
                }
            }

            // 最新问题
            RoleContent roleContent = new RoleContent();
            // 添加图片信息
            if (!ImageAddFlag) {
                roleContent.role = "user";
                roleContent.content = Base64.getEncoder().encodeToString(ImageUtil.read("src\\main\\resources\\1.jpg"));
                roleContent.content_type = "image";
                text.add(JSON.toJSON(roleContent));
            }
            // 添加对图片提出要求的信息
            RoleContent roleContent1 = new RoleContent();
            roleContent1.role = "user";
            roleContent1.content = NewQuestion;
            roleContent1.content_type = "text";
            text.add(JSON.toJSON(roleContent1));

            message.put("text", text);
            payload.put("message", message);

            requestJson.put("header", header);
            requestJson.put("parameter", parameter);
            requestJson.put("payload", payload);
            webSocket.send(requestJson.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        JsonParse myJsonParse = gson.fromJson(text, JsonParse.class);
        if (myJsonParse.header.code != 0) {
            webSocket.close(1000, "");
            throw new BaseException(myJsonParse.header.code+"");
        }
        List<Text> textList  = myJsonParse.payload.choices.text;
        for (Text temp : textList) {
            totalAnswer =totalAnswer+temp.content;
        }
        if (myJsonParse.header.status==2){
//            System.out.println(111);
        }
//        System.out.println(totalAnswer);
        if (myJsonParse.header.status == 2){
            // 可以关闭连接，释放资源
            RoleContent roleContent = new RoleContent();
            roleContent.setRole("assistant");
            roleContent.setContent(totalAnswer);
            roleContent.setContent_type("text");
            historyList.add(roleContent);
            resultMap.put("totalAnswer",totalAnswer);
            resultMap.replace("wsCloseFlag","false","true");
        }

    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        super.onFailure(webSocket, t, response);
        if (null!=response){
            int code= response.code();
            if (101!=code){
                System.exit(0);
                throw new BaseException("连接失败");
            }
            throw new BaseException("错误码:"+code+",错误体:"+response.body());
        }
    }

    // 鉴权方法
    public static String getAuthUrl(String hostUrl, String apiKey, String apiSecret) throws Exception {
        URL url = new URL(hostUrl);
        // 时间
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date = format.format(new Date());
        // 拼接
        String preStr = "host: " + url.getHost() + "\n" +
                "date: " + date + "\n" +
                "GET " + url.getPath() + " HTTP/1.1";
        // SHA256加密
        Mac mac = Mac.getInstance("hmacsha256");
        SecretKeySpec spec = new SecretKeySpec(apiSecret.getBytes(StandardCharsets.UTF_8), "hmacsha256");
        mac.init(spec);

        byte[] hexDigits = mac.doFinal(preStr.getBytes(StandardCharsets.UTF_8));
        // Base64加密
        String sha = Base64.getEncoder().encodeToString(hexDigits);
        // 拼接
        String authorization = String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"", apiKey, "hmac-sha256", "host date request-line", sha);
        // 拼接地址
        HttpUrl httpUrl = Objects.requireNonNull(HttpUrl.parse("https://" + url.getHost() + url.getPath())).newBuilder().//
                addQueryParameter("authorization", Base64.getEncoder().encodeToString(authorization.getBytes(StandardCharsets.UTF_8))).//
                addQueryParameter("date", date).//
                addQueryParameter("host", url.getHost()).//
                build();
        return httpUrl.toString();
    }

    //返回的json结果拆解
    class JsonParse {
        Header header;
        Payload payload;
    }

    class Header {
        int code;
        int status;
        String sid;
    }

    class Payload {
        Choices choices;
    }

    class Choices {
        List<Text> text;
    }

    class Text {
        String role;
        String content;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public class RoleContent {
        String role;
        String content;
        String content_type;
    }

}

