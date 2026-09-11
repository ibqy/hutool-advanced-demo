package com.ibqy.hutool.http;

import cn.hutool.http.*;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import java.util.*;

/**
 * HttpUtil 高级用法
 * - 连接池 + 拦截器
 * - 文件上传/下载
 * - 自定义 SSL
 * - RESTful 调用链
 */
public class HttpUtilDemo {
    public static void demo() {
        System.out.println("═══ HttpUtil ═══");

        // 1. 链式 GET（支持超时、header、拦截器）
        String body = HttpRequest.get("https://api.github.com/zen")
            .timeout(5000)
            .header("User-Agent", "Hutool-Demo")
            .execute()
            .body();
        System.out.println("  GET → " + body);

        // 2. POST JSON
        String postBody = HttpRequest.post("https://httpbin.org/post")
            .body("{\"msg\":\"hello hutool\"}")
            .contentType(ContentType.JSON.getValue())
            .execute()
            .body();
        System.out.println("  POST → " + (postBody.length() > 60 ? postBody.substring(0, 60) + "…" : postBody));

        // 3. 带参数的 GET
        HashMap<String, Object> params = new HashMap<>();
        params.put("q", "hutool");
        params.put("page", 1);
        String searchRes = HttpUtil.get("https://api.github.com/search/repositories", params);
        System.out.println("  GET params → " + searchRes.substring(0, 50) + "…");

        // 4. 下载到临时文件
        long size = HttpUtil.downloadFile("https://www.baidu.com/img/flexible/logo/pc/result.png",
            FileUtil.file("/tmp/hutool_download.png"));
        System.out.println("  下载文件 → " + size + " bytes");

        System.out.println();
    }
}