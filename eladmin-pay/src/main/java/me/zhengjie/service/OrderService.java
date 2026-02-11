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
package me.zhengjie.service;

import lombok.RequiredArgsConstructor;
import me.zhengjie.domain.Order;
import me.zhengjie.mapstruct.OrderMapper;
import me.zhengjie.repository.OrderRepository;
import me.zhengjie.service.dto.OrderDto;
import me.zhengjie.service.dto.OrderQueryCriteria;
import me.zhengjie.util.SerialNumberGenerator;
import me.zhengjie.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Zheng Jie
 */
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final RedisUtils redisUtils;
    @Autowired
    private StringRedisTemplate redisTemplate;
    private static final String DATE_FORMAT = "yyyyMMdd";
    private static final String REDIS_KEY_PREFIX = "order:serial:";

    public PageResult<Order> queryAll(OrderQueryCriteria criteria, Pageable pageable) throws Exception {
        Page<Order> page = orderRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page);
    }

    public OrderDto findById(long id) {
        Order order = orderRepository.findById(id).orElseGet(null);
        return orderMapper.toDto(order);
    }

    @Transactional(rollbackFor = Exception.class)
    public void create(Order resources) {
        resources.setSerialNumber(geneSerialNumber());
        resources.setPayLink("https://baidu.com");
        resources.setPayQRCode("https://baidu.com");
        HttpServletRequest request = RequestHolder.getHttpServletRequest();
        resources.setPurchaserUA(request.getHeader("User-Agent"));
        resources.setPurchaserIp(StringUtils.getIp(request));
        orderRepository.save(resources);
    }


    /**
     * 生成流水号（格式：年月日 + 用户id + 序列号）
     * 示例：2023122400010001
     */
    public String geneSerialNumber() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        String dateStr = sdf.format(new Date());
        Long currentUserId = SecurityUtils.getCurrentUserId();
        String formatUserId = String.format("%04d", currentUserId);
        String key = REDIS_KEY_PREFIX + formatUserId + ":" + dateStr;
        Long sequence = redisTemplate.opsForValue().increment(key, 1);
        if (sequence == 1) {
            redisTemplate.expire(key, 1, TimeUnit.DAYS);
        }
        String seqStr = String.format("%04d", sequence);
        return dateStr + formatUserId + seqStr;
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(Order resources) {
        orderRepository.save(resources);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Order> menuSet) {
        for (Order order : menuSet) {
            orderRepository.deleteById(order.getId());
        }
    }

    public Order findOne(Long id) {
        Order order = orderRepository.findById(id).orElseGet(Order::new);
        ValidationUtil.isNull(order.getId(), "Order", "id", id);
        return order;
    }
}
