package network.pluto.bibliotheca.repositories.mag;

import network.pluto.bibliotheca.models.mag.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    List<Author> findByIdIn(List<Long> authorIds);

}