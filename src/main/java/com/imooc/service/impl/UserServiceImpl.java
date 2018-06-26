package com.imooc.service.impl;

import com.imooc.bean.User;
import com.imooc.constant.ApiCodeEnum;
import com.imooc.dao.UserDao;
import com.imooc.dto.ApiCodeDto;
import com.imooc.service.UserService;
import com.imooc.util.CommonUtil;
import com.imooc.util.Md5Util;
import com.imooc.util.cache.CodeCache;
import com.imooc.util.cache.TokenCache;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @author 潘畅
 * @date 2018/6/8 10:56
 */
@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userDao;


    @Override
    public ApiCodeDto sendSmsVerificationCode(Long phone) {
        if (phone == null){
            return new ApiCodeDto(ApiCodeEnum.PARAM_NULL);
        }
        User user = new User();
        user.setPhone(phone);
        List<User> userList = userDao.selectUser(user);
        if (CollectionUtils.isEmpty(userList)){
            return new ApiCodeDto(ApiCodeEnum.USER_NOT_EXISTS);
        }
        /*
         *     因为我们后续还需要验证码，而验证码又必须在“saveVerificationCode()”满足一定条件才能生成，
         * 所以这里放了一个容器，若生成了验证码，通过“StringBuilder”可以再拿出来（PS：最主要的原因是String
         * 类型指向的值是固定的，不可变，而“StringBuilder”是“指针”，它指向的值可随意改变）
         */
        StringBuilder codeStringBuilder = new StringBuilder();
        //将验证码缓存起来，并设置缓存时间1min
        if (!saveVerificationCode(String.valueOf(phone), codeStringBuilder)){
            //若将验证码保存进缓存失败，说明验证码还未过期，用户重复请求了
            return new ApiCodeDto(ApiCodeEnum.REPEAT_REQUEST);
        }
        //取出验证码
        String verificationCode = codeStringBuilder.toString();
        //发送验证码
        if (!sendVerificationCodeByThirdTunnel(phone, verificationCode)){
            return new ApiCodeDto(ApiCodeEnum.SEND_FAILURE);
        } else {
            return new ApiCodeDto(ApiCodeEnum.SUCCESS, verificationCode);
        }
    }

    @Override
    public ApiCodeDto login(Long phone, String verificationCode) {
        //先校验手机号
        String phoneNumber = String.valueOf(phone);
        if (StringUtils.isBlank(phoneNumber)){
            return new ApiCodeDto(ApiCodeEnum.PARAM_NULL);
        }
        //校验手机号是否已经注册
        User user = new User();
        user.setPhone(phone);
        List<User> userList = userDao.selectUser(user);
        if (CollectionUtils.isEmpty(userList)){
            return new ApiCodeDto(ApiCodeEnum.USER_NOT_EXISTS);
        }
        //校验验证码
        switch (verifyCode(phoneNumber, verificationCode)){
            case 0:
                //继续执行后续代码
                break;
            case 1:
                return new ApiCodeDto(ApiCodeEnum.CODE_ERROR);
            case 2:
                return new ApiCodeDto(ApiCodeEnum.CODE_INVALID);
            default:
        }

        //生成token,并保存进缓存
        StringBuilder tokenStringBuilder = new StringBuilder();
        saveToken(phoneNumber, tokenStringBuilder);

        ApiCodeDto apiCodeDto = new ApiCodeDto(ApiCodeEnum.SUCCESS);
        //从容器中取出token
        String token = tokenStringBuilder.toString();
        LOGGER.info(phone + "|" +token);
        apiCodeDto.setToken(token);
        return apiCodeDto;
    }

    /**
     * 生成token，并保存进缓存
     * @param phoneNumber 手机号
     * @param tokenStringBuilder token容器
     * 备注：其实，在登录阶段就应该验证token，若在缓存中能取到token，则说明该用户登陆过，且还未过期，此时直接登陆成功，
     *       无需再验证。但是由于我们的前端并没有传递token过来，所以这里就无法验证token
     */
    private void saveToken(String phoneNumber, StringBuilder tokenStringBuilder) {
        //生成32位token
        String token = CommonUtil.getUUID();
        //登录时返回给前端的token
        String responseToken;
        //TODO 真实场景：若能从缓存中取到token，则说明登录未过期，可直接登录
        TokenCache tokenCache = TokenCache.getInstance();
        try {
            /*
             *     这里其实肯定取不到，因为token是我现生成的，不是前端传过来的，但是我需要调用这个方法来初始化
             * BaseGuavaCache中的单例LoadingCache<K, V> cache
             */
            responseToken = (String) tokenCache.getValue(phoneNumber);
        } catch (Exception e) {
            //所有的cache都是公用BaseGuvaCache中的单例LoadingCache<K, V> cache，所以
            /**
             * 问题：为什么这里是tokenCache.put(token, phoneNumber)，而不是tokenCache.put(phoneNumber, token)？
             * 原因：不论是CodeCache还是TokenCache，都是公用BaseGuvaCache中的单例LoadingCache<K, V> cache对象，因为
             *       我们在缓存验证码时，已经将“phoneNumber”作为键，“验证码”作为值缓存起来了，若这里还用
             *       “phoneNumber”作为键，而我们的验证码缓存又没过期，那么就相当于同一个键值插入了2次，就会报错！！
             */
            tokenCache.put(phoneNumber, token);
            responseToken = token;
        }
        //登录时返回给前端的token放到容器中
        tokenStringBuilder.append(responseToken);
    }

    /**
     * 保存验证码进缓存
     * 备注：验证码的缓存是设置了过期时间的（1min），
     *           若“该手机号首次进入”或“该手机号对应的缓存已经过期”，此时调用“cache.get(key)”方法会抛出异常，
     *       此时需要保存手机号，验证码进缓存，返回true；
     *           若不抛出异常，则说明该缓存正处于有效期，用户在1min内重复请求发送验证码，无需保存进缓存，返回false
     * @param phone 手机号
     * @param codeStringBuilder 用来装验证码的容器
     * @return 是否成功保存手机号、验证码进缓存
     */
    private boolean saveVerificationCode(String phone, StringBuilder codeStringBuilder) {
        CodeCache codeCache = CodeCache.getInstance();
        try {
            codeCache.getValue(phone);
            //若不报异常，说明已存在该缓存，且未过期，说明在1min内重复请求了
            return false;
        } catch (Exception e) {
            LOGGER.info("缓存中无该手机号对应的验证码或该验证码已经过期！");
            //当缓存过期或没有时，取值会报异常，说明此时可将验证码存入缓存

            //获取6位随机验证码
            String verificationCode = String.valueOf(CommonUtil.getVerificationCode(6));
            //存入容器，留后续程序调用验证码
            codeStringBuilder.append(verificationCode);

            //存入缓存
            codeCache.put(phone, verificationCode);
            return true;
        }
    }

    /**
     * 调用第三方短信通道，发送验证码
     *
     * @param phone 手机号
     * @param verificationCode 验证码
     * @return 是否发送成功
     */
    private boolean sendVerificationCodeByThirdTunnel(Long phone, String verificationCode) {
        LOGGER.info(phone + "|" + verificationCode);
        return true;
    }

    /**
     * 校验手机号和验证码是否匹配
     * @param phoneNumber 手机号
     * @param verificationCode 验证码
     * @return 验证标识：0 成功 1 验证码不正确 2 验证码过期
     * 备注：当从缓存中取值报错时，要么缓存中无该值，要么缓存中该值已经过期
     */
    private int verifyCode(String phoneNumber, String verificationCode) {
        CodeCache codeCache = CodeCache.getInstance();
        try {
            String verificationCodeInCache = (String) codeCache.getValue(phoneNumber);
            if (verificationCode.equals(Md5Util.getMD5(verificationCodeInCache))){
                //验证成功
                return 0;
            } else {
                //验证码不正确
                return 1;
            }
        } catch (Exception e) {
            LOGGER.error("该手机号：" + phoneNumber +"对应的验证码已经过期！");
            //验证码已过期
            return 2;
        }
    }
}
