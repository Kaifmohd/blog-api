package com.kaifsam.blog.services.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.kaifsam.blog.entities.Category;
import com.kaifsam.blog.entities.Post;
import com.kaifsam.blog.entities.User;
import com.kaifsam.blog.exceptions.ResourceNotFoundException;
import com.kaifsam.blog.payloads.PostDto;
import com.kaifsam.blog.payloads.PostResponse;
import com.kaifsam.blog.repositories.CategoryRepo;
import com.kaifsam.blog.repositories.PostRepo;
import com.kaifsam.blog.repositories.UserRepo;
import com.kaifsam.blog.services.PostService;

@Service
public class PostServiceImpl implements PostService {

	@Autowired
	private PostRepo postRepo;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private CategoryRepo categoryRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	
	
	@Override
	public PostDto createPost(PostDto postDto,Integer userId,Integer categoryId) {
		User user = this.userRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User", " User id ", userId));
		Category category = this.categoryRepo.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category", " Category id", categoryId));
		
		
		Post post = this.modelMapper.map(postDto, Post.class);
		post.setImageName("default.png");
		post.setAddedDate(new Date());
		post.setUser(user);
		post.setCategory(category);
		
		Post newpost = this.postRepo.save(post);
		return this.modelMapper.map(newpost, PostDto.class);
	}

	@Override
	public PostDto updatePost(PostDto postDto, Integer postId) {
	
		Post post = this.postRepo.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post", "post id", postId));
		
		post.setTitle(postDto.getTitle());
		post.setContent(postDto.getContent());
		post.setImageName(postDto.getImageName());
		Post newpost=this.postRepo.save(post);
		return this.modelMapper.map(newpost, PostDto.class);
	}

	@Override
	public void deletePost(Integer postId) {
		Post post = this.postRepo.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post", "post id", postId));
		this.postRepo.delete(post);
	}

	@Override
	public PostResponse getAllPost(Integer pageNumber,Integer pageSize,String sortBy,String sortDir) {
//dynamic pagesizing using path variable.
//		int pageSize=5;
//		int pageNumber=2;
		Sort sort=(sortDir.equalsIgnoreCase("asc")) ?  Sort.by(sortBy).ascending() :  Sort.by(sortBy).descending();
		Pageable p = PageRequest.of(pageNumber, pageSize,sort);
		Page<Post> pagePosts  = this.postRepo.findAll(p);
		List<Post> posts= pagePosts.getContent();
		List<PostDto> postDto = posts.stream().map((post)-> this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
		PostResponse postResponse = new PostResponse();
		postResponse.setContent(postDto);
		postResponse.setPageNumber(pagePosts.getNumber());
		postResponse.setPageSize(pagePosts.getSize());
		postResponse.setTotalElements(pagePosts.getTotalElements());
		postResponse.setTotalPages(pagePosts.getTotalPages());
		postResponse.setLastPage(pagePosts.isLast());
		return postResponse;
	}

	@Override
	public PostDto getPostById(Integer postId) {
		Post post = this.postRepo.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post", " Post Id ", postId));
		return this.modelMapper.map(post, PostDto.class);
	}

	@Override
	public List<PostDto> getPostsByCategory(Integer categoryId) {
		Category cat = this.categoryRepo.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category", " Category Id ", categoryId));
		List<Post> posts = this.postRepo.findByCategory(cat);
		List<PostDto> postsDto = posts.stream().map((post)-> this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
		return postsDto;
	}

	@Override
	public List<PostDto> getPostsByUser(Integer userId) {
		User user = this.userRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User", " User id ", userId));
		List<Post> posts = this.postRepo.findByUser(user);
		List<PostDto> postsDto = posts.stream().map((post)-> this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
		return postsDto;
	}

	@Override
	public List<PostDto> searchPosts(String keyword) {
		List<Post> posts = this.postRepo.findByTitleContaining(keyword);
		List<PostDto> postsDto = posts.stream().map((post)-> this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
		return postsDto;
	}
	
	@Override
	public List<PostDto> searchByTitle(String keyword) {
		List<Post> posts = this.postRepo.searchByTitle("%"+keyword+"%");
		List<PostDto> postsDto = posts.stream().map((post)-> this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
		return postsDto;
	}

}
