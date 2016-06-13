package models;

import com.avaje.ebean.Model;

import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * Created by Paul-Alexandre on 11/06/2016.
 */

@Entity
public class Solution extends Model {

    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 55)
    @Column(name = "ID")
    private String id;
    @Column(name = "FEE")
    private Float fee;
    @Column(name = "SENDING_DATE")
    private Integer sendingDate;
    @Column(name = "EVAL_SCORE")
    private Float evalScore;
    @JoinColumn(name = "INSTANCE_ID", referencedColumnName = "ID")
    @ManyToOne
    private Instance instanceId;

    public static Finder<String, Solution> find = new Finder<String,Solution>(Solution.class);

}