package com.kaifsam.blog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kaifsam.blog.entities.Category;

public interface CategoryRepo extends JpaRepository<Category, Integer>{

}
