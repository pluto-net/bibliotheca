package network.pluto.bibliotheca.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@ToString(exclude = { "journal", "authors", "keywords", "fosList", "urls", "comments" })
@Getter
@Setter
@Entity
public class Paper {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "paperSequence")
    @SequenceGenerator(name = "paperSequence", sequenceName = "paper_sequence", allocationSize = 1)
    @Id
    private long id;

    @Column
    private String magId;

    @Type(type = "text")
    @Lob
    @Column(nullable = false)
    private String title;

    @Column
    private Integer year;

    @Column(name = "N_CITATION")
    private Integer citedCount;

    @Type(type = "text")
    @Lob
    @Column(name = "ABSTRACT")
    private String paperAbstract;

    @Column
    private String lang;

    @Type(type = "text")
    @Lob
    @Column
    private String doi;

    @Type(type = "text")
    @Lob
    @Column
    private String publisher;

    @Type(type = "text")
    @Lob
    @Column
    private String venue;

    @Column
    private String volume;

    @Column
    private String issue;

    @Column
    private String pageStart;

    @Column
    private String pageEnd;

    @ManyToOne
    @JoinColumn(name = "JOURNAL_ID")
    private Journal journal;

    @LazyCollection(LazyCollectionOption.EXTRA)
    @BatchSize(size = 10)
    @OneToMany(mappedBy = "paper")
    private List<PaperAuthor> authors = new ArrayList<>();

    @LazyCollection(LazyCollectionOption.EXTRA)
    @BatchSize(size = 10)
    @OneToMany(mappedBy = "paper")
    private List<PaperKeyword> keywords = new ArrayList<>();

    @LazyCollection(LazyCollectionOption.EXTRA)
    @BatchSize(size = 10)
    @ManyToMany
    @JoinTable(name = "REL_PAPER_FOS",
            joinColumns = @JoinColumn(name = "PAPER_ID"),
            inverseJoinColumns = @JoinColumn(name = "FOS_ID"))
    private List<Fos> fosList = new ArrayList<>();

    @LazyCollection(LazyCollectionOption.EXTRA)
    @BatchSize(size = 10)
    @OneToMany(mappedBy = "paper")
    private List<PaperUrl> urls = new ArrayList<>();

}
