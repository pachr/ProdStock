package models;

import com.avaje.ebean.Model;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

/**
 * Created by Paul-Alexandre on 11/06/2016.
 */

@Entity
public class ProductLine extends Model {

    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 55)
    @Column(name = "ID")
    private String id;
    @Column(name = "PRODUCT_LINE_NUMBER")
    private Integer productLineNumber;
    @JoinColumn(name = "INSTANCE_ID", referencedColumnName = "ID")
    @ManyToOne
    private Instance instanceId;
    @OneToMany(mappedBy = "productLineId")
    private List<Product> produitCollection;

    public static Finder<String, ProductLine> find = new Finder<String,ProductLine>(ProductLine.class);

}
