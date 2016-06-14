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
    private Integer id;
    @Column(name = "NAME")
    private String name;
    @OneToMany(mappedBy = "instanceId")
    private List<Product> produitCollection;
    @OneToMany(mappedBy = "instanceId")
    private List<Command> commandeCollection;
    @OneToMany(mappedBy = "instanceId")
    private List<ProductLine> productLineCollection;
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

	/**
	* Returns value of commandeCollection
	* @return
	*/
	public List<Command> getCommandeCollection() {
		return commandeCollection;
	}

	/**
	* Sets new value of commandeCollection
	* @param
	*/
	public void setCommandeCollection(List<Command> commandeCollection) {
		this.commandeCollection = commandeCollection;
	}

	/**
	* Returns value of productLineCollection
	* @return
	*/
	public List<ProductLine> getProductLineCollection() {
		return productLineCollection;
	}

	/**
	* Sets new value of productLineCollection
	* @param
	*/
	public void setProductLineCollection(List<ProductLine> productLineCollection) {
		this.productLineCollection = productLineCollection;
	}

	/**
	* Returns value of boxCollection
	* @return
	*/
	public List<Box> getBoxCollection() {
		return boxCollection;
	}

	/**
	* Sets new value of boxCollection
	* @param
	*/
	public void setBoxCollection(List<Box> boxCollection) {
		this.boxCollection = boxCollection;
	}

	/**
	* Returns value of solutionCollection
	* @return
	*/
	public List<Solution> getSolutionCollection() {
		return solutionCollection;
	}

	/**
	* Sets new value of solutionCollection
	* @param
	*/
	public void setSolutionCollection(List<Solution> solutionCollection) {
		this.solutionCollection = solutionCollection;
	}

	/**
	* Returns value of boxTypeCollection
	* @return
	*/
	public List<BoxType> getBoxTypeCollection() {
		return boxTypeCollection;
	}

	/**
	* Sets new value of boxTypeCollection
	* @param
	*/
	public void setBoxTypeCollection(List<BoxType> boxTypeCollection) {
		this.boxTypeCollection = boxTypeCollection;
	}

	/**
	* Returns value of productLineTypeCollection
	* @return
	*/
	public List<ProductLineType> getProductLineTypeCollection() {
		return productLineTypeCollection;
	}

	/**
	* Sets new value of productLineTypeCollection
	* @param
	*/
	public void setProductLineTypeCollection(List<ProductLineType> productLineTypeCollection) {
		this.productLineTypeCollection = productLineTypeCollection;
	}

	/**
	* Returns value of productTypeCollection
	* @return
	*/
	public List<ProductType> getProductTypeCollection() {
		return productTypeCollection;
	}

	/**
	* Sets new value of productTypeCollection
	* @param
	*/
	public void setProductTypeCollection(List<ProductType> productTypeCollection) {
		this.productTypeCollection = productTypeCollection;
	}



}
