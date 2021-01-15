package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserAdminBusinessService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordCryptographyProvider cryptographyProvider;

    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity signup(UserEntity userEntity) throws SignUpRestrictedException{
        if(userDao.getUserByUsername(userEntity.getUserName())!=null){
            throw new
             SignUpRestrictedException("SGR-001", "Try any other Username, this Username has already been taken");
        }
        if(userDao.getUserByEmail(userEntity.getEmail())!=null){
            throw new
            SignUpRestrictedException("SGR-002", "This user has already been registered, try with any other emailId");
        }

        String password = userEntity.getPassword();
        if(password == null){
            password = "password@123";
        }

        String[] encryptedPassword = cryptographyProvider.encrypt(password);
        userEntity.setSalt(encryptedPassword[0]);
        userEntity.setPassword(encryptedPassword[1]);
        return userDao.createUser(userEntity);
    }
}
