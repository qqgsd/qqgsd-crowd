package com.qqgsd.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailReturnVO {

    // 回报信息主键
    private Integer returnId;

    // 当前支持的金额
    private Integer supportMoney;

    // 单笔限购 0：不限购
    private Integer signalPurchase;

    // 支持者的数量
    private Integer supporterCount;

    // 运费，取值为 0 时表示包邮
    private Integer freight;

    // 众筹成功后多少天发货
    private Integer returnDate;

    // 回报内容
    private String content;
}
