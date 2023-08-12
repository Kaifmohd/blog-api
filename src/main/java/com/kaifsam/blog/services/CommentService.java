package com.kaifsam.blog.services;

import com.kaifsam.blog.payloads.CommentDto;

public interface CommentService {

	CommentDto createComment(CommentDto comemntDto,Integer postId);
	
	void deleteComment(Integer commentId);
	
}
