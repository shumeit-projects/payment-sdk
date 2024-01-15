import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shumeit.sdk.api.entity.Request;
import com.shumeit.sdk.api.entity.Response;
import com.shumeit.sdk.api.enums.RespCode;
import com.shumeit.sdk.api.utils.JsonUtil;
import com.shumeit.sdk.api.utils.RequestClient;
import com.shumeit.sdk.api.utils.SecurityUtil;
import com.shumeit.sdk.api.utils.StringUtil;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * Author: Cmf
 * Date: 2022.2.22
 * Time: 16:29
 * Description:
 */
public class Test {
    public static String MCH_PRIVATE_KEY = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDBTPH6DzZ/zNnMtTihbqVX/cCU0MoUxhthHJF59dGgB6haAR3oHp7mPVpdMWxyyPBOj6rsfNYgofsbCGT90uPVKqnAwUxJdzDWTckty4YeiFahx5om6EV/2uMfDYaeRutjPReJ6XOQFuTK8tPvGZu/I1lTdTCat8Pkh2aM+9zHw2J3pF98xD3+jPwzLXDoP4WmC7W70Z9v7y0AOlKWllslNFZlKOkCRffHBH3ufMh7ujN1RZfTtGffC1teKc7ps76z2fn2HelLvNuzFmHWiah4cyH2nRk4+1X/CzCpBxIug/GcyVul/8QFShodEbE5pHBj2s3QtizRe+KnP26/fqf5AgMBAAECggEARYmAowxF9sOitlma/bfoEzddyCs0BzUdNFoqm00rE/6tOXOdZptdXkZD87KL6N8QU7KrMlfWT/b1dkwWyJjNDSpD1uuyndsa3n0tGiRVIza4s4oykjyMg+oiOlGLU/T/SLv5c8tfnz4dIEUqqKD1vlThFXza902UUunCooUYb2PhYGpvwJW2noy3OSz9MU4MqbdmxlK+1I6AMYLTMbmDdZHV+gWwKdSHOfuk0KO9QGjHg0HByPAc7WuZVE+P5N7NAa3WNysOeCP9yQNd+qA+l91gGn98SZAvfmp/VtQ1YEiGEONakOuJ3EbBNNCdkZpFzQuGErM8dWk+FCNS72gUAQKBgQD3Io3ktRCB+hxU15G25dCj2TH04UeOirToZkYp2f/4Uz857JMyddHVDAnsMy16zWPtXp/hXD1lgfOWv5UoGFybOSRCUqguM1f8tiwnlq2ark5+olKyACd+PE/0NzwIHmNEGqzs8QcBN205Q2TUT9xx1AN7lxPFRmqbcSFBXF5BCQKBgQDIPAdGoGDe+pZ3K0OSw4FoMAJ9NUzEogVQVBQ/y03LgYwMnoGfcm3epFyoAp9U7tB/duIePqHx+O2mkGlYgGTBqSdBRRuAKgBjJY6BgMTvCYN/ioAwDKZmPUfQGw9yFQPbAwH8sI/hjrP9ptZyY5JnPsdj9yqJJ3Rxi2gy6sgbcQKBgQCplBJdf0kGh0LtjMY2BFg4Ng8rg3IAg3lDhvpaOXaFRUo7SvRuSZ5kXFBqvWtVup35AVcf4pVWk+c759ZHnv/cm0cgI2u32A9mHvMJb0FMxJKkHmo0LhrraiNK5qPQxllDIDLMm089LHVMN8x2Sx5vFRYWtkIgugLpCqXRHbqnUQKBgQCmj+itg3ORiHnpn3+ScjcPaABqDxBQ8UXiUE6X0pJgGt9ZD/FbpWBnt+mriKLpj864eTgXWJp2Ik/uImq/R+6IeiFBVi9OKaIw12j3qRoojY1pZtjDH4K7wXYfUqNCjuCqKpJYlLbbNk3bThFNDi8W204Zhd1IQh3G5eRDlrHmwQKBgQC+vulkPTIlz6ifFlxm+CxYbMlc5c+gNOdeW9WjunzFrot/CZVYYB4qoyywhAgVYrOSj6GlDTSWcijFcgMEbaSX7m8VdIJzRFnS6/26D3ZEZT42/BBFMsZUypIPM4u3aoQP/ij0PcEV0AFrBcjAfcz38elfUc0oEM/xpW4ZAoX61w==";
    public static String PLAT_PUB_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkXwP2PGUpluIV66KEhlb" +
            "aR6p6mA8SqfoY2/6ZwyzbaEMK3iJxJx1LYe4BjFGnUFAjl6ciEsvZ8PBvdDTGcLI" +
            "5q7RN/a7OKVemh7+BsnOcqYMJ+t7YCy0m0/ipKHv1iBi1xASXU8ToNtnfYPEzTYe" +
            "wrfwzeTSXO7S0ZQyhl9ybjT9+MmSJvEsV0idR0KcrvTJEkwHlvbA384xfOkEvhxE" +
            "lzPqzpfAm5l3mqW9uU54h22lNYUaabh4/6WqnVcyqdJolN6InOzVbjEu3laAJAUy" +
            "gJPEVK6hfe3B14H1cLULMEJS7ekLWCGFcdU5C8iNprkb8hXS+HdkJC1/SJJ27vVv" +
            "jwIDAQAB";
    public static String MCH_NO = "888000000044001";

