package com.qqgsd.crowd.handler;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.qqgsd.crowd.api.MysqlRemoteService;
import com.qqgsd.crowd.config.PayProperties;
import com.qqgsd.crowd.entity.vo.OrderProjectVO;
import com.qqgsd.crowd.entity.vo.OrderVO;
import com.qqgsd.crowd.util.ResultEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

public class PayHandler {

    @Autowired
    private PayProperties payProperties;

    private final Logger logger= LoggerFactory.getLogger(PayHandler.class);

    @Autowired
    private MysqlRemoteService mysqlRemoteService;

    @ResponseBody
    @RequestMapping(value = "/generate/order")
    public String generateOrder(HttpSession session, OrderVO orderVO) throws AlipayApiException {

        OrderProjectVO orderProjectVO= (OrderProjectVO) session.getAttribute("orderProjectVO");
        orderVO.setOrderProjectVO(orderProjectVO);
        String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String user = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        String orderNum = time + user;
        orderVO.setOrderNum(orderNum);
        Double orderAmount = (double) (orderProjectVO.getSupportPrice() * orderProjectVO.getReturnContent() + orderProjectVO.getFreight());
        orderVO.setOrderAmount(orderAmount);
        session.setAttribute("orderVO",orderVO);
        return sendRequestToAliPay(orderNum, orderAmount, orderProjectVO.getProjectName(), String.valueOf(orderProjectVO.getReturnContent()));
    }

    /**
     * 为了调用支付宝接口专门封装的方法
     * @param outTradeNo 外部订单号，也就是商户订单号，也就是我们生成的订单号
     * @param totalAmount 订单的总金额
     * @param subject 订单的标题，这里可以使用项目名称
     * @param body 商品的描述，这里可以使用回报描述
     * @return 返回到页面上显示的支付宝登录界面
     */
    private String sendRequestToAliPay(String outTradeNo, Double totalAmount, String subject, String body) throws AlipayApiException {
        //获得初始化的 AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(
                payProperties.getGatewayUrl(),
                payProperties.getAppId(),
                payProperties.getMerchantPrivateKey(),
                "json", payProperties.getCharset(),
                payProperties.getAlipayPublicKey(),
                payProperties.getSignType());
        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(payProperties.getReturnUrl());
        alipayRequest.setNotifyUrl(payProperties.getNotifyUrl());
        alipayRequest.setBizContent("{\"out_trade_no\":\""+ outTradeNo +"\"," + "\"total_amount\":\""+ totalAmount +"\"," + "\"subject\":\""+ subject +"\"," + "\"body\":\""+ body +"\"," + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        //请求
        return alipayClient.pageExecute(alipayRequest).getBody();
    }

    @RequestMapping(value = "/return")
    public String returnUrlMethod(HttpServletRequest request,HttpSession session) throws AlipayApiException {
        Map<String,String> params = new HashMap<>();
        Map<String,String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
// 乱码解决，这段代码在出现乱码时使用
            valueStr = new String(valueStr.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            params.put(name, valueStr);
        }
        boolean signVerified = AlipaySignature.rsaCheckV1(
                params, payProperties.getAlipayPublicKey(), payProperties.getCharset(), payProperties.getSignType()); //调用 SDK 验证签名
        // ——请在这里编写您的程序（以下代码仅作参考）——
        if(signVerified) {
            // 商户订单号
            String orderNum = new
                    String(request.getParameter("out_trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            // 支付宝交易号
            String payOrderNum = new
                    String(request.getParameter("trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            // 付款金额
            String orderAmount = new
                    String(request.getParameter("total_amount").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            // 保存到数据库
            OrderVO orderVO = (OrderVO) session.getAttribute("orderVO");
            orderVO.setPayOrderNum(payOrderNum);
            ResultEntity<String> resultEntity=mysqlRemoteService.saveOrderRemote(orderVO);
            logger.info("Order save result="+resultEntity.getMessage());

            return "trade_no:"+payOrderNum+"<br/>out_trade_no:"+orderNum+"<br/>total_amount:"+orderAmount;
        }else {
            // 页面显示信息：验签失败
            return "验签失败";
        }

    }

    @RequestMapping("/notify")
    public void notifyUrlMethod(HttpServletRequest request) throws
            UnsupportedEncodingException, AlipayApiException {
        //获取支付宝 POST 过来反馈信息
        Map<String,String> params = new HashMap<String,String>();
        Map<String,String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            valueStr = new String(valueStr.getBytes(StandardCharsets.ISO_8859_1), "utf-8");
            params.put(name, valueStr);
        }
        boolean signVerified = AlipaySignature.rsaCheckV1(
                params, payProperties.getAlipayPublicKey(), payProperties.getCharset(), payProperties.getSignType()); //调用 SDK 验证签名

        if(signVerified) {//验证成功
            //商户订单号
            String out_trade_no = new
                    String(request.getParameter("out_trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            //支付宝交易号
            String trade_no = new
                    String(request.getParameter("trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            //交易状态
            String trade_status = new
                    String(request.getParameter("trade_status").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            logger.info("out_trade_no="+out_trade_no);
            logger.info("trade_no="+trade_no);
            logger.info("trade_status="+trade_status);
        }else {//验证失败
            logger.info("验证失败");
        }
    }
}
