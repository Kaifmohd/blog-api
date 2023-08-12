package com.kaifsam.blog.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kaifsam.blog.entities.Category;
import com.kaifsam.blog.entities.Post;
import com.kaifsam.blog.entities.User;

public interface PostRepo extends JpaRepository<Post, Integer> {

	List<Post> findByUser(User user);
	List<Post> findByCategory(Category category);
	
	
	
	
	List<Post> findByTitleContaining(String title);


	//Alterantive to the above
	@Query("select p from Post p where p.title like :key")
	List<Post> searchByTitle(@Param("key")String title);
}
