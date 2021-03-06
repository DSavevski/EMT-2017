package mk.ukim.finki.emt.web;

import mk.ukim.finki.emt.model.jpa.Book;
import mk.ukim.finki.emt.model.jpa.BookDetails;
import mk.ukim.finki.emt.model.jpa.Category;
import mk.ukim.finki.emt.service.QueryService;
import mk.ukim.finki.emt.service.StoreManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Riste Stojanov
 */
@Controller
public class AdminController {

  StoreManagementService storeManagementService;
  QueryService queryService;

  @Autowired
  public AdminController(StoreManagementService storeManagementService, QueryService queryService) {
    this.storeManagementService = storeManagementService;
    this.queryService = queryService;
  }

  @RequestMapping(value = {"/admin/category"}, method = RequestMethod.GET)
  public String addCategory(Model model) {
    model.addAttribute("pageFragment", "addCategory");
    return "index";
  }

  @RequestMapping(value = {"/admin/book"}, method = RequestMethod.GET)
  public String addProduct(Model model) {
    model.addAttribute("pageFragment", "addBook");
    return "index";
  }


  @RequestMapping(value = {"/admin/category"}, method = RequestMethod.POST)
  public String createCategory(Model model,
                               @RequestParam String categoryName) {
    Category category = storeManagementService.createTopLevelCategory(categoryName);
    return "redirect:/admin/category";
  }


  @RequestMapping(value = {"/admin/book"}, method = RequestMethod.POST)
  public String createProduct(HttpServletRequest request,
                              HttpServletResponse resp,
                              Model model,
                              @RequestParam String name,
                              @RequestParam Long categoryId,
                              @RequestParam String authors,
                              @RequestParam String isbn,
                              @RequestParam Double price,
                              @RequestParam String description,
                              MultipartFile picture) throws IOException, SQLException {

    Book product = storeManagementService.createBook(
      name,
      categoryId,
      authors.split(";"),
      isbn,
      price,
      description
    );
    storeManagementService.addBookPicture(product.id, picture.getBytes(), picture.getContentType());

    model.addAttribute("product", product);
    return "index";
  }


  @RequestMapping(value = {"/admin/book/{id}"}, method = RequestMethod.POST)
  public String updateProduct(@PathVariable Long id, Model model, @RequestParam String name,
                              @RequestParam String isbn, @RequestParam String[] authors,
                              @RequestParam String description, @RequestParam Double price){

    storeManagementService.updateBook(id,name,authors,isbn,description,price);

    BookDetails bookDetails = queryService.getBookDetailsById(id);

    model.addAttribute("bd", bookDetails);
    model.addAttribute("pageFragment","bookDetails");

    return "index";

  }


}
