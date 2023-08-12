package com.kaifsam.blog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kaifsam.blog.entities.Comment;

public interface CommentRepo extends JpaRepository<Comment, Integer>{

}
