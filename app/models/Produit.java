package models;

import com.avaje.ebean.Model;

import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * Created by Paul-Alexandre on 11/06/2016.
 */

@Entity
public class Produit extends Model {

    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 55)
    @Column(name = "ID")
    private String id;
    @Column(name = "SET_UP_TIME")
    private Integer setUpTime;
    @Column(name = "PRODUCTION_TIME")
    private Integer productionTime;
    @Column(name = "HEIGHT")
    private Integer height;
    @Column(name = "WIDTH")
    private Integer width;
    @Column(name = "MAX_UNIT")
    private Integer maxUnit;
    @JoinColumn(name = "INSTANCE_ID", referencedColumnName = "ID")
    @ManyToOne
    private Instance instanceId;

    public static Finder<String, Produit> find = new Finder<String, Produit>(Produit.class);

}
