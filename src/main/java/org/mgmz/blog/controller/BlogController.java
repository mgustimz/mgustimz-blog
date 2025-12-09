package org.mgmz.blog.controller;

import org.mgmz.blog.entities.Post;
import org.mgmz.blog.services.MarkdownService;
import org.mgmz.blog.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class BlogController {

    private final PostService postService;
    private final MarkdownService markdownService; // Added this

    @Autowired
    public BlogController(PostService postService, MarkdownService markdownService) {
        this.postService = postService;
        this.markdownService = markdownService;
    }

    @GetMapping("/")
    public String home() {
        return "home"; // Looks for templates/home.html
    }

    // UPDATED: /blog endpoint now handles pagination
    @GetMapping("/blog")
    public String blog(@RequestParam(value = "page", defaultValue = "0") int page, Model model) {
        int pageSize = 5; // How many posts per page?

        Page<Post> pagePost = postService.getPublishedPostsPaginated(page, pageSize);

        // "posts" is just the list of 5 items for this page
        model.addAttribute("posts", pagePost.getContent());

        // Metadata for the buttons
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pagePost.getTotalPages());

        return "blog";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact"; // Looks for templates/contact.html
    }

    @GetMapping("/about")
    public String about() {
        return "about"; // Looks for templates/about.html
    }

    @GetMapping("/tag/{tagName}")
    public String postsByTag(@PathVariable String tagName, Model model) {
        model.addAttribute("posts", postService.getPostsByTag(tagName));
        model.addAttribute("tagName", tagName); // Optional: if you want to show "Showing posts for #java" in UI
        return "blog";
    }

    @GetMapping("/search")
    public String search(@RequestParam(value = "q", required = false) String query, Model model) {
        List<Post> searchResults = postService.searchPosts(query);
        model.addAttribute("posts", searchResults);
        model.addAttribute("query", query);
        return "blog";
    }

    @GetMapping("/{slug}")
    public String viewPost(@PathVariable String slug, Model model) {
        if (slug.equalsIgnoreCase("favicon.ico") || slug.equalsIgnoreCase("robots.txt")) {
            return "forward:/";
        }

        Post post = postService.getPostBySlug(slug).orElse(null);
        if (post == null) return "redirect:/blog"; // Redirect to blog index if not found

        String htmlContent = markdownService.render(post.getContent());
        model.addAttribute("post", post);
        model.addAttribute("htmlContent", htmlContent);

        return "post";
    }
}
