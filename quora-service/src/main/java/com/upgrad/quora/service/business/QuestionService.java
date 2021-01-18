package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

//nbNB+=
@Service
public class QuestionService {

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private UserDao userDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthEntity userAuthCheck(final String userAuthToken) throws AuthorizationFailedException {
        UserAuthEntity userAuthEntity = userDao.getUserAuthByAccesstoken(userAuthToken);
        if(userAuthEntity == null){
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        else if(userAuthEntity.getLogoutAt() != null){
            throw new AuthorizationFailedException("ATHR-002", "User is signed out. Sign in first to post a question");
        }
        return userAuthEntity;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity postQuestion(final QuestionEntity question){
        return questionDao.postAQuestion(question);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<QuestionEntity> getAllQuestions(){
        return questionDao.getAllQuestions();
    }

    @Transactional
    public QuestionEntity questionCheck(Integer userId, String questionUuid) throws AuthorizationFailedException, InvalidQuestionException {
        if(questionOwnerCheck(userId) && ifQuestionExist(questionUuid)){
            return questionDao.getQuestionById(questionUuid);
        }
        return null;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public boolean questionOwnerCheck(final Integer userId) throws AuthorizationFailedException {
        List<QuestionEntity> questionEntity = questionDao.getQuestionByUserId(userId);
        if(questionEntity!= null){
            return true;
        }
        throw new AuthorizationFailedException("ATHR-003", "Only the question owner can edit the question");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public boolean ifQuestionExist(final String questionId) throws InvalidQuestionException {
        QuestionEntity questionEntity = questionDao.getQuestionById(questionId);
        if(questionEntity!= null){
            return true;
        }
        throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity editQuestion(QuestionEntity question){
        return questionDao.editQuestion(question);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity deleteQuestion(final String userId, final String questionId) throws InvalidQuestionException, AuthorizationFailedException {
        //
        List<QuestionEntity> questionEntity = questionDao.getQuestionByUserId(Integer.parseInt(userId));
        if(ifQuestionExist(questionId)){
            if(questionEntity != null || isUserAnAdmin(userId)){
                QuestionEntity q = null;
                ArrayList<QuestionEntity> questionArray = new ArrayList<>();
                for(int i=0; i<questionEntity.size(); i++){
                    questionArray.add(questionEntity.get(i));
                    if(questionArray.get(i).getUuid().equals(questionId)){
                        q = questionArray.get(i);
                        break;
                    }
                }
                questionDao.deleteQuestion(q);
                return q;
            }
        }
        throw new AuthorizationFailedException("ATHR-003", "Only the question owner or admin can delete the question");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public boolean isUserAnAdmin(String userId){
        UserEntity user = userDao.getUserById(Integer.parseInt(userId));
        String role = user.getRole();
        if(role.equals("admin")){
            return true;
        }
        return false;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<QuestionEntity> getAllQuestionsById(Integer userId){
        return questionDao.getQuestionByUserId(userId);
    }
}
