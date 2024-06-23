package com.example.smartstudy.utils;

import com.example.smartstudy.exception.BaseException;
import com.example.smartstudy.model.dto.UserDto;
import com.example.smartstudy.properties.XfApiProperties;
import com.example.smartstudy.utils.XfApi.XfIdNumberUtil;
import com.example.smartstudy.utils.XfApi.XfImageUnderstandUtil;
import com.example.smartstudy.utils.XfApi.XfSyncImageUtil;
import com.example.smartstudy.utils.XfApi.XfSyncTextUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 自定义讯飞工具类
 */
@Component
public class XfUtil {

    @Resource
    private XfApiProperties xfApiProperties;

    @Resource
    private ObjectMapper objectMapper;

    //身份证识别工具类
    public UserDto IdNumberResult(MultipartFile file) {
        try {
            // OCR webapi 接口地址
            String IdNumberUrl = "http://webapi.xfyun.cn/v1/service/v1/ocr/idcard";
            Map<String, String> header = XfIdNumberUtil.buildHttpHeader(xfApiProperties.getAppId());
            byte[] imageByteArray = file.getBytes();
            String imageBase64 = new String(Base64.encodeBase64(imageByteArray), "UTF-8");
            String result = XfIdNumberUtil.doPost1(IdNumberUrl, header, "image=" + URLEncoder.encode(imageBase64, "UTF-8"));
            Map<String, Object> data = (Map<String, Object>) objectMapper.readValue(result, new TypeReference<Map<String, Object>>() {
            }).get("data");
            String address = (String) data.get("address");
            String birthdayString = (String) data.get("birthday");
            String idNumber = (String) data.get("id_number");
            String name = (String) data.get("name");
            data.get("sex");
            String sex = (String) data.get("sex");
            Map<String, Object> headPortrait = (Map<String, Object>) data.get("head_portrait");
            String headPortraitImage = (String) headPortrait.get("image");
            byte[] headImageByte = Base64.decodeBase64(headPortraitImage);

            // 定义日期格式
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
            Date birthday = null;
            try {
                // 将字符串解析为Date对象
                birthday = sdf.parse(birthdayString);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //存储用户个人信息
            UserDto userDto = UserDto.builder()
                    .address(address)
                    .birthday(birthday)
                    .headImage(headImageByte)
                    .idNumber(idNumber)
                    .name(name)
                    .sex(sex)
                    .build();

            return userDto;
        } catch (Exception e) {
            throw new BaseException(e.getMessage());
        }

    }

    //文本合规工具类
    public String SyncTextResult(String content) {
        try {
            //文本合规请求地址
            String TextUrl = "https://audit.iflyaisol.com//audit/v2/syncText";
            //文本合规key
            String TextApiKey = "70e63cd93ff7cb9bf3910cd6f3b0ebba";
            /**
             * 业务参数
             * --- 如果需要使用黑白名单资源，放开lib_ids与categories参数
             * */
            String json = "{\n" +
                    "  \"is_match_all\": 1,\n" +
                    "  \"content\": \"" + content + "\"\n" + "," +
                    "  \"categories\": [\n" +
                    "    \"pornDetection\",\n" +
                    "    \"violentTerrorism\",\n" +
                    "    \"political\",\n" +
                    "    \"lowQualityIrrigation\",\n" +
                    "    \"contraband\",\n" +
                    "    \"advertisement\",\n" +
                    "    \"uncivilizedLanguage\"\n" +
                    "  ]\n" +
                    "}";
            // 获取鉴权
            Map<String, String> urlParams =
                    XfSyncTextUtil.getAuth(xfApiProperties.getAppId(), TextApiKey, xfApiProperties.getApiSecret());
            // 发起请求
            String returnResult = XfSyncTextUtil.doPostJson(TextUrl, urlParams, json);
            Map<String,Object> data=
                    (Map<String, Object>) objectMapper.readValue(returnResult, new TypeReference<Map<String, Object>>() {}).get("data");
            Map<String,Object> result = (Map<String, Object>) data.get("result");
            String suggest = (String) result.get("suggest");
            return suggest;
        } catch (Exception e) {
            throw new BaseException("文本合规请求错误:" + e.getMessage());
        }
    }

    //图片合规工具类
    public String SyncImageResult(String imageUrlOrPath) {
        try {
            String imageApiKey="70e63cd93ff7cb9bf3910cd6f3b0ebba";
            String url = "https://audit.iflyaisol.com//audit/v2/image";
            // 1、业务参数
            String json = "{\n" +
                    "  \"content\": \"" + imageUrlOrPath  + "\"\n" +
                    "}";
            // 2、获取鉴权
            Map<String, String> urlParams = XfSyncImageUtil.getAuth(xfApiProperties.getAppId(), imageApiKey, xfApiProperties.getApiSecret());
            ///3、发起请求
            String returnResult = XfSyncImageUtil.doPostJson(url, urlParams, json);
            Map<String,Object> data=
                    (Map<String, Object>) objectMapper.readValue(returnResult, new TypeReference<Map<String, Object>>() {}).get("data");
            Map<String,Object> result = (Map<String, Object>) data.get("result");
            String suggest = (String) result.get("suggest");
            return suggest;
        }catch (Exception e){
            throw new BaseException("图片合规合规请求错误:" + e.getMessage());
        }
    }


    //图片理解工具类
    public Map<Object,Object> historyList(String ask) throws Exception {
        final String hostUrl = "https://spark-api.cn-huabei-1.xf-yun.com/v2.1/image";
        final String apiSecret = "NTY1MDJkZDUyODBhNWIyOTQ3NmVkYmQw";
        final String apiKey = "70e63cd93ff7cb9bf3910cd6f3b0ebba";

        XfImageUnderstandUtil xfImageUnderstandUtil=new XfImageUnderstandUtil();

        xfImageUnderstandUtil.NewQuestion = ask;
        // 构建鉴权url
        String authUrl = xfImageUnderstandUtil.getAuthUrl(hostUrl, apiKey,apiSecret);
        OkHttpClient client = new OkHttpClient.Builder().build();
        String url = authUrl.toString().replace("http://", "ws://").replace("https://", "wss://");
        Request request = new Request.Builder().url(url).build();
        for (int i = 0; i < 1; i++) {
            xfImageUnderstandUtil.totalAnswer = "";
            client.newWebSocket(request, new XfImageUnderstandUtil());
        }
        return XfImageUnderstandUtil.resultMap;
    }

//    public static void main(String[] args) throws Exception {
//        XfUtil xfUtil=new XfUtil();
//        String question="帮我解释下这图片";
//        Map<Object, Object> map = xfUtil.historyList(question);
//        map.put("wsCloseFlag","false");
//        while (true){
//            Thread.sleep(200);
//            if (map.get("wsCloseFlag").equals("true")){
//                break;
//            }
//        }
//        System.out.println(map.get("totalAnswer"));
//    }
}
