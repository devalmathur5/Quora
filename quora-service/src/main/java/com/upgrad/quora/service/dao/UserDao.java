package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class UserDao {
//nbNB+=
    @PersistenceContext
    private EntityManager entityManager;

    public UserEntity createUser(UserEntity userEntity){
        entityManager.persist(userEntity);
        return userEntity;
    }

    public UserEntity getUserByUsername(String username){
        try{
            return entityManager.createNamedQuery("userByUsername", UserEntity.class)
                    .setParameter("username", username).getSingleResult();
        }catch(NoResultException nre){
            return null;
        }
    }

    public UserEntity getUserByEmail(String email){
        try{
            return entityManager.createNamedQuery("userByEmail", UserEntity.class)
                    .setParameter("email", email).getSingleResult();
        }catch(NoResultException nre){
            return null;
        }
    }

    // this method checks if the username & password are a correct combination or not.
    public boolean authenticatePass(final String username, final String password){
        try{
            UserEntity user = entityManager.createNamedQuery("passwordByUsername", UserEntity.class)
                    .setParameter("username", username).getSingleResult();
            //if(user != null){
                return true;
            //}
            //return false;
        }catch(NoResultException nre){
            return false;
        }
    }

    public UserAuthEntity createAuthToken(final UserAuthEntity userAuthTokenEntity){
        entityManager.persist(userAuthTokenEntity);
        return userAuthTokenEntity;
    }

    public void updateUser(final UserEntity updateUserEntity){
        entityManager.merge(updateUserEntity);
    }

    public UserEntity getUserById(final Integer id){
        try{
            return entityManager.createNamedQuery("userById", UserEntity.class)
                    .setParameter("id", id).getSingleResult();
        }
        catch (NoResultException nre){
            return null;
        }
    }

    public UserAuthEntity getUserAuthByAccesstoken(final String accesstoken){
        try{
            return entityManager.createNamedQuery("userByAuthtoken", UserAuthEntity.class)
                    .setParameter("accesstoken", accesstoken).getSingleResult();
        }
        catch (NoResultException nre){
            return null;
        }
    }

    public void updateUserAuth(final UserAuthEntity updateUserAuthEntity){
        entityManager.merge(updateUserAuthEntity);
    }

    public UserEntity getUserByUuid(final String uuid){
        try{
            return entityManager.createNamedQuery("userByUuid", UserEntity.class)
                    .setParameter("uuid", uuid).getSingleResult();
        }
        catch (NoResultException nre){
            return null;
        }
    }

    public UserEntity deleteUser(final UserEntity user){
        entityManager.remove(user);
        return user;
    }
}
