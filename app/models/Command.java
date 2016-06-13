package models;

import com.avaje.ebean.Model;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

/**
 * Created by Paul-Alexandre on 11/06/2016.
 */

@Entity
public class Command extends Model {

    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 55)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "MIN_TIME")
    private Integer minTime;
    @Column(name = "SENDING_TDATE")
    private Integer sendingTdate;
    @Column(name = "REAL_TDATE")
    private Integer realTdate;
    @Column(name = "FEE")
    private Float fee;
    @Column(name = "PRODUCT_ID_QUANTITY")
    private Integer productIdQuantity;
    @JoinColumn(name = "INSTANCE_ID", referencedColumnName = "ID")
    @ManyToOne
    private Instance instanceId;

    public static Finder<String, Command> find = new Finder<String, Command>(Command.class);

}
