import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shumeit.sdk.api.entity.Request;
import com.shumeit.sdk.api.entity.Response;
import com.shumeit.sdk.api.enums.RespCode;
import com.shumeit.sdk.api.utils.RequestUtil;
import com.shumeit.sdk.api.utils.SecurityUtil;
import com.shumeit.sdk.api.utils.StringUtil;

import java.util.Objects;

/**
 * Author: Cmf
 * Date: 2022.2.22
 * Time: 16:29
 * Description:
 */
public class Test {
    public static String MCH_PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCrRMvZdRn4BgRxLrlFWa3896MiPXYbNt78rmgmio02n4bEweqKN6LDijP+EM7rJ2fuNOERPyKbK0UEOJMXSFLjl+i1A83ZnbYgy4u6MnaMRoUV5ARyn+IAuugwISh8Rcp6BuLPmGi+bZcegsP/t8N3FCBbtL/YkqjzLHg6fB5wh1gEm+lVBP0sOSJ39mdMVyHWZ0aO7Q5DxmNW94kj8Ud8l2ezrDvPoHIexGZ5SIHfW+YAPqZCK1FqkeDNuj+VpyUMItvEJbcHzzF3DpbSZSQUhgrPB7HUQ8wfoGZEu8N1W9Ql6Jo9UlSm84l+0gcrWFEUbGiE6wiP4JK6Yly9vlI1AgMBAAECggEAC17a5coHelbz7ou6kyccDQJn2zsXX0EqVr5BfeWO8IRwzxlMt/ZvoOnI/+Ifyq7oEIGEO7/kTUb3IwuagF412pRYR8RD42G8GLtKwIbfJfr8lPJ5dR62Q3QlPCTHalC725QfdMbalWHzOXxXBOAvYvV8HHqIjhlWjCZ0qPVak//cVGsvfArNc2w4l2nAZeyQ5jxhGhtdTa0SLSGJK0n9nkgEzeG0DcM8qj+v3qi1fMaAeI18aruSqYvZgyZbiJEyigJmDJMNdiQIlzpyJ7zOiskVVkPmfbpagmUrBP86ZW14eC5pWrzPhJxN7jKxCufER9fWLVHtAWNzzrKFycQjAQKBgQDq4SBAdtx46ZuyvNHFN2UaKS+m27XoqIlvPCXytZh6uOotIzGAgPjyPyKlcxAfH7WgLSQldJVA9BSjYBO/1ZL0OQqNy1sRCinHa6uLqBMTPTHCePyzuSwMB8r4N0uKMSzH7wwZK+xoCZz9owDcotaoSOPVqAScmRP1O+D1C7As9QKBgQC6q13Gr06dMaFgsLVxNRKTx4BEiGnXHYpGK6m8vohJbI9zwoUXPfx+btINX9qXQvl0L1zQdKALGgauCt7iMGolOLXO05nAsQLc/yWJTRhypvy5z/B4r24b89gru8IK+1nyK4NGkz3maXRJOb5D6mwsQo3PGlHl2x5yLmiqOSZIQQKBgAEqLqPDMyKyXeBMCWpF9Q1lKORV8Jf/xfa0QKfseJ4bLqYcp5Ewkot4/q39XRYabScnMiexZVAWJcgNoOtxnrHxWnkLih8SgtSHVylxIu8UfZuuR+qIP4yruTjz5T8BMWyoZdH2Isz9RzcGyOlPMWtSOTcFB0jThYLQ0jPE5BHFAoGBAKsfk70jUo8aQ1VvSTY5sAkXV4f8AgL5CpyfRGBJzoh449z8EqQm6ARDtX4bRDlsZWBUVuKmN11WA4+jWYXDWls67LhpFMcnvnc304JV0baxTHdOn6UTNX6cgKw+2HjWyTMPHq4hJxrFy5uhOsHp8jZ04Kld+Hwd7oA/+SUrdOPBAoGBAIqEvU+LxiG05ceiOR8b1eSbGfceaA3pU/UJF6lullER/pxjLZA8caaruj3pMRl9HofBD05yKb/nHSJlo3DjBx+FUreig3wX7o9cMMLGf8kz1Nrj+6uGNKxDG4nj4VB60Js7XLPr5ZTWR+X5nYGkRoJJmz9nh9mjc3c2lLFiawB2";
    public static String PLAT_PUB_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqWl3fGWWbV4brGj7FcBKgxj2Xa+4vANwX+lfKbNMRtp39GSOyyBHWw0ka+yqt5vn0lKJw1Us8BIJKbDlbxcRszjPWb1spAEheYXKobVoYppoQ+kc5WDNXDBb8Qsb3Q3pU40+b7N1FMpy/7G1vBlRIit13n2P+QbpguKcYMzUhuvP36f4svE1wJUgoPSHBva4y337mKqwLd0kPUVqXBmQgZz4txyh7r86zg9t0v0gJOUivN+Tc63nYiYqxJQv8Eeo6Ui6Zdx62ZIE3G932D+S3sS0MbOgPoZrLb+zxzmXz2V0ELgCAaUhhZLlC8ygtBQs7RxC4H0Eem/GGKFSc1jdGwIDAQAB";
    public static String MCH_NO = "888000000020001";

    public static String GATEWAY_API = "http://192.168.0.236:20902/trade";


    public static void main(String[] args) {
        //1.初始化加载商户私钥和数美平台公钥
        SecurityUtil.init(PLAT_PUB_KEY, MCH_PRIVATE_KEY);

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
        Response<String> response = RequestUtil.doRequest(GATEWAY_API, request);

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