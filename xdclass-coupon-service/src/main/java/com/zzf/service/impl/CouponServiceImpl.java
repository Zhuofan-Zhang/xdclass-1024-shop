package com.zzf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzf.dto.CouponDTO;
import com.zzf.enums.BizCodeEnum;
import com.zzf.enums.CouponCategoryEnum;
import com.zzf.enums.CouponPublishEnum;
import com.zzf.enums.CouponStateEnum;
import com.zzf.exception.BizException;
import com.zzf.interceptor.LoginInterceptor;
import com.zzf.mapper.CouponMapper;
import com.zzf.mapper.CouponRecordMapper;
import com.zzf.model.CouponDO;
import com.zzf.model.CouponRecordDO;
import com.zzf.model.LoginUser;
import com.zzf.service.CouponService;
import com.zzf.util.CommonUtil;
import com.zzf.util.JsonData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zzf
 * @since 2023-09-09
 */
@Service
@Slf4j
public class CouponServiceImpl implements CouponService {

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private CouponRecordMapper couponRecordMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public Map<String, Object> getCouponListWithPagination(int page, int size) {

        Page<CouponDO> pageInfo = new Page<>(page, size);

        IPage<CouponDO> couponDOIPage = couponMapper.selectPage(pageInfo, new QueryWrapper<CouponDO>()
                .eq("publish", CouponPublishEnum.PUBLISH)
                .eq("category", CouponCategoryEnum.PROMOTION)
                .orderByDesc("created_at"));
        Map<String, Object> pageMap = new HashMap<>(3);
        pageMap.put("total_record", couponDOIPage.getTotal());
        pageMap.put("total_page", couponDOIPage.getPages());
        pageMap.put("current_data", couponDOIPage.getRecords().stream().map(obj -> processObj(obj)).collect(Collectors.toList()));


        return pageMap;
    }

    public JsonData addCoupon(long couponId, CouponCategoryEnum category) {

        String uuid = CommonUtil.generateUUID();
        String lockKey = "lock:coupon:" + couponId;

        Boolean lockFlag = redisTemplate.opsForValue().setIfAbsent(lockKey, uuid, Duration.ofMinutes(10));
        if (lockFlag) {
            //加锁成功
            log.info("add lock successfully:{}", couponId);
            try {
                validateAndUpdateCouponRecord(couponId, category);
            } finally {
                String script = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";

                Integer result = redisTemplate.execute(new DefaultRedisScript<>(script, Integer.class), Arrays.asList(lockKey), uuid);
                log.info("release lock：{}", result);
            }
        } else {
            //加锁失败
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                log.error("recursion error");
            }
            addCoupon(couponId, category);
        }

        validateAndUpdateCouponRecord(couponId, category);


        return JsonData.buildCodeAndMsg(0, "add coupon successfully");

    }

    private void validateAndUpdateCouponRecord(long couponId, CouponCategoryEnum category) {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();


        CouponDO couponDO = couponMapper.selectOne(new QueryWrapper<CouponDO>()
                .eq("id", couponId)
                .eq("category", category.name()));

        //优惠券是否可以领取
        this.validateCoupon(couponDO, loginUser.getId());

        //构建领劵记录
        CouponRecordDO couponRecordDO = new CouponRecordDO();
        BeanUtils.copyProperties(couponDO, couponRecordDO);
        couponRecordDO.setCreatedAt(new Date());
        couponRecordDO.setUsageState(CouponStateEnum.NEW.name());
        couponRecordDO.setUserId(loginUser.getId());
        couponRecordDO.setUserName(loginUser.getName());
        couponRecordDO.setCouponId(couponId);
        couponRecordDO.setId(null);

        //扣减库存
        int rows = couponMapper.reduceStock(couponId);

        //int flag = 1/0;

        if (rows == 1) {
            //库存扣减成功才保存记录
            couponRecordMapper.insert(couponRecordDO);

        } else {
            log.warn("Issue coupon failed:{},USER:{}", couponDO, loginUser);

            throw new BizException(BizCodeEnum.COUPON_NO_STOCK);
        }
    }

    private void validateCoupon(CouponDO couponDO, Long userId) {

        if (couponDO == null) {
            throw new BizException(BizCodeEnum.COUPON_NO_EXITS);
        }

        //库存是否足够
        if (couponDO.getRemaining() <= 0) {
            throw new BizException(BizCodeEnum.COUPON_NO_STOCK);
        }

        //判断是否是否发布状态
        if (!couponDO.getPublish().equals(CouponPublishEnum.PUBLISH.name())) {
            throw new BizException(BizCodeEnum.COUPON_GET_FAIL);
        }

        //是否在领取时间范围
        long time = CommonUtil.getCurrentTimestamp();
        long start = couponDO.getValidFrom().getTime();
        long end = couponDO.getValidUntil().getTime();
        if (time < start || time > end) {
            throw new BizException(BizCodeEnum.COUPON_OUT_OF_TIME);
        }

        //用户是否超过限制
        int recordNum = couponRecordMapper.selectCount(new QueryWrapper<CouponRecordDO>()
                .eq("coupon_id", couponDO.getId())
                .eq("user_id", userId));

        if (recordNum >= couponDO.getUserLimit()) {
            throw new BizException(BizCodeEnum.COUPON_OUT_OF_LIMIT);
        }


    }

    private CouponDTO processObj(CouponDO couponDO) {
        CouponDTO couponDTO = new CouponDTO();
        BeanUtils.copyProperties(couponDO, couponDTO);
        return couponDTO;
    }
}
