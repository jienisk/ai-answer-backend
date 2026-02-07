package com.jieni.aianswer.model.vo;

import cn.hutool.json.JSONUtil;

import com.alibaba.excel.util.StringUtils;
import com.jieni.aianswer.model.entity.UserAnswer;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;  // 【修改1】新增导入

//import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

/**
 * 用户答案视图
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://www.code-nav.cn">编程导航学习圈</a>
 */
@Data
public class UserAnswerVO implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(UserAnswerVO.class);
    /**
     *
     */
    private Long id;

    /**
     * 应用 id
     */
    private Long appId;

    /**
     * 应用类型（0-得分类，1-角色测评类）
     */
    private Integer appType;

    /**
     * 评分策略（0-自定义，1-AI）
     */
    private Integer scoringStrategy;

    /**
     * 用户答案（JSON 数组）
     */
    private List<String> choices;

    /**
     * 评分结果 id
     */
    private Long resultId;

    /**
     * 结果名称，如物流师
     */
    private String resultName;

    /**
     * 结果描述
     */
    private String resultDesc;

    /**
     * 结果图标
     */
    private String resultPicture;

    /**
     * 得分
     */
    private Integer resultScore;

    /**
     * 用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建用户信息
     */
    private UserVO user;

    /**
     * 封装类转对象
     *
     * @param userAnswerVO
     * @return
     */
    public static UserAnswer voToObj(UserAnswerVO userAnswerVO) {
        if (userAnswerVO == null) {
            return null;
        }
        UserAnswer userAnswer = new UserAnswer();
        BeanUtils.copyProperties(userAnswerVO, userAnswer);
        userAnswer.setChoices(JSONUtil.toJsonStr(userAnswerVO.getChoices()));
        return userAnswer;
    }

    /**
     * 对象转封装类
     *
     * @param userAnswer
     * @return
     */
    public static UserAnswerVO objToVo(UserAnswer userAnswer) {
        if (userAnswer == null) {
            return null;
        }
        UserAnswerVO userAnswerVO = new UserAnswerVO();
        BeanUtils.copyProperties(userAnswer, userAnswerVO);

        // 修复 choices 字段的 JSON 解析 - 添加容错处理
        userAnswerVO.setChoices(parseChoicesSafely(userAnswer.getChoices()));

        return userAnswerVO;
    }

    private static List<String> parseChoicesSafely(String choicesStr) {
        // 如果为空，返回空列表
        if (StringUtils.isBlank(choicesStr)) {
            return new ArrayList<>();
        }

        String trimmedChoices = choicesStr.trim();

        try {
            // 情况1：已经是有效的 JSON 数组格式
            if (trimmedChoices.startsWith("[") && trimmedChoices.endsWith("]")) {
                return JSONUtil.toList(trimmedChoices, String.class);
            }

            // 情况2：逗号分隔的字符串 "A,B,C"
            if (trimmedChoices.contains(",")) {
                return Arrays.stream(trimmedChoices.split(","))
                        .map(String::trim)
                        .filter(StringUtils::isNotBlank)
                        .collect(Collectors.toList());
            }

            // 情况3：单个字符串 "A"
            return Arrays.asList(trimmedChoices);

        } catch (Exception e) {
            // 记录错误日志，但不让整个请求失败
//            log.print("解析 choices 字段失败，原始值: '{}', 错误: {}");
            log.error("解析 choices 字段失败，原始值: '{}', 错误: {}", choicesStr, e.getMessage());
            // 尝试最后的挽救：如果是被转义的 JSON 字符串
            try {
                // 处理可能被转义过的 JSON 字符串
                String unescaped = trimmedChoices.replace("\\\"", "\"");
                if (unescaped.startsWith("[") && unescaped.endsWith("]")) {
                    return JSONUtil.toList(unescaped, String.class);
                }
            } catch (Exception ex) {
//                log.print("二次解析 choices 字段也失败: {}");
                log.error("二次解析 choices 字段也失败: {}", ex.getMessage());
            }

            // 如果所有解析都失败，返回包含原始值的列表
            return Arrays.asList(choicesStr);
        }
    }
}
