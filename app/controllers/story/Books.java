package controllers.story;

import controllers.Controller;
import controllers.RequireLogin;
import models.story.Book;
import models.story.Chapter;
import play.mvc.With;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

@With(RequireLogin.class)
public class Books extends Controller {
    public static void index() {
        int page = params.get("page", Integer.class);
        int size = params.get("size", Integer.class);
        String q = params.get("q", String.class);
        q = q == null ? "" : q.trim();
        String userId = getCurrentUser().getId();
        long totalPage = q.isEmpty() ? getTotalPage(Book.countByUserId(userId), page, size) : getTotalPage(Book.countByUserIdForSearch(userId, q), page, size);
        if (page < 1 || page > totalPage) {
            notFound("Page not found: " + page);
        }
        List<Book> books = q.isEmpty() ?  Book.listByUserId(userId, page, size) : Book.listByUserIdForSearch(userId, q, page, size);
        render(books, totalPage, page, size);
    }

    public static void edit(String id) {
        render();
    }

    public static void form(String id) {
        String userId = getCurrentUser().getId();
        Book book = id == null ? new Book() : Book.findByUserId(id, userId);
        if (book == null) {
            notFound("Book not found: " + id);
        }
        render("@form", id, book);
    }

    public static void save(String id, Book book) {
        checkAuthenticity();
        book.trim();

        validation.required("book.title", book.title).message("Books.form.title.required");
        validation.required("book.author", book.author).message("Books.form.author.required");
        validation.required("book.url", book.url).message("Books.form.url.required");
        validation.url("book.url", book.url).message("Books.form.url.invalid");

        if (id == null) {
            if (!validation.hasError("book.url") && Book.existsByUrl(book.url)) {
                validation.addError("book.url", "Books.form.url.exists");
            }

            if (!validation.hasError("book.title") && !validation.hasError("book.author") && Book.existsByTitleAndAuthor(book.title, book.author)) {
                validation.addError("book.title", "Books.form.title.exists");
                validation.addError("book.author", "Books.form.author.exists");
            }
        }

        if (validation.hasErrors()) {
            render("@form", id, book);
        }

        String userId = getCurrentUser().getId();
        Book model = id == null ? new Book() : Book.findByUserId(id, userId);
        if (model == null) {
            notFound("Book not found: " + id);
        }
        model.userId = userId;
        model.copyFrom(book);
        model.save();

        index();
    }

    public static void delete(String id) {
        checkAuthenticity();

        String userId = getCurrentUser().getId();
        Book book = Book.findByUserId(id, userId);
        if (book == null) {
            notFound("Book not found: " + id);
        }
        Chapter.delete("bookId=?1", id);
        book.delete();

        index();
    }

    public static void download(String id) {
        String userId = getCurrentUser().getId();
        Book book = Book.findByUserId(id, userId);
        if (book == null) {
            notFound("Book not found: " + id);
        }
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        PrintWriter out = new PrintWriter(new OutputStreamWriter(buf, StandardCharsets.UTF_8));
        for (Chapter chapter : Chapter.listByBookId(id)) {
            out.println(chapter.title);
            out.println();
            out.println(chapter.body);
            out.println();
        }
        out.flush();

        renderBinary(new ByteArrayInputStream(buf.toByteArray()), book.title + ".txt");
    }
}
