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
    private Integer id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "PRODUCT_LINE_NUMBER")
    private Integer productLineNumber;
    @JoinColumn(name = "INSTANCE_ID", referencedColumnName = "ID")
    @ManyToOne
    private Instance instanceId;
    @OneToMany(mappedBy = "productLineId")
    private List<Product> produitCollection;

    public static Finder<String, ProductLine> find = new Finder<String,ProductLine>(ProductLine.class);


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
	* Returns value of name
	* @return
	*/
	public String getName() {
		return name;
	}

	/**
	* Sets new value of name
	* @param
	*/
	public void setName(String name) {
		this.name = name;
	}

	/**
	* Returns value of productLineNumber
	* @return
	*/
	public Integer getProductLineNumber() {
		return productLineNumber;
	}

	/**
	* Sets new value of productLineNumber
	* @param
	*/
	public void setProductLineNumber(Integer productLineNumber) {
		this.productLineNumber = productLineNumber;
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
