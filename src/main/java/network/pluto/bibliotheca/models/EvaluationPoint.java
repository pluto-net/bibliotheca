package network.pluto.bibliotheca.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@ToString
@Getter
@Setter
@Embeddable
public class EvaluationPoint {

    @Column(nullable = false)
    private double total;

    @Column(nullable = false)
    private int originality;

    @Column(nullable = false)
    private int significance;

    @Column(nullable = false)
    private int validity;

    @Column(nullable = false)
    private int organization;

    @Column
    private String originalityComment;

    @Column
    private String significanceComment;

    @Column
    private String validityComment;

    @Column
    private String organizationComment;

    public void updateTotal() {
        int total = originality + significance + validity + organization;
        this.total = total / 4;
    }
}
