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
 * 支付方式
 * @author zhengjie
 * @date 2018/08/01 16:45:43
 */
public enum PayMethod {

    /** 支付宝支付 */
    ALIPAY("ALIPAY"),

    /** 微信支付 */
    WXPAY("WXPAY");

    private final String value;

    PayMethod(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
