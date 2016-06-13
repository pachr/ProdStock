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
    private String id;
    @Column(name = "MIN_TIME")
    private Integer minTime;
    @Column(name = "SENDING_TDATE")
    private Integer sendingTdate;
    @Column(name = "FEE")
    private Float fee;
    @Column(name = "PRODUCT_ID_QUANTITY")
    private Integer productIdQuantity;
    @JoinColumn(name = "INSTANCE_ID", referencedColumnName = "ID")
    @ManyToOne
    private Instance instanceId;
    @OneToMany(mappedBy = "commandId")
    private List<Product> produitCollection;

    public static Finder<String, Command> find = new Finder<String, Command>(Command.class);

}
