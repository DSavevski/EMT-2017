package mk.ukim.finki.emt.service.impl;

import mk.ukim.finki.emt.model.jpa.*;
import mk.ukim.finki.emt.persistence.*;
import mk.ukim.finki.emt.service.BookServiceHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Riste Stojanov
 */
@Service
public class BookHelperImpl implements BookServiceHelper {

  /**
   * TODO: move this into book details helper
   */
  @Autowired
  private BookPictureRepository bookPictureRepository;
  private CategoryRepository categoryRepository;
  private BookRepository bookRepository;
  private AuthorsRepository authorsRepository;
  private BookDetailsRepository bookDetailsRepository;


  @Autowired
  public BookHelperImpl(
    CategoryRepository categoryRepository,
    BookRepository bookRepository,
    AuthorsRepository authorsRepository,
    BookDetailsRepository bookDetailsRepository
  ) {
    this.categoryRepository = categoryRepository;
    this.bookRepository = bookRepository;
    this.authorsRepository = authorsRepository;
    this.bookDetailsRepository = bookDetailsRepository;
  }

  @Override
  public List<Book> getBooksInCategory(Long categoryId) {
    return null;
  }

  @Override
  public BookDetails getBookDetails(Long bookId) {
    return bookDetailsRepository.findBookDetailsByBook_Id(bookId);
  }

  @Override
  public Book createBook(String name, Long categoryId, String[] authors, String isbn, Double price,String description) {
    Book book = new Book();
    book.name = name;
    book.isbn = isbn;
    book.price = price;
    book.category = categoryRepository.findOne(categoryId);
    for (String authorName : authors) {
      Author author = getOrCreateAuthor(authorName);
      book.authors.add(author);
    }
    // avoid transient exception
    Book savedBook = bookRepository.save(book);

    BookDetails bookDetails = new BookDetails();
    bookDetails.description = description;
    bookDetails.book = savedBook;
    //bookDetailsRepository.save(bookDetails);
    //return bookRepository.save(book);
      bookDetailsRepository.save(bookDetails);
    return savedBook;
  }


  @Override
  public Book updateBook(Long bookId, String name, String[] authors, String isbn,String description, Double price) {
    Book book = bookRepository.findOne(bookId);
    book.name = name;
    book.authors = new ArrayList<>();
    for (String authorName : authors) {
      Author author = getOrCreateAuthor(authorName);
      book.authors.add(author);
    }
    book.isbn = isbn;
    book.price = price;
    bookRepository.save(book);

    BookDetails bookDetails = bookDetailsRepository.findBookDetailsByBook_Id(bookId);
    bookDetails.book = book;
    bookDetails.description = description;
    bookDetailsRepository.save(bookDetails);

    return book;
  }

  @Override
  public Book updateBookPrice(Long bookId, Double price) {
    return null;
  }

  @Override
  public Book updateBookCategory(Long bookId, Long newCategoryId) {
    return null;
  }

  @Override
  public BookPicture addBookPicture(Long bookId, byte[] bytes, String contentType) throws SQLException {
    BookPicture bookPicture = new BookPicture();
    bookPicture.book = bookRepository.findOne(bookId);
    FileEmbeddable picture = new FileEmbeddable();
    picture.contentType = contentType;
    picture.data = new SerialBlob(bytes);
    picture.size = bytes.length;
    picture.fileName = bookPicture.book.name;
    bookPicture.picture = picture;
    return bookPictureRepository.save(bookPicture);
  }


  private Author getOrCreateAuthor(String authorName) {
    Author author = authorsRepository.findByNameAndLastName(authorName);
    if (author == null) {
      author = new Author();
      author.nameAndLastName = authorName;
      author = authorsRepository.save(author);
    }
    return author;
  }
}
