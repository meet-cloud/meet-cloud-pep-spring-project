package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.entity.Message;
import com.example.entity.Account;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;
import java.util.List;
import java.util.Optional;


@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public Message createMessage(Message message) {
        // Validate message text
        if (message.getMessageText() == null || message.getMessageText().trim().isEmpty()) {
            throw new IllegalArgumentException("Message text cannot be blank.");
        }
        if (message.getMessageText().length() > 255) {
            throw new IllegalArgumentException("Message text cannot exceed 255 characters.");
        }

       
        // Validate postedBy (user must exist)
        if (message.getPostedBy() == null) {
            throw new IllegalArgumentException("postedBy cannot be null.");
        }

        Optional<Account> user = accountRepository.findById(message.getPostedBy());
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User does not exist.");
        }

        // Save message
        return messageRepository.save(message);
    }


    public List<Message> getAllMessages() {
        return messageRepository.findAll(); // ✅ Fetch all messages from DB
    }


    

}
