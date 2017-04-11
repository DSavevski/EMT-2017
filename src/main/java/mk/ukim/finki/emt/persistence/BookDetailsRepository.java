package mk.ukim.finki.emt.persistence;

import mk.ukim.finki.emt.model.jpa.BookDetails;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Dragan on 4/10/17.
 */
public interface BookDetailsRepository extends CrudRepository<BookDetails,Long> {
    BookDetails findBookDetailsByBook_Id(Long id);
}
