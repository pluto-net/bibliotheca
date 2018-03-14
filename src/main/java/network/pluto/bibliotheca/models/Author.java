package network.pluto.bibliotheca.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import network.pluto.bibliotheca.enums.AuthorType;

import javax.persistence.*;

@ToString(exclude = { "member" })
@Getter
@Setter
@Entity
public class Author extends BaseEntity {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "authorSequence")
    @SequenceGenerator(name = "authorSequence", sequenceName = "author_sequence", allocationSize = 1)
    @Id
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AuthorType type;

    @Column(nullable = false)
    private String name;

    @Column
    private String institution;

    @Column
    private String major;

}
