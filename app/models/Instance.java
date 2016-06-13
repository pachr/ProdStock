package models;

import com.avaje.ebean.Model;

import javax.persistence.*;

import javax.validation.constraints.*;
import java.util.Collection;
import java.util.List;

/**
 * Created by Paul-Alexandre on 11/06/2016.
 */

@Entity
public class Instance extends Model {

    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 55)
    @Column(name = "ID")
    private String id;
    @OneToMany(mappedBy = "instanceId")
    private List<Produit> produitCollection;
    @OneToMany(mappedBy = "instanceId")
    private List<Commande> commandeCollection;
    @OneToMany(mappedBy = "instanceId")
    private List<ProductLines> productLinesCollection;
    @OneToMany(mappedBy = "instanceId")
    private List<Box> boxCollection;
    @OneToMany(mappedBy = "instanceId")
    private List<Solution> solutionCollection;
    @OneToMany(mappedBy = "instanceId")
    private List<BoxType> boxTypeCollection;
    @OneToMany(mappedBy = "instanceId")
    private List<ProductLineType> productLineTypeCollection;
    @OneToMany(mappedBy = "instanceId")
    private List<ProductType> productTypeCollection;

    public static Finder<String, Instance> find = new Finder<String,Instance>(Instance.class);

}
