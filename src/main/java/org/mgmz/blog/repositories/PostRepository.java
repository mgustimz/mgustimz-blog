package org.mgmz.blog.repositories;

import org.mgmz.blog.entities.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // Find a post by its URL slug (Standard naming convention automagically works)
    Optional<Post> findBySlug(String slug);

    // Check if a slug exists (useful when creating new posts to avoid duplicates)
    boolean existsBySlug(String slug);

    // Find all published posts, ordered by newest first
    List<Post> findByPublishedTrueOrderByCreatedAtDesc();

    // Find all posts (for Admin), ordered by newest first
    List<Post> findAllByOrderByCreatedAtDesc();

    List<Post> findByPublishedTrueAndTagsNameOrderByCreatedAtDesc(String tagName);

    List<Post> findByPublishedTrueAndTitleContainingIgnoreCaseOrPublishedTrueAndContentContainingIgnoreCaseOrderByCreatedAtDesc(String title, String content);

    Page<Post> findByPublishedTrue(Pageable pageable);
}
