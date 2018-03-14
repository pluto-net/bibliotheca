package network.pluto.bibliotheca.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import network.pluto.bibliotheca.enums.ArticleSource;
import network.pluto.bibliotheca.enums.ArticleType;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ToString(exclude = { "createdBy", "authors" })
@Getter
@Setter
@Entity
public class Article extends BaseEntity {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "articleSequence")
    @SequenceGenerator(name = "articleSequence", sequenceName = "article_sequence", allocationSize = 1)
    @Id
    private long id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "MEMBER_ID")
    private Member createdBy;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ArticleType type;

    @LazyCollection(LazyCollectionOption.EXTRA)
    @BatchSize(size = 10)
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "REL_ARTICLE_AUTHOR",
            joinColumns = @JoinColumn(name = "ARTICLE_ID"),
            inverseJoinColumns = @JoinColumn(name = "AUTHOR_ID"))
    @OrderColumn(name = "AUTHOR_ORDER")
    private List<Author> authors = new ArrayList<>();

    @Column(nullable = false)
    private String title;

    @Type(type = "text")
    @Lob
    @Column
    private String summary;

    @Column
    private String link;

    @Column
    @Enumerated(EnumType.STRING)
    private ArticleSource source;

    @Type(type = "text")
    @Lob
    @Column
    private String note;

    @Column
    @Embedded
    private ArticlePoint point;

    @Column
    private LocalDateTime articlePublishedAt;

    @Column
    private LocalDateTime articleUpdatedAt;

}
