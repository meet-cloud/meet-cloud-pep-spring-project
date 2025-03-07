package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.entity.Account;
import com.example.repository.AccountRepository;
import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public Account registerAccount(Account account) {
        // Validate username is not blank
        if (account.getUsername() == null || account.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be blank.");
        }

        // Validate password length
        if (account.getPassword() == null || account.getPassword().length() < 4) {
            throw new IllegalArgumentException("Password must be at least 4 characters long.");
        }

        // Check for duplicate username
        if (accountRepository.findByUsername(account.getUsername()).isPresent()) {
            throw new IllegalStateException("Username already exists");
        }


        // Save the account
        return accountRepository.save(account);
    }


    @Transactional(readOnly = true)
    public Account authenticate(String username, String password) {
        Optional<Account> optionalAccount = accountRepository.findByUsername(username);

        // Verify user exists and password matches
        if (optionalAccount.isPresent() && optionalAccount.get().getPassword().equals(password)) {
            return optionalAccount.get();
        }
        return null;
    }
}


