package org.mgmz.blog.controller;

import org.mgmz.blog.entities.Post;
import org.mgmz.blog.entities.Tag;
import org.mgmz.blog.repositories.PostRepository;
import org.mgmz.blog.services.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final PostService postService;
    private final PostRepository postRepository;

    public AdminController(PostService postService, PostRepository postRepository) {
        this.postService = postService;
        this.postRepository = postRepository;
    }

    @GetMapping
    public String adminDashboard(Model model) {
        model.addAttribute("posts", postService.getAllPosts());
        return "admin/dashboard";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("post", new Post());
        model.addAttribute("tagString", "");
        return "admin/edit";
    }

    // UPDATED: Now accepts tagString
    @PostMapping("/save")
    public String savePost(@ModelAttribute Post post, @RequestParam("tagString") String tagString) {
        postService.savePost(post, tagString);
        return "redirect:/admin";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Post> postOpt = postService.getPostById(id); // Use Service method
        if (postOpt.isPresent()) {
            Post post = postOpt.get();
            // Convert tags to "java, spring" string for the input box
            String tagString = post.getTags().stream()
                    .map(Tag::getName)
                    .collect(Collectors.joining(", "));

            model.addAttribute("post", post);
            model.addAttribute("tagString", tagString);
            return "admin/edit";
        }
        return "redirect:/admin";
    }

    @PostMapping("/delete/{id}")
    public String deletePost(@PathVariable Long id) {
        postRepository.deleteById(id);
        return "redirect:/admin";
    }
}
