package network.pluto.bibliotheca.repositories.mag;

import network.pluto.bibliotheca.models.mag.Paper;
import network.pluto.bibliotheca.models.mag.PaperAuthorAffiliation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaperAuthorAffiliationRepository extends JpaRepository<PaperAuthorAffiliation, PaperAuthorAffiliation.PaperAuthorAffiliationId> {

    @Query("select r.paper from PaperAuthorAffiliation r join r.paper where r.id.authorId = :authorId and not r.id.paperId = :paperId order by r.authorSequenceNumber asc, r.paper.citationCount desc")
    List<Paper> getAuthorMainPapers(@Param("paperId") long paperId, @Param("authorId") long authorId, Pageable pageable);

}
