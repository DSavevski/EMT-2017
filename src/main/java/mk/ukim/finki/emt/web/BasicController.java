package mk.ukim.finki.emt.web;

import mk.ukim.finki.emt.model.jpa.Book;
import mk.ukim.finki.emt.model.jpa.BookDetails;
import mk.ukim.finki.emt.model.jpa.BookPicture;
import mk.ukim.finki.emt.service.QueryService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Riste Stojanov
 */
@Controller
public class BasicController {


  QueryService queryService;

  @Autowired
  public BasicController(QueryService queryService) {
    this.queryService = queryService;
  }

  @RequestMapping(value = {"/index", "/"}, method = RequestMethod.GET)
  public String index(Model model) {

    System.out.println("TEST");
    Page<Book> page = queryService.getBooksInCategory(1L,1,2);
    System.out.println("==================" + page);
    model.addAttribute("products", queryService.getBooksInCategory(1L,0, 20));


    return "index2";
  }

  @RequestMapping(value = {"/category/{categoryId}"}, method = RequestMethod.GET)
  public String categoryProducts(
    @PathVariable Long categoryId,
    Model model
  ) {
    Page<Book> page = queryService.getBooksInCategory(
      categoryId, 0, 20
    );

    model.addAttribute("products", page);

    return "index";
  }

  @RequestMapping(value = {"/search"}, method = RequestMethod.GET)
  public String search(
    @PathVariable Long categoryId,
    @RequestParam String query,
    Model model
  ) {
    List<Book> page = queryService.searchBook(query);

    model.addAttribute("products", page);

    return "index";
  }
  @RequestMapping(value = {"/book/{id}"}, method = RequestMethod.GET)
  public String bookDetails(@PathVariable Long id, Model model){
    BookDetails bookDetails = queryService.getBookDetailsById(id);
    model.addAttribute("bd", bookDetails);
    model.addAttribute("pageFragment","bookDetails");
    return "index";
  }

  @RequestMapping(value = {"/book/{id}/picture"}, method = RequestMethod.GET)
  @ResponseBody
  public void index(@PathVariable Long id, HttpServletResponse response) throws IOException, SQLException {
    OutputStream out = response.getOutputStream();
    BookPicture bookPicture = queryService.getByBookId(id);

    String contentDisposition = String.format("inline;filename=\"%s\"",
      bookPicture.picture.fileName + ".png?productId=" + id);
    response.setHeader("Content-Disposition", contentDisposition);
    response.setContentType("image/png");
    response.setContentLength((int) bookPicture.picture.size);
    IOUtils.copy(bookPicture.picture.data.getBinaryStream(), out);
    out.flush();
    out.close();
  }


}
