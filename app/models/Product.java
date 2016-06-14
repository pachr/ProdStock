package models;

import com.avaje.ebean.Model;

import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * Created by Paul-Alexandre on 11/06/2016.
 */

@Entity
public class Product extends Model {

    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 55)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "NAME")
    private String name;
    @JoinColumn(name = "PRODUCT_TYPE_ID", referencedColumnName = "ID")
    @ManyToOne
    private ProductType productTypeId;
    @Column(name = "START_PRODUCTION")
    private String startProduction;
    @JoinColumn(name = "COMMAND_ID", referencedColumnName = "ID")
    @ManyToOne
    private Command commandId;
    @JoinColumn(name = "PRODUCT_LINE_ID", referencedColumnName = "ID")
    @ManyToOne
    private ProductLine productLineId;
    @JoinColumn(name = "BOX_ID", referencedColumnName = "ID")
    @ManyToOne
    private Box boxId;
    @JoinColumn(name = "INSTANCE_ID", referencedColumnName = "ID")
    @ManyToOne
    private Instance instanceId;

    public static Finder<String, Product> find = new Finder<String, Product>(Product.class);

}