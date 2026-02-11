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
package me.zhengjie.domain.enums;

/**
 * 订单状态
 * @author zhengjie
 * @date 2018/08/01 16:45:43
 */
public enum PayStatus {

    /** 订单未支付 */
    UNPAY("UNPAY"),

    /** 订单完全支付 */
    FULL_PAID("FULL_PAID"),

    /** 订单部分支付 */
    PARTIAL_PAID("PARTIAL_PAID"),

    /** 订单超时 */
    PAY_TIMEOUT("PAY_TIMEOUT");

    private final String value;

    PayStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
