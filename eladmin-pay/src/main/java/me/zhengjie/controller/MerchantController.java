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

import cn.hutool.core.util.IdUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.domain.Merchant;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.mapstruct.MerchantMapper;
import me.zhengjie.repository.MerchantRepository;
import me.zhengjie.service.MerchantService;
import me.zhengjie.service.dto.MerchantDto;
import me.zhengjie.service.dto.MerchantQueryCriteria;
import me.zhengjie.utils.PageResult;
import me.zhengjie.utils.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * @author Zheng Jie
 * @date 2018-11-23
 */
@Api(tags = "系统：商户管理")
@RestController
@RequestMapping("/api/merchant")
@RequiredArgsConstructor
public class MerchantController {

    private final PasswordEncoder passwordEncoder;
    private final MerchantService merchantService;
    private final MerchantRepository merchantRepository;
//    private final DataService dataService;
//    private final RoleService roleService;

    @ApiOperation("查询商户列表")
    @GetMapping
    @PreAuthorize("@el.check('merchant:list')")
    public ResponseEntity<PageResult<MerchantDto>> query(MerchantQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(merchantService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @Log("新增商户")
    @ApiOperation("新增商户")
    @PostMapping
    @PreAuthorize("@el.check('merchant:add')")
    public ResponseEntity<Merchant> create(@Validated @RequestBody Merchant merchant) {
        // 1. 验证邮箱是否已被注册
        String email = merchant.getEmail().trim();
        if (merchantRepository.findByEmail(email).isPresent()) {
            throw new BadRequestException(String.format("邮箱【%s】已被其他商户注册，请使用其他邮箱或联系管理员", email));
        }
        // 2. 自动生成商户id和商户key
        merchant.setUsername(String.format("merchant_%d", System.currentTimeMillis()));
        String merchantKey = UUID.randomUUID().toString().replace("-", "");
        merchant.setPassword(passwordEncoder.encode(merchantKey));
        merchantService.create(merchant);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Log("修改商户")
    @ApiOperation("修改商户")
    @PutMapping
    @PreAuthorize("@el.check('merchant:edit')")
    public ResponseEntity<Object> updateMerchant(@Validated(Merchant.Update.class) @RequestBody Merchant resources) throws Exception {
        merchantService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除商户")
    @ApiOperation("删除商户")
    @DeleteMapping
    @PreAuthorize("@el.check('merchant:del')")
    public ResponseEntity<Object> deleteMerchant(@RequestBody Set<Long> ids) {
//        for (Long id : ids) {
//            Integer currentLevel = Collections.min(roleService.findByMerchantsId(SecurityUtils.getCurrentMerchantId()).stream().map(RoleSmallDto::getLevel).collect(Collectors.toList()));
//            Integer optLevel = Collections.min(roleService.findByMerchantsId(id).stream().map(RoleSmallDto::getLevel).collect(Collectors.toList()));
//            if (currentLevel > optLevel) {
//                throw new BadRequestException("角色权限不足，不能删除：" + merchantService.findById(id).getMerchantname());
//            }
//        }
//        merchantService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

//    @ApiOperation("修改密码")
//    @PostMapping(value = "/updatePass")
//    public ResponseEntity<Object> updateMerchantPass(@RequestBody MerchantPassVo passVo) throws Exception {

    /// /        String oldPass = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, passVo.getOldPass());
    /// /        String newPass = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, passVo.getNewPass());
    /// /        MerchantDto Merchant = merchantService.findByName(SecurityUtils.getCurrentMerchantname());
    /// /        if (!passwordEncoder.matches(oldPass, Merchant.getPassword())) {
    /// /            throw new BadRequestException("修改失败，旧密码错误");
    /// /        }
    /// /        if (passwordEncoder.matches(newPass, Merchant.getPassword())) {
    /// /            throw new BadRequestException("新密码不能与旧密码相同");
    /// /        }
    /// /        merchantService.updatePass(Merchant.getMerchantname(), passwordEncoder.encode(newPass));
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
    @ApiOperation("重置密码")
    @PutMapping(value = "/resetPwd")
    public ResponseEntity<Object> resetPwd(@RequestBody Set<Long> ids) {
//        String pwd = passwordEncoder.encode("123456");
//        merchantService.resetPwd(ids, pwd);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/findOne")
    public ResponseEntity<Merchant> findOne(@RequestBody Merchant example) {
        Optional<Merchant> optional = merchantService.findOne(example);
        return optional.map(merchant -> new ResponseEntity<>(merchant, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.OK));
    }
}
