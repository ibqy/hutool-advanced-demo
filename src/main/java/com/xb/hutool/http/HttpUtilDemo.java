package com.xb.hutool.http;

import cn.hutool.http.*;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import java.util.*;

/**
 * HttpUtilDemo - 演示 Hutool HTTP 网络请求的高级用法
 *
 * HTTP 调用是微服务和对接第三方 API 的基础。本类演示链式 GET/POST 请求、
 * 参数拼接、文件下载等常见场景，帮助学习者掌握 Hutool 对 HttpURLConnection 的封装技巧。
 *
 * @author ibqy
 */
public class HttpUtilDemo {

    /**
     * HTTP 演示入口：展示链式 GET、POST JSON、带参请求和文件下载
     */
    public static void demo() {
        System.out.println("═══ HttpUtil ═══");

        // 1. 链式 GET（必须设置 timeout，否则可能无限阻塞）
        String body = HttpRequest.get("https://api.github.com/zen")
            .timeout(5000)
            .header("User-Agent", "Hutool-Demo")
            .execute()
            .body();
        System.out.println("  GET → " + body);

        // 2. POST JSON —— 必须设置 Content-Type，否则服务端可能无法解析
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