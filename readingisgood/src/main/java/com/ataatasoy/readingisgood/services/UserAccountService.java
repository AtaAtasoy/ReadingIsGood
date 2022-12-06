package com.ataatasoy.readingisgood.services;

import com.ataatasoy.readingisgood.models.UserAccount;
import com.ataatasoy.readingisgood.repository.UserAccountRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserAccountService {
    @Autowired
    private Validator validator;

    @Autowired
    private UserAccountRepository repository;

    public String addUserAccount(UserAccount userAccount){
        Set<ConstraintViolation<UserAccount>> violations = validator.validate(userAccount);

        if (!violations.isEmpty()){
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<UserAccount> constraintViolation : violations) {
                sb.append(constraintViolation.getMessage());
            }
            throw new ConstraintViolationException("Error occurred: " + sb.toString(), violations);
        }
        repository.save(userAccount);
        return "Account for " + userAccount.getName() + " Added!";
    }

}
