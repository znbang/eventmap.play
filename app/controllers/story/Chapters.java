package controllers.story;

import controllers.Controller;
import controllers.RequireLogin;
import models.story.Book;
import models.story.Chapter;
import play.mvc.With;

import java.util.List;

@With(RequireLogin.class)
public class Chapters extends Controller {
    public static void index(String bookId) {
        int page = params.get("page", Integer.class);
        int size = params.get("size", Integer.class);
        String userId = getCurrentUser().getId();
        Book book = Book.findById(bookId);
        if (book == null || !book.userId.equals(userId)) {
            notFound();
        }
        long totalPage = getTotalPage(Chapter.countByBookId(bookId), page, size);
        if (page < 1 || page > totalPage) {
            notFound();
        }
        List<Chapter> chapters = Chapter.listByBookId(bookId, page, size);
        render(book, chapters, totalPage, page, size);
    }

    public static void show(String bookId, int page) {
        String userId = getCurrentUser().getId();
        Book book = Book.findById(bookId);
        if (book == null || !book.userId.equals(userId)) {
            notFound("Book not found: " + bookId);
        }
        long totalPage = Chapter.countByBookId(bookId);
        if (page < 1 || page > totalPage) {
            notFound("Page not found: " + page);
        }
        Chapter chapter = Chapter.findByBookIdAndPage(bookId, page);
        if (chapter == null) {
            notFound("Chapter not found: " + page);
        }
        render(book, chapter, totalPage, page, 1);
    }
}
