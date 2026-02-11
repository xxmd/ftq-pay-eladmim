/*
 *  Copyright 2019-2025 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package me.zhengjie.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseEntity;
import me.zhengjie.domain.enums.PayStatus;
import me.zhengjie.domain.enums.PayMethod;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Zheng Jie
 * @date 2019-03-25
 */
@Entity
@Getter
@Setter
@Table(name = "pay_order")
public class Order extends BaseEntity implements Serializable {

    @Id
    @NotNull(groups = Update.class)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String serialNumber;

    @NotBlank
    private String skuName;

    @NotNull
    private BigDecimal skuPrice;

    @NotNull
    private Integer skuQuantity;

    private String extraParam;

    @Enumerated(EnumType.STRING)
    @NotNull
    private PayMethod payMethod;

    private String payLink;

    @Column(name = "pay_qr_code")
    private String payQRCode;

    @Enumerated(EnumType.STRING)
    private PayStatus payStatus = PayStatus.UNPAY;

    private BigDecimal payAmount;

    @Column(name = "purchaser_ua")
    private String purchaserUA;

    private String purchaserIp;

    @NotNull
    @ApiModelProperty(value = "是否启用")
    private Boolean enabled;
}