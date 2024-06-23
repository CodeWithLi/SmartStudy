package com.example.smartstudy.service.impl;

import com.example.smartstudy.exception.BaseException;
import com.example.smartstudy.utils.XfUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class XfApiService {

    @Resource
    private XfUtil xfUtil;

    public void SyncTextResult(String content){
        String result = xfUtil.SyncTextResult(content);
        SyncResult(result);
    }

    public void SyncImageResult(String imageName){
        String result = xfUtil.SyncImageResult(imageName);
        SyncResult(result);
    }

    public void SyncResult(String result){
        if (!result.equals("pass") && !result.equals("block")) {
            throw new BaseException("请求内容不存在");
        } else if (result.equals("block")) {
            throw new BaseException("请求内容违规");
        }
    }
}
