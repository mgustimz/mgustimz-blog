package org.mgmz.blog.controller;

import org.mgmz.blog.entities.Post;
import org.mgmz.blog.services.MarkdownService;
import org.mgmz.blog.services.PostService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
public class RssController {

    private static final DateTimeFormatter RSS_DATE_FORMATTER = DateTimeFormatter.RFC_1123_DATE_TIME;

    private final PostService postService;

    private final MarkdownService markdownService; // 2. Add this field

    // 3. Update Constructor to include MarkdownService
    public RssController(PostService postService, MarkdownService markdownService) {
        this.postService = postService;
        this.markdownService = markdownService;
    }

    @GetMapping(value = "/rss", produces = "application/xml")
    public String rssFeed() {
        List<Post> posts = postService.getPublishedPosts();

        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
        xml.append("<rss version=\"2.0\">");
        xml.append("<channel>");
        xml.append("<title>mgustimz</title>");
        xml.append("<link>http://localhost:8080</link>");
        xml.append("<description>A minimalist blog built with Spring Boot</description>");

        for (Post post : posts) {
            xml.append("<item>");
            xml.append("<title>").append(escapeXml(post.getTitle())).append("</title>");
            xml.append("<link>http://localhost:8080/").append(post.getSlug()).append("</link>");
            xml.append("<guid isPermaLink=\"true\">http://localhost:8080/").append(post.getSlug()).append("</guid>");

            String pubDate = post.getCreatedAt().atZone(ZoneId.systemDefault()).format(RSS_DATE_FORMATTER);
            xml.append("<pubDate>").append(pubDate).append("</pubDate>");

            // 4. RENDER TO HTML HERE
            // This turns "**Bold**" into "<p><strong>Bold</strong></p>"
            String htmlContent = markdownService.render(post.getContent());

            // 5. Put the nice HTML into the description
            xml.append("<description>").append(escapeXml(htmlContent)).append("</description>");

            xml.append("</item>");
        }

        xml.append("</channel>");
        xml.append("</rss>");

        return xml.toString();
    }

    private String escapeXml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}
