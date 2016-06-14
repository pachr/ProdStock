package models;


import com.avaje.ebean.Model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Created by Paul-Alexandre on 13/06/2016.
 */

@Entity
public class BoxType extends Model{

    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 55)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "HEIGHT")
    private Integer height;
    @Column(name = "WIDTH")
    private Integer width;
    @Column(name = "PRICE")
    private Float price;
    @JoinColumn(name = "INSTANCE_ID", referencedColumnName = "ID")
    @ManyToOne
    private Instance instanceId;

    public static Finder<String, BoxType> find = new Finder<String,BoxType>(BoxType.class);


    /**
    * Returns value of id
    * @return
    */
    public Integer getId() {
        return id;
    }

    /**
    * Sets new value of id
    * @param
    */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
    * Returns value of height
    * @return
    */
    public Integer getHeight() {
        return height;
    }

    /**
    * Sets new value of height
    * @param
    */
    public void setHeight(Integer height) {
        this.height = height;
    }

    /**
    * Returns value of width
    * @return
    */
    public Integer getWidth() {
        return width;
    }

    /**
    * Sets new value of width
    * @param
    */
    public void setWidth(Integer width) {
        this.width = width;
    }

    /**
    * Returns value of price
    * @return
    */
    public Float getPrice() {
        return price;
    }

    /**
    * Sets new value of price
    * @param
    */
    public void setPrice(Float price) {
        this.price = price;
    }

    /**
    * Returns value of instanceId
    * @return
    */
    public Instance getInstanceId() {
        return instanceId;
    }

    /**
    * Sets new value of instanceId
    * @param
    */
    public void setInstanceId(Instance instanceId) {
        this.instanceId = instanceId;
    }

}
