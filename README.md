# My Bear Blog 🐻

> A minimalist, server-side rendered blogging platform built with Spring Boot and Thymeleaf.
> Designed for speed, simplicity, and zero client-side JavaScript bloat.

![Java](https://img.shields.io/badge/Java-25-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.x-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-Green?style=for-the-badge&logo=thymeleaf&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)

## 📖 About

This project is a custom-built "Digital Garden" engine. It mimics the philosophy of [Bear Blog](https://bearblog.dev) by prioritizing content over complex layouts.

It is a **Monolith** application that renders HTML on the server. No React, no Vue, just pure HTML & CSS.

### ✨ Key Features

* **Minimalist Design:** Uses [Water.css](https://watercss.kognise.dev/) for a classless, beautiful look.
* **Markdown Support:** Write posts in Markdown (GitHub Flavored) with support for:
    * Tables
    * Task Lists `[ ]`
    * Strikethrough `~~text~~`
    * Auto-linking
* **System Dark Mode:** Automatically adapts to your OS theme (Light/Dark) without JavaScript.
* **Admin Dashboard:** Secure interface to Create, Edit, and Delete posts.
* **Draft System:** Save posts as drafts before publishing.
* **Tagging System:** Organize posts by topics (e.g., `#java`, `#minimalism`).
* **Search & Pagination:** Built-in search functionality and paginated lists.
* **RSS Feed:** Automatic `rss.xml` generation for subscribers.

## 🛠️ Tech Stack

* **Backend:** Java 25, Spring Boot 4
* **Template Engine:** Thymeleaf
* **Database:** PostgreSQL (Production) / H2 (Dev)
* **Security:** Spring Security (Form Login, BCrypt, CSRF)
* **Markdown Engine:** CommonMark
* **Frontend:** HTML5, CSS3 (Variables for Dark Mode), Water.css

## 🚀 Getting Started

### Prerequisites
* JDK 25 installed.
* Maven installed.
* PostgreSQL (optional, defaults to H2 in dev).

### Installation

1.  **Clone the repository**
    ```bash
    git clone [https://github.com/mgustimz/mgustimz-blog.git](https://github.com/mgustimz/mgustimz-blog.git)
    cd mgustimz-blog
    ```

2.  **Run the Application**
    ```bash
    mvn spring-boot:run
    ```

3.  **Access the App**
    * **Frontend:** `http://localhost:8080`
    * **Admin Panel:** `http://localhost:8080/admin`
    * **RSS Feed:** `http://localhost:8080/rss`

### Default Login
* **Username:** `admin`
* **Password:** `password`
* *(Note: Change these in `application.properties` or environment variables before deploying)*

## ⚙️ Configuration

The application uses `application.properties`.

**Local Development (H2 Database):**
No configuration needed. Just run it.

**Production (PostgreSQL):**
Set these Environment Variables on your host (Render, Railway, etc.):

| Variable | Description |
| :--- | :--- |
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://host:5432/dbname` |
| `SPRING_DATASOURCE_USERNAME` | DB User |
| `SPRING_DATASOURCE_PASSWORD` | DB Password |
| `ADMIN_USERNAME` | Your custom admin username |
| `ADMIN_PASSWORD` | Your custom admin password |

## 📂 Project Structure

```text
src/main/java/org/mgmz/blog
├── config/          # SecurityConfig (Spring Security)
├── controller/      # Admin, Blog, Sitemap controllers
├── entities/        # Post, Tag (JPA Entities)
├── repositories/    # Interfaces for DB access
├── services/        # Business logic (Markdown parsing, Search)
└── BearBlogApplication.java

src/main/resources
├── static/
│   ├── css/         # style.css (Dark mode logic)
│   └── images/      # Logos and assets
└── templates/       # Thymeleaf HTML files
    ├── admin/       # Dashboard & Editor
    ├── fragments.html # Shared Header/Footer
    └── blog.html, post.html, etc.
```

## 🤝 Contributing

1.  Fork the Project
2.  Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3.  Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4.  Push to the Branch (`git push origin feature/AmazingFeature`)
5.  Open a Pull Request

## 📝 License

Distributed under the MIT License. See `LICENSE` for more information.

---

*Built with ☕ and Spring Boot by mgustimz*