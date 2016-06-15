package models;


import com.avaje.ebean.Model;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

/**
 * Created by Paul-Alexandre on 11/06/2016.
 */

@Entity
public class Pile extends Model{

    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 55)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "WIDTH")
    private Integer width;
    @Column(name = "HEIGHT_MAX")
    private Integer heightMax;
    @JoinColumn(name = "BOX_ID", referencedColumnName = "ID")
    @OneToOne
    private Box boxId;
    @JoinColumn(name = "BOX_COMMAND_ID", referencedColumnName = "ID")
    @OneToOne
    private Command commandPileId;
    @JoinColumn(name = "PRODUCT_TYPE_ID", referencedColumnName = "ID")
    @OneToOne
    private ProductType productTypeId;

    public static Finder<String, Pile> find = new Finder<String,Pile>(Pile.class);


  }
