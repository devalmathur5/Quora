package com.upgrad.quora.service.business;

import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QuestionService {


    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthEntity createQuestion(final QuestionEntity questionEntity){

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthEntity auth
}