    public static String GATEWAY_API = "http://192.168.0.239:20902/fx";

    public static void main(String[] args) {
        //1.初始化加载商户私钥和数美平台公钥
        //SecurityUtil.init(PLAT_PUB_KEY, MCH_PRIVATE_KEY);
        Test test = new Test();
        test.rate();
        //quoteAndDeal();
        //query();
    }

    private void rate() {
        SecurityUtil securityUtil = new SecurityUtil(PLAT_PUB_KEY, MCH_PRIVATE_KEY);
        RequestClient requestClient = new RequestClient(securityUtil);
        //2.按接口文档,封装请求的data数据
        JSONObject req = new JSONObject();
        req.put("sellCur", "CNH");
        req.put("buyCur", "USD");

        //3.封装请求体
        Request<JSONObject> request = new Request<>();
        request.setMchNo(MCH_NO);
        request.setMethod("exchange.inquiryRate");
        //request.setMethod("bizSource.dataPush");
        request.setVersion("1.0");
        request.setRandStr(StringUtil.gen32LenRand());
        request.setSignType("1");
        request.setSecKey("");
        request.setData(req);

        //4.发送请求,获得响应,doRequest方法内部已经处理了加签，验签,secKey的加解密等操作.详见 RequestUtil#doRequest
        Response<String> response = requestClient.doRequest(GATEWAY_API, request);
        System.out.println(JsonUtil.toString(response));

        JSONObject data = JSON.parseObject(response.getData(), JSONObject.class);

        if (Objects.equals(response.getRespCode(), RespCode.SUCCESS.getCode())) {
            System.out.println(data.toJSONString());
        } else if (Objects.equals(response.getRespCode(), RespCode.FAIL.getCode())) {
            System.out.println("bizErrCode:" + data.getString("bizErrCode"));
            System.out.println("bizErrMsg:" + data.getString("bizErrMsg"));
            //todo 失败处理
        } else {
            System.out.println("未知的响应状态");
            //todo 未知处理
        }
    }

    public void query() {
        SecurityUtil securityUtil = new SecurityUtil(PLAT_PUB_KEY, MCH_PRIVATE_KEY);
        RequestClient requestUtil = new RequestClient(securityUtil);
        //2.按接口文档,封装请求的data数据
        JSONObject req = new JSONObject();
        req.put("mchTrxNo", "f939e59c21814311a3954e815e48debf");
        req.put("exchangeTrxNo", "485002230608483763319623122952");

        //3.封装请求体
        Request<JSONObject> request = new Request<>();
        request.setMchNo(MCH_NO);
        request.setMethod("exchange.qry");
        //request.setMethod("bizSource.dataPush");
        request.setVersion("1.0");
        request.setRandStr(StringUtil.gen32LenRand());
        request.setSignType("1");
        request.setSecKey("");
        request.setData(req);

        //4.发送请求,获得响应,doRequest方法内部已经处理了加签，验签,secKey的加解密等操作.详见 RequestUtil#doRequest
        Response<String> response = requestUtil.doRequest(GATEWAY_API, request);

        JSONObject data = JSON.parseObject(response.getData(), JSONObject.class);

        if (Objects.equals(response.getRespCode(), RespCode.SUCCESS.getCode())) {
            System.out.println(data.toJSONString());
        } else if (Objects.equals(response.getRespCode(), RespCode.FAIL.getCode())) {
            System.out.println("bizErrCode:" + data.getString("bizErrCode"));
            System.out.println("bizErrMsg:" + data.getString("bizErrMsg"));
            //todo 失败处理
        } else {
            System.out.println("未知的响应状态");
            //todo 未知处理
        }
    }

