package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommonService {

    @Autowired
    private UserDao userDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity getUser(final String userUuid, final String userAuthToken) throws UserNotFoundException, AuthorizationFailedException {

        UserAuthEntity userAuthEntity = userDao.getUserAuthByAccesstoken(userAuthToken);
        if(userAuthEntity == null){
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        else if(userAuthEntity.getLogoutAt() != null){
            throw new AuthorizationFailedException("ATHR-002", "User is signed out. Sign in first to get user details");
        }

        UserEntity userEntity = userDao.getUserByUuid(userUuid);
        if(userEntity == null){
            throw new UserNotFoundException("USR-001", "User with entered uuid does not exist");
        }
        else{
            return userEntity;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity ifUserExist(final String userUuid) throws UserNotFoundException {
        UserEntity userEntity = userDao.getUserByUuid(userUuid);
        if(userEntity == null){
            throw new UserNotFoundException("USR-001", "User with entered uuid whose question details are to be seen doesn not exits");
        }
        return userEntity;
    }
}
