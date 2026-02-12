package com.example.okrmanagement.service;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.okrmanagement.common.ErrorCode;
import com.example.okrmanagement.entity.Anniversary;
import com.example.okrmanagement.entity.User;
import com.example.okrmanagement.exception.BusinessException;
import com.example.okrmanagement.mapper.AnniversaryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnniversaryService extends ServiceImpl<AnniversaryMapper, Anniversary> {

    @Autowired
    private UserService userService;

    private User getCurrentUser() {
        long userId = StpUtil.getLoginIdAsLong();
        return userService.getUserById(userId);
    }

    public Anniversary createAnniversary(Anniversary anniversary) {
        User user = getCurrentUser();
        anniversary.setUserId(user.getId());
        save(anniversary);
        return anniversary;
    }

    public List<Anniversary> getAnniversaries() {
        User user = getCurrentUser();
        return lambdaQuery().eq(Anniversary::getUserId, user.getId()).list();
    }

    public Anniversary updateAnniversary(Long anniversaryId, Anniversary updatedAnniversary) {
        User user = getCurrentUser();
        Anniversary anniversary = getById(anniversaryId);
        if (anniversary == null) {
            throw new BusinessException(ErrorCode.ANNIVERSARY_NOT_FOUND);
        }
        if (!anniversary.getUserId().equals(user.getId())) {
            throw new BusinessException(ErrorCode.NO_PERMISSION);
        }
        anniversary.setTitle(updatedAnniversary.getTitle());
        anniversary.setDescription(updatedAnniversary.getDescription());
        anniversary.setAnniversaryDate(updatedAnniversary.getAnniversaryDate());
        anniversary.setCoverUrl(updatedAnniversary.getCoverUrl());
        updateById(anniversary);
        return anniversary;
    }

    public void deleteAnniversary(Long anniversaryId) {
        User user = getCurrentUser();
        Anniversary anniversary = getById(anniversaryId);
        if (anniversary == null) {
            throw new BusinessException(ErrorCode.ANNIVERSARY_NOT_FOUND);
        }
        if (!anniversary.getUserId().equals(user.getId())) {
            throw new BusinessException(ErrorCode.NO_PERMISSION);
        }
        removeById(anniversaryId);
    }
}
