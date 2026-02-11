# 支付系统

## 主要功能

### 商户入驻，接入支付功能
### 商户创建订单，后台返回支付链接，网页访问链接，拉起支付应用
### 用户支付订单，成功|失败回调

### 系统管理员使用
- 商户管理
- 支付管理
- 应用管理
- 订单管理

### 商户使用
- 应用管理
- 订单管理

## 接口

### 创建订单
- 请求参数
```json
{
  "tenantId": "租户id",
  "tenantSecret": "租户密钥",
  "skuName": "商品名称",
  "skuPrice": 1.23,
  "skuQuantity": 1,
  "payMethod": "支付方式（ALIPAY|WXPAY|VISA|CASH）",
  "extraParam": "额外参数"
}
```

- 响应参数
```json
{
  "result": "SUCCESS|TENANT_INFO_ERROR|SKU_PRICE_ERROR|SERVER_ERROR",
  "description": "具体描述",
  "orderId": "订单id",
  "payLink": "支付链接",
  "payQRCode": "支付二维码",
  "createTime": "订单创建时间",
  "duration": "订单有效时长（单位毫秒）"
}
```

### 查询订单
- 请求参数
```json
{
  "tenantId": "租户id",
  "tenantSecret": "租户密钥",
  "orderId": "订单id"
}
```

- 响应参数
```json
{
  "result": "SUCCESS|TENANT_INFO_ERROR|ORDER_NOT_EXIST|PERMISSION_DENY|SERVER_ERROR",
  "description": "具体描述",
  "orderId": "订单id",
  "orderStatus": "订单状态（UNPAY|PAY_SUCCESS|PAY_AMOUNT_MISMATCH|TIMEOUT）",
  "createTime": "订单创建时间",
  "payTime": "",
  "payMethod": "支付方式（ALIPAY|WXPAY|VISA|CASH）",
  "payMoney": 0.01,
  "payDevice": "支付设备",
  "payIp": "支付ip",
  "skuName": "商品名称",
  "skuPrice": 1.23,
  "skuQuantity": 1
}
```

### 关闭订单

### 订单退款