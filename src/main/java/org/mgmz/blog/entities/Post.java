package org.mgmz.blog.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "posts", indexes = {
        // Index the slug because we will look up posts by URL (e.g. /blog/my-first-post)
        @Index(name = "idx_post_slug", columnList = "slug", unique = true)
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    // The URL-friendly version of the title (e.g., "My First Post" -> "my-first-post")
    @Column(nullable = false, unique = true)
    private String slug;

    // "TEXT" is crucial for Postgres. Without this, it might default to
    // VARCHAR(255), which is too short for a blog post.
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    // Short description for the homepage list (optional)
    @Column(length = 500)
    private String summary;

    // NEW: Relationship to Tags
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "post_tags",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    // Status flag: false = Draft, true = Published
    @Column(nullable = false)
    private boolean published = false;

    // Automatically set when the record is created
    @CreationTimestamp
    private LocalDateTime createdAt;

    // Automatically updated whenever you edit the post
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // You might want to manually set a publishing date separate from creation
    private LocalDateTime publishedAt;

    /**
     * Best Practice: A semantic creator method.
     * This enforces that a Post MUST have a title, content, and slug to exist.
     */
    public static Post create(String title, String content, String slug) {
        // 1. Validation (The Builder pattern often skips this!)
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Post title cannot be empty");
        }
        if (slug == null || slug.isBlank()) {
            throw new IllegalArgumentException("Post slug cannot be empty");
        }

        // 2. Creation
        Post post = new Post();
        post.title = title;
        post.content = content;
        post.slug = slug;
        post.published = false; // Default to draft
        return post;
    }

    // --- BUSINESS LOGIC METHODS ---
    // Instead of just setting fields, we can have meaningful methods

    public void publish() {
        this.published = true;
        this.publishedAt = LocalDateTime.now();
    }

    public void unpublish() {
        this.published = false;
        this.publishedAt = null;
    }

    // Helper to add a tag
    public void addTag(Tag tag) {
        this.tags.add(tag);
        tag.getPosts().add(this);
    }

    public void removeTag(Tag tag) {
        this.tags.remove(tag);
        tag.getPosts().remove(this);
    }

    /**
     * Calculates reading time based on 200 words per minute.
     *
     * @Transient means "don't try to save this column to the database"
     */
    @Transient
    public String getReadingTime() {
        if (content == null || content.isEmpty()) {
            return "1 min read";
        }

        // Split by whitespace to count words
        int words = content.split("\\s+").length;

        // Calculate minutes (rounding up)
        int minutes = (int) Math.ceil((double) words / 200);

        return minutes + " min read";
    }
}
