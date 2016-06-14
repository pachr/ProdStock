package models;

import com.avaje.ebean.Model;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

/**
 * Created by Paul-Alexandre on 13/06/2016.
 */

@Entity
public class ProductType extends Model{

    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 55)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "NAME")
    private String name;
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
    @OneToMany(mappedBy = "productTypeId")
    private List<Product> produitCollection;

    public static Finder<String, ProductType> find = new Finder<String, ProductType>(ProductType.class);

}
