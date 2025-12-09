package org.mgmz.blog.controller;

import org.mgmz.blog.entities.Post;
import org.mgmz.blog.services.PostService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
public class SitemapController {

    private static final DateTimeFormatter SITEMAP_DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;

    private final PostService postService;

    public SitemapController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping(value = "/sitemap.xml", produces = "application/xml")
    public String sitemap() {
        List<Post> posts = postService.getPublishedPosts();

        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        xml.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">");

        // Static Pages
        addUrl(xml, "http://localhost:8080/", "1.0");
        addUrl(xml, "http://localhost:8080/blog", "0.9");
        addUrl(xml, "http://localhost:8080/about", "0.8");
        addUrl(xml, "http://localhost:8080/contact", "0.8");

        // Dynamic Posts
        for (Post post : posts) {
            xml.append("<url>");
            xml.append("<loc>http://localhost:8080/").append(post.getSlug()).append("</loc>");
            if (post.getUpdatedAt() != null) {
                xml.append("<lastmod>").append(post.getUpdatedAt().format(SITEMAP_DATE_FORMAT)).append("</lastmod>");
            }
            xml.append("<changefreq>monthly</changefreq>");
            xml.append("</url>");
        }

        xml.append("</urlset>");
        return xml.toString();
    }

    private void addUrl(StringBuilder xml, String loc, String priority) {
        xml.append("<url>");
        xml.append("<loc>").append(loc).append("</loc>");
        xml.append("<changefreq>weekly</changefreq>");
        xml.append("<priority>").append(priority).append("</priority>");
        xml.append("</url>");
    }
}
