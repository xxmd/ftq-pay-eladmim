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
package me.zhengjie.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.domain.Order;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.mapstruct.OrderMapper;
import me.zhengjie.service.OrderService;
import me.zhengjie.service.dto.OrderDto;
import me.zhengjie.service.dto.OrderQueryCriteria;
import me.zhengjie.utils.PageResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Zheng Jie
 * @date 2018-12-03
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "支付：订单管理")
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;
    private static final String ENTITY_NAME = "order";

    @GetMapping
    @ApiOperation("查询订单")
    @PreAuthorize("@el.check('order:list')")
    public ResponseEntity<PageResult<Order>> query(OrderQueryCriteria criteria, Pageable pageable) throws Exception {
        return new ResponseEntity<>(orderService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @Log("新增订单")
    @ApiOperation("新增订单")
    @PostMapping
    @PreAuthorize("@el.check('order:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody Order resources) {
        if (resources.getId() != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        orderService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Log("修改订单")
    @ApiOperation("修改订单")
    @PutMapping
    @PreAuthorize("@el.check('order:edit')")
    public ResponseEntity<Object> update(@Validated(Order.Update.class) @RequestBody Order resources) {
        orderService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

//    @Log("删除订单")
//    @ApiOperation("删除订单")
//    @DeleteMapping
//    @PreAuthorize("@el.check('order:del')")
//    public ResponseEntity<Object> deleteOrder(@RequestBody Set<Long> ids){
//        Set<Order> orderSet = new HashSet<>();
//        for (Long id : ids) {
//            List<OrderDto> orderList = orderService.getOrders(id);
//            orderSet.add(orderService.findOne(id));
//            orderSet = orderService.getChildOrders(orderMapper.toEntity(orderList), orderSet);
//        }
//        orderService.delete(orderSet);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
}
