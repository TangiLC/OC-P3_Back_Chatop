package com.chatop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chatop.model.Message;

public interface MessageRepository extends JpaRepository<Message, Integer> {
  //findById(ID id) is JPA native method
  //save() is JPA native method create/update
  //deleteById(ID id) is JPA native method

  List<Message> findByRentalId(Integer rentalId);

  List<Message> findByUserId(Integer userId);
}
