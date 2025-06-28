package com.jieni.aianswer.controller;

import cn.hutool.core.io.FileUtil;
import com.jieni.aianswer.common.BaseResponse;
import com.jieni.aianswer.common.ErrorCode;
import com.jieni.aianswer.common.ResultUtils;
import com.jieni.aianswer.constant.FileConstant;
import com.jieni.aianswer.exception.BusinessException;
import com.jieni.aianswer.exception.ThrowUtils;
import com.jieni.aianswer.manager.CosManager;
import com.jieni.aianswer.mapper.UserAnswerMapper;
import com.jieni.aianswer.model.dto.file.UploadFileRequest;
import com.jieni.aianswer.model.dto.statistic.AppAnswerCountDTO;
import com.jieni.aianswer.model.dto.statistic.AppAnswerResultCountDTO;
import com.jieni.aianswer.model.entity.User;
import com.jieni.aianswer.model.enums.FileUploadBizEnum;
import com.jieni.aianswer.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * 统计分析接口
 *
 * @author Jieni
 */
@RestController
@RequestMapping("/app/statistic")
@Slf4j
public class AppStatisticController {

    @Resource
    private UserAnswerMapper userAnswerMapper;

    /**
     * 热门应用及回答数统计（top 10）
     *
     * @return
     */
    @GetMapping("/answer_count")
    public BaseResponse<List<AppAnswerCountDTO>> getAppAnswerCount() {
        return ResultUtils.success(userAnswerMapper.doAppAnswerCount());
    }

    /**
     * 某应用回答结果分布统计
     *
     * @param appId
     * @return
     */
    @GetMapping("/answer_result_count")
    public BaseResponse<List<AppAnswerResultCountDTO>> getAppAnswerResultCount(Long appId) {
        ThrowUtils.throwIf(appId == null || appId <= 0, ErrorCode.PARAMS_ERROR);
        return ResultUtils.success(userAnswerMapper.doAppAnswerResultCount(appId));
    }
}

