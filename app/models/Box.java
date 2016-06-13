package models;


import com.avaje.ebean.Model;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

/**
 * Created by Paul-Alexandre on 11/06/2016.
 */

@Entity
public class Box extends Model{

    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 55)
    @Column(name = "ID")
    private String id;
    @JoinColumn(name = "BOX_TYPE_ID", referencedColumnName = "ID")
    @ManyToOne
    private String boxTypeId;
    @JoinColumn(name = "COMMAND_ID", referencedColumnName = "ID")
    @ManyToOne
    private String commandId;
    @JoinColumn(name = "INSTANCE_ID", referencedColumnName = "ID")
    @ManyToOne
    private Instance instanceId;
    @OneToMany(mappedBy = "productId")
    private List<Produit> produitCollection;

    public static Finder<String, Box> find = new Finder<String,Box>(Box.class);

}
