package org.mgmz.blog.services;

import org.mgmz.blog.entities.Post;
import org.mgmz.blog.entities.Tag;
import org.mgmz.blog.repositories.PostRepository;
import org.mgmz.blog.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class PostService {

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");

    private static final Pattern WHITESPACE = Pattern.compile("\\s");

    private final PostRepository postRepository;

    private final TagRepository tagRepository;

    @Autowired
    public PostService(PostRepository postRepository, TagRepository tagRepository) {
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Post> getPublishedPosts() {
        return postRepository.findByPublishedTrueOrderByCreatedAtDesc();
    }

    public List<Post> getPostsByTag(String tagName) {
        return postRepository.findByPublishedTrueAndTagsNameOrderByCreatedAtDesc(tagName);
    }

    public Optional<Post> getPostBySlug(String slug) {
        return postRepository.findBySlug(slug);
    }

    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    public List<Post> searchPosts(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return getPublishedPosts();
        }
        return postRepository.findByPublishedTrueAndTitleContainingIgnoreCaseOrPublishedTrueAndContentContainingIgnoreCaseOrderByCreatedAtDesc(keyword, keyword);
    }

    public Page<Post> getPublishedPostsPaginated(int pageNo, int pageSize) {
        // Create a PageRequest: Page Number, Size, and Sort Direction
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("createdAt").descending());
        return postRepository.findByPublishedTrue(pageable);
    }

    /**
     * MAIN SAVE METHOD: Handles Slugs + Tags
     */
    @Transactional
    public void     savePost(Post post, String tagString) {
        // 1. Generate Slug if missing
        if (post.getSlug() == null || post.getSlug().isEmpty()) {
            String generatedSlug = toSlug(post.getTitle());
            post.setSlug(ensureUniqueSlug(generatedSlug));
        }

        // 2. Parse Tags (String -> Entities)
        if (tagString != null && !tagString.isBlank()) {
            Set<Tag> newTags = new HashSet<>();
            String[] names = tagString.split(",");

            for (String name : names) {
                String cleanName = name.trim().toLowerCase();
                if (!cleanName.isEmpty()) {
                    // Find existing or create new
                    Tag tag = tagRepository.findByName(cleanName)
                            .orElseGet(() -> tagRepository.save(new Tag(cleanName)));
                    newTags.add(tag);
                }
            }
            post.setTags(newTags);
        } else {
            post.getTags().clear();
        }

        // 3. Save
        postRepository.save(post);
    }

    public String toSlug(String input) {
        if (input == null) throw new IllegalArgumentException("Input cannot be null");
        String noWhitespace = WHITESPACE.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(noWhitespace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH);
    }

    private String ensureUniqueSlug(String slug) {
        String originalSlug = slug;
        int counter = 1;
        while (postRepository.existsBySlug(slug)) {
            slug = originalSlug + "-" + counter;
            counter++;
        }
        return slug;
    }
}
