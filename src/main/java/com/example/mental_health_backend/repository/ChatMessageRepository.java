package com.example.mental_health_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.mental_health_backend.entity.ChatMessage;
import com.example.mental_health_backend.entity.User;

import jakarta.transaction.Transactional;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByUserOrderByCreatedAtAsc(User user);

    // All chats of a user
    List<ChatMessage> findByUserId(Long userId);

    // Delete ALL chats of user
    @Modifying
    @Transactional
    void deleteByUserId(Long userId);

    // chat No.
    @Query(value = """
            SELECT MAX(CAST(SUBSTRING(conversation_id, 5) AS UNSIGNED))
            FROM chat_messages
            WHERE user_id = :userId
              AND conversation_id LIKE 'chat%'
            """, nativeQuery = true)
    Integer getLastChatNumberByUser(@Param("userId") Long userId);

}
