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
	* Returns value of setUpTime
	* @return
	*/
	public Integer getSetUpTime() {
		return setUpTime;
	}

	/**
	* Sets new value of setUpTime
	* @param
	*/
	public void setSetUpTime(Integer setUpTime) {
		this.setUpTime = setUpTime;
	}

	/**
	* Returns value of productionTime
	* @return
	*/
	public Integer getProductionTime() {
		return productionTime;
	}

	/**
	* Sets new value of productionTime
	* @param
	*/
	public void setProductionTime(Integer productionTime) {
		this.productionTime = productionTime;
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
	* Returns value of maxUnit
	* @return
	*/
	public Integer getMaxUnit() {
		return maxUnit;
	}

	/**
	* Sets new value of maxUnit
	* @param
	*/
	public void setMaxUnit(Integer maxUnit) {
		this.maxUnit = maxUnit;
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

	/**
	* Returns value of produitCollection
	* @return
	*/
	public List<Product> getProduitCollection() {
		return produitCollection;
	}

	/**
	* Sets new value of produitCollection
	* @param
	*/
	public void setProduitCollection(List<Product> produitCollection) {
		this.produitCollection = produitCollection;
	}

}
