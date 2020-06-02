package com.example.demo.service;

import com.example.demo.dto.PaginationDTO;
import com.example.demo.dto.QuestionDTO;
import com.example.demo.exception.CustomizeErrorCode;
import com.example.demo.exception.CustomizeException;
import com.example.demo.mapper.QuestionMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.Question;
import com.example.demo.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;

    public PaginationDTO list(Integer page, Integer size)/*根据输入参数：页码、每页展示数据个数进行PaginationDTO对象的查询*/ {
        PaginationDTO paginationDTO = new PaginationDTO();
        //根据输入参数：数据条数、页码、每页展示数据个数、进行逻辑判断，比如页码是否越界
        Integer totalCount = questionMapper.count();
        paginationDTO.setPagination(totalCount, page, size);
        if (page < 1) {
            page = 1;
        }
        if (page > paginationDTO.getTotalPage()) {
            page = paginationDTO.getTotalPage();
        }
        //size*(page-1)
        Integer offset = size * (page - 1);
        //第一步：通过questionMapper.list()方法得到questions对象
        List<Question> questions = questionMapper.list(offset, size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        //第二步：循环questions对象
        for (Question question : questions) {
            //这个地方，由于mybatis无法将下划线转成驼峰，所以在配置文件中进行配置驼峰，让它能够将下划线路由成驼峰标识
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOList);
        return paginationDTO;
    }

    public PaginationDTO list(Long userId, Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO();
        //根据传入的不同用户来进行相应分页的展示
        Integer totalCount = questionMapper.countByUserId(userId);
        paginationDTO.setPagination(totalCount, page, size);
        if (page < 1) {
            page = 1;
        }
        if (page > paginationDTO.getTotalPage()) {
            page = paginationDTO.getTotalPage();
        }
        //size*(page-1)
        Integer offset = size * (page - 1);
        //第一步：通过questionMapper.listByuserId(userId,offset,size)方法得到questions对象
        List<Question> questions = questionMapper.listByuserId(userId, offset, size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        //第二步：循环questions对象
        for (Question question : questions) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOList);
        return paginationDTO;
    }

    public QuestionDTO getById(Long id) {
        Question question = questionMapper.getById(id);
        if (question == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);
        User user = userMapper.findById(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if (question.getId() == null) {
            //创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            //create即insert
            questionMapper.create(question);
        } else {
            // 更新
            question.setGmtModified(question.getGmtCreate());
            questionMapper.update(question);
        }
    }

    /*增加问题的浏览数
     * @param id
     * 需要注意的一点是：数据库中的viewCount字段要初始化，否则代码一运行就会报空指针异常，这块儿曾经卡住2个小时
     */
    public void incView(Long questionId) {
        Question question = new Question();
        question.setId(questionId);
        question.setViewCount(1);
        questionMapper.incView(question);
    }
}