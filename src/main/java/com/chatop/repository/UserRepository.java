package com.chatop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chatop.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
  //findById(ID id) is JPA native method
  //save() is JPA native method create/update
  //deleteById(ID id) is JPA native method

  User findByEmail(String email);
}
