package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
public class AuthenticationService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordCryptographyProvider cryptographyProvider;

    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthEntity authenticateUser(final String username, final String password) throws AuthenticationFailedException{
        UserEntity userEntity = userDao.getUserByUsername(username);
        //check if the username exists in the users entity.
        if(userEntity == null){
            throw new AuthenticationFailedException("ATH-001", "This username does not exist");
        }

        //encrypt the password & generate the JWT token.
        final String encryptedPassword = cryptographyProvider.encrypt(password, userEntity.getSalt());
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);
        //check if the password provided matches!
        //if password matches, set the user details to user_auth entity.
        if(encryptedPassword.equals(userEntity.getPassword())){
            UserAuthEntity userAuthToken = new UserAuthEntity();

            final ZonedDateTime now = ZonedDateTime.now();
            final ZonedDateTime expiresAt = now.plusHours(8);

            userAuthToken.setUuid(userEntity.getUuid());
            userAuthToken.setUserId(userEntity.getId());
            userAuthToken.setAccessToken(jwtTokenProvider.generateToken(userEntity.getUuid(), now, expiresAt));
            userAuthToken.setLoginAt(now);
            userAuthToken.setExpiresAt(expiresAt);

            userDao.createAuthToken(userAuthToken);
            userDao.updateUser(userEntity);

            return userAuthToken;
        }
        else{
            throw new AuthenticationFailedException("ATH-002", "Password failed");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity signoutService(final String accesstoken) throws SignOutRestrictedException{
        UserAuthEntity userAuthEntity = userDao.getUserAuthByAccesstoken(accesstoken);
        // if the userAuthEntity is not present for which the accesstoken is passed as arguments, throw exception!
        if(userAuthEntity == null){
            throw new SignOutRestrictedException("SGR-001", "User is not Signed in");
        }
        // else update the logout time & return the user entity.
        else{
            UserEntity userEntity = userDao.getUserById(userAuthEntity.getUserId());

            ZonedDateTime now = ZonedDateTime.now();
            userAuthEntity.setLogoutAt(now);
            userDao.updateUserAuth(userAuthEntity);

            return userEntity;
        }
    }
}
