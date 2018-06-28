package network.pluto.bibliotheca.repositories.mag;

import network.pluto.bibliotheca.dtos.AffiliationDto;
import network.pluto.bibliotheca.dtos.AuthorDto;
import network.pluto.bibliotheca.models.mag.Author;
import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AuthorRepositoryImpl extends QueryDslRepositorySupport implements AuthorRepositoryCustom {

    public AuthorRepositoryImpl() {
        super(Author.class);
    }

    @Override
    public Map<Long, List<AuthorDto>> getAuthorsByPaperIdIn(List<Long> paperIds) {
        if (paperIds.isEmpty()) {
            return new HashMap<>();
        }

        String sql = "select t.paper_id, t.author_sequence_number, a.id as author_id, a.display_name as author_name, ah.hindex, af.id as affiliation_id, af.display_name as affiliation_name from" +
                " (select paper_id, author_id, affiliation_id, author_sequence_number, row_number() over (partition by paper_id order by author_sequence_number) as row from mcsa.rel_paper_author_affiliation where paper_id in (:paperIds)) t" +
                " join mcsa.author a on t.author_id = a.id" +
                " left join mcsa.author_hindex ah on t.author_id = ah.author_id" +
                " left join mcsa.affiliation af on t.affiliation_id = af.id" +
                " where t.row < 11";

        List<Object[]> results = getEntityManager()
                .createNativeQuery(sql)
                .setParameter("paperIds", paperIds)
                .getResultList();

        return results
                .stream()
                .map(t -> {
                    AuthorDto authorDto = new AuthorDto();
                    authorDto.setPaperId(((BigInteger) t[0]).longValue());
                    authorDto.setOrder((Integer) t[1]);
                    authorDto.setId(((BigInteger) t[2]).longValue());
                    authorDto.setName((String) t[3]);

                    if (t[4] != null) {
                        authorDto.setHIndex((Integer) t[4]);
                    }

                    if (t[5] != null) {
                        AffiliationDto affiliationDto = new AffiliationDto();
                        affiliationDto.setId(((BigInteger) t[5]).longValue());
                        affiliationDto.setName((String) t[6]);
                        authorDto.setAffiliation(affiliationDto);
                    }

                    return authorDto;
                })
                .collect(Collectors.collectingAndThen(
                        Collectors.groupingBy(AuthorDto::getPaperId),
                        map -> {
                            map.values().forEach(list -> list.sort(Comparator.comparing(AuthorDto::getOrder)));
                            return map;
                        }));
    }

    @Override
    public List<Author> findCoAuthors(long authorId) {
        String sql =
                "select a.*\n" +
                        "from\n" +
                        "  (\n" +
                        "    select r1.author_id as id\n" +
                        "    from\n" +
                        "      mcsa.rel_paper_author_affiliation r1\n" +
                        "    where\n" +
                        "      r1.paper_id in (\n" +
                        "        select r2.paper_id\n" +
                        "        from\n" +
                        "          mcsa.rel_paper_author_affiliation r2\n" +
                        "          join mcsa.paper p on r2.paper_id = p.id\n" +
                        "        where\n" +
                        "          r2.author_id = :authorId\n" +
                        "          and r2.author_sequence_number < 10\n" +
                        "        order by p.citation_count desc\n" +
                        "        limit 10\n" +
                        "      )\n" +
                        "      and r1.author_id <> :authorId\n" +
                        "      and r1.author_sequence_number < 10\n" +
                        "    group by r1.author_id\n" +
                        "    order by count(r1.author_id) desc\n" +
                        "    limit 5\n" +
                        "  ) t\n" +
                        "  join mcsa.author a on t.id = a.id;";

        List<Author> results = getEntityManager()
                .createNativeQuery(sql, Author.class)
                .setParameter("authorId", authorId)
                .getResultList();

        return results;
    }

}