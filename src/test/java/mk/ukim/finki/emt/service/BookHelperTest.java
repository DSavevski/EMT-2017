package mk.ukim.finki.emt.service;

import mk.ukim.finki.emt.model.exceptions.CategoryInUseException;
import mk.ukim.finki.emt.model.jpa.Book;
import mk.ukim.finki.emt.model.jpa.BookDetails;
import mk.ukim.finki.emt.model.jpa.Category;
import mk.ukim.finki.emt.persistence.BookRepository;
import mk.ukim.finki.emt.persistence.CategoryRepository;
import mk.ukim.finki.emt.persistence.impl.SearchRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Riste Stojanov
 */
@RunWith(SpringRunner.class)
@SpringBootTest
//@ActiveProfiles("test")
public class BookHelperTest {

  public static final String AUTHOR_NAME = "Riste Stojanov";
  @Autowired
  BookServiceHelper serviceHelper;

  @Autowired
  SearchRepository searchRepository;

  @Autowired
  BookRepository bookRepository;

  @Autowired
  CategoryRepository categoryRepository;

  @Autowired
  BookServiceHelper bookServiceHelper;


  private Book book;
  private Category child;
  private Category nephew;

  @After
  public void clearTestEntities() {

  }

  @Test
  public void testCrud() throws CategoryInUseException {

    Category c = new Category();
    c.name = "base";
    Category base = categoryRepository.save(c);
    Book book = serviceHelper.createBook("Java essentials", base.id, new String[]{"Joshua Bloch"}, "123", 100d,"test");


//    List<Book> foundBooks = searchRepository.searchKeyword(Book.class, "java", "name", "category.name");


  }

  @Test
  public void testCreateBookDetails(){
    String [] authors = {"John","John2"};

    Book bookDetails = bookServiceHelper.createBook("Test1",1L,authors,"test-isbn",59D,"TESt desc");
    Assert.assertNotNull(bookDetails);
    Assert.assertEquals(bookDetails.name,"Test1");
    Assert.assertEquals(bookDetails.isbn,"test-isbn");
  }

  @Test
  public void testGetBookDetails(){
    BookDetails book = bookServiceHelper.getBookDetails(8L);
    Assert.assertNotNull(book);
  }

  @Test
  public void testUpdateBookDetails(){
    BookDetails bookDetails = bookServiceHelper.getBookDetails(8L);
    String [] authors = {"John","John2"};
    bookServiceHelper.updateBook(bookDetails.book.id,
            "update",authors,"update","update",50D);
    Assert.assertEquals(bookDetails.book.name,"update");
  }

}