    private void quoteAndDeal() {
        SecurityUtil securityUtil = new SecurityUtil(PLAT_PUB_KEY, MCH_PRIVATE_KEY);
        RequestClient requestUtil = new RequestClient(securityUtil);
        //2.按接口文档,封装请求的data数据
        JSONObject req = new JSONObject();
        req.put("sellCur", "USD");
        req.put("buyCur", "CNH");
        req.put("tradeAmount", new BigDecimal("800"));
        req.put("tradeMode", 2);

        //3.封装请求体
        Request<JSONObject> request = new Request<>();
        request.setMchNo(MCH_NO);
        request.setMethod("exchange.quote");
        //request.setMethod("bizSource.dataPush");
        request.setVersion("1.0");
        request.setRandStr(StringUtil.gen32LenRand());
        request.setSignType("1");
        request.setSecKey("");
        request.setData(req);

        //4.发送请求,获得响应,doRequest方法内部已经处理了加签，验签,secKey的加解密等操作.详见 RequestUtil#doRequest
        Response<String> response = requestUtil.doRequest(GATEWAY_API, request);

        JSONObject data = JSON.parseObject(response.getData(), JSONObject.class);
        DemoRespData demoRespData = null;

        if (Objects.equals(response.getRespCode(), RespCode.SUCCESS.getCode())) {
            System.out.println(data.toJSONString());
            deal(data);
            req.put("quoteId", data.getString("quoteId"));
            //demoRespData = data.toJavaObject(DemoRespData.class);
        } else if (Objects.equals(response.getRespCode(), RespCode.FAIL.getCode())) {
            System.out.println("bizErrCode:" + data.getString("bizErrCode"));
            System.out.println("bizErrMsg:" + data.getString("bizErrMsg"));
            //todo 失败处理
        } else {
            System.out.println("未知的响应状态");
            //todo 未知处理
        }
    }

    private static void deal(JSONObject quoteRes) {
        SecurityUtil securityUtil = new SecurityUtil(PLAT_PUB_KEY, MCH_PRIVATE_KEY);
        RequestClient requestUtil = new RequestClient(securityUtil);
        JSONObject req = new JSONObject();
        String mchTrxNo = UUID.randomUUID().toString().replaceAll("-", "");
        System.out.println("商户流水号：" + mchTrxNo);
        req.put("mchTrxNo", mchTrxNo);
        req.put("quoteId", quoteRes.getString("quoteId"));
        req.put("sellCur", quoteRes.getString("sellCur"));
        req.put("buyCur", quoteRes.getString("buyCur"));
        req.put("tradeAmount", quoteRes.getBigDecimal("tradeAmount"));
        req.put("tradeMode", quoteRes.getIntValue("tradeMode"));

        //3.封装请求体
        Request<JSONObject> request = new Request<>();
        request.setMchNo(MCH_NO);
        request.setMethod("exchange.deal");
        //request.setMethod("bizSource.dataPush");
        request.setVersion("1.0");
        request.setRandStr(StringUtil.gen32LenRand());
        request.setSignType("1");
        request.setSecKey("");
        request.setData(req);

        //4.发送请求,获得响应,doRequest方法内部已经处理了加签，验签,secKey的加解密等操作.详见 RequestUtil#doRequest
        Response<String> response = requestUtil.doRequest(GATEWAY_API, request);

        JSONObject data = JSON.parseObject(response.getData(), JSONObject.class);
        System.out.println(data.toJSONString());
    }


}

class DemoReqData {
    String demoKey1;

    public String getDemoKey1() {
        return demoKey1;
    }

    public void setDemoKey1(String demoKey1) {
        this.demoKey1 = demoKey1;
    }
}

class DemoRespData {
    String demoKey2;

    public String getDemoKey2() {
        return demoKey2;
    }

    public void setDemoKey2(String demoKey2) {
        this.demoKey2 = demoKey2;
    }
}