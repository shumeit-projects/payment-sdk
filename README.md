# 深圳数美信息-聚合支付SDK [![Build Status](https://github.com/shumeit-projects/payment-sdk/actions/workflows/.github/workflows/maven.yml/badge.svg)](https://github.com/shumeit-projects/payment-sdk/actions) ![Maven Central](https://img.shields.io/maven-central/v/com.shumeit.sdk/payment-sdk) [![GitHub license](https://img.shields.io/github/license/shumeit-projects/payment-sdk?style=social)](https://github.com/shumeit-projects/payment-sdk/blob/master/LICENSE)

# maven依赖

```xml
<dependency>
    <groupId>com.shumeit.sdk</groupId>
    <artifactId>payment-sdk</artifactId>
    <version>1.1.0-RELEASE</version>
</dependency>
```

# 使用教程

```java
//1.初始化加载商户私钥和数美平台公钥
RequestClient reqClient = new RequestClient(PLAT_PUB_KEY, MCH_PRIVATE_KEY);

//2.按接口文档,封装请求的data数据
DemoReqData reqData = new DemoReqData();
reqData.setDemoKey1("key1 content");

//3.封装请求体
Request<DemoReqData> request = new Request<>();
request.setMchNo(MCH_NO);
request.setMethod("trade.doPay");
request.setVersion("1.0");
request.setRandStr(StringUtil.gen32LenRand());
request.setSignType("1");
request.setSecKey("");
request.setData(reqData);

//4.发送请求,获得响应,doRequest方法内部已经处理了加签，验签,secKey的加解密等操作.详见 RequestUtil#doRequest
Response<String> response = reqClient.doRequest(GATEWAY_API, request);

JSONObject data = JSON.parseObject(response.getData(), JSONObject.class);
DemoRespData demoRespData = null;

if (Objects.equals(response.getRespCode(), RespCode.SUCCESS.getCode())) {
    demoRespData = data.toJavaObject(DemoRespData.class);
} else if (Objects.equals(response.getRespCode(), RespCode.FAIL.getCode())) {
    System.out.println("bizErrCode:" + data.getString("bizErrCode"));
    System.out.println("bizErrMsg:" + data.getString("bizErrMsg"));
    //todo 失败处理
} else {
    System.out.println("未知的响应状态");
    //todo 未知处理
}

if (demoRespData != null) {
    //todo 业务处理
}

```