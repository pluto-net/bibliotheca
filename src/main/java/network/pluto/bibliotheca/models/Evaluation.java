package network.pluto.bibliotheca.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Evaluation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long evaluationId;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "MEMBER_ID")
    private Member createdBy;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "ARTICLE_ID")
    private Article article;

    @Column
    private Double total;

    @Column
    private Double originality;

    @Column
    private Double contribution;

    @Column
    private Double analysis;

    @Column
    private Double expressiveness;

    @Column
    private String originalityComment;

    @Column
    private String contributionComment;

    @Column
    private String analysisComment;

    @Column
    private String expressivenessComment;

    @Column(nullable = false)
    private int vote;

    @OneToMany(mappedBy = "evaluation", fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();
}
