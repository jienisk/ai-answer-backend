package com.jieni.aianswer.scoring;

import com.jieni.aianswer.model.entity.App;
import com.jieni.aianswer.model.entity.UserAnswer;

import java.util.List;

/**
 * 评分策略
 *
 * @author Jieni
 */
public interface ScoringStrategy {

    /**
     * 执行评分
     *
     * @param choices
     * @param app
     * @return
     * @throws Exception
     */
    UserAnswer doScore(List<String> choices, App app) throws Exception;
}