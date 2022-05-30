package com.example.demo.common;

import com.dtflys.forest.annotation.DataFile;
import com.dtflys.forest.annotation.Get;
import com.dtflys.forest.callback.OnProgress;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public interface RequestClient {
    /**
     * 聪明的你一定看出来了@Get注解代表该方法专做GET请求
     * 在url中的{0}代表引用第一个参数，{1}引用第二个参数
     */
    @Get("http://ditu.amap.com/service/regeo?longitude={0}&latitude={1}")
    Map getLocation(String longitude, String latitude);

    @Get(url = "https://way.jd.com/JDAI/ocr_idcard?appkey={0}&body={1}", dataType = "json")
    Map getIdCard(String appKey, @DataFile("file") String filePath, OnProgress onProgress);
}
