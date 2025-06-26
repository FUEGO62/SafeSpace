package com.bytebuilder.data.repository;

import com.bytebuilder.data.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentRepository extends MongoRepository<Comment, String> {

}
