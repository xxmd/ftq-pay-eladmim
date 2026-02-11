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
import me.zhengjie.domain.Merchant;
import me.zhengjie.domain.Order;
import me.zhengjie.mapstruct.MerchantMapper;
import me.zhengjie.mapstruct.OrderMapper;
import me.zhengjie.repository.MerchantRepository;
import me.zhengjie.repository.OrderRepository;
import me.zhengjie.service.dto.MerchantDto;
import me.zhengjie.service.dto.MerchantQueryCriteria;
import me.zhengjie.service.dto.OrderDto;
import me.zhengjie.service.dto.OrderQueryCriteria;
import me.zhengjie.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Zheng Jie
 */
@Service
@RequiredArgsConstructor
public class MerchantService {
    private final MerchantRepository merchantRepository;
    private final MerchantMapper merchantMapper;

    public PageResult<MerchantDto> queryAll(MerchantQueryCriteria criteria, Pageable pageable) {
        Page<Merchant> page = merchantRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(merchantMapper::toDto));
    }

    public MerchantDto findById(long id) {
        Merchant item = merchantRepository.findById(id).orElseGet(null);
        return merchantMapper.toDto(item);
    }

    public MerchantDto findByEmail(long id) {
        Merchant item = merchantRepository.findById(id).orElseGet(null);
        return merchantMapper.toDto(item);
    }

    @Transactional(rollbackFor = Exception.class)
    public void create(Merchant merchant) {
        merchantRepository.save(merchant);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(Merchant item) {
        merchantRepository.save(item);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Merchant> merchantSet) {
        for (Merchant merchant : merchantSet) {
            merchantRepository.deleteById(merchant.getId());
        }
    }

    public Optional<Merchant> findOne(Merchant merchant) {
        if (merchant == null) {
            return Optional.empty();
        }
        if (StringUtils.isNotEmpty(merchant.getEmail())) {
            return merchantRepository.findByEmail(merchant.getEmail());
        }
        return Optional.empty();
    }

//    public Order findOne(Long id) {
//        Order order = orderRepository.findById(id).orElseGet(Order::new);
//        ValidationUtil.isNull(order.getId(), "Order", "id", id);
//        return order;
//    }
}
