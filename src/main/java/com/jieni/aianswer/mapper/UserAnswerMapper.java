package com.jieni.aianswer.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jieni.aianswer.model.dto.statistic.AppAnswerCountDTO;
import com.jieni.aianswer.model.dto.statistic.AppAnswerResultCountDTO;
import com.jieni.aianswer.model.entity.UserAnswer;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author Admin
* @description 针对表【user_answer(用户答题记录)】的数据库操作Mapper
* @createDate 2025-06-14 22:35:23
* @Entity generator.domain.UserAnswer
*/
public interface UserAnswerMapper extends BaseMapper<UserAnswer> {
    @Select("select appId, count(userId) as answerCount from user_answer\n" +
            "    group by appId order by answerCount desc limit 10;")
    List<AppAnswerCountDTO> doAppAnswerCount();


    @Select("select resultName, count(resultName) as resultCount from user_answer\n" +
            "    where appId = #{appId}\n" +
            "    group by resultName order by resultCount desc;")
    List<AppAnswerResultCountDTO> doAppAnswerResultCount(Long appId);
}




