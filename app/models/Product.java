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
	* Returns value of productTypeId
	* @return
	*/
	public ProductType getProductTypeId() {
		return productTypeId;
	}

	/**
	* Sets new value of productTypeId
	* @param
	*/
	public void setProductTypeId(ProductType productTypeId) {
		this.productTypeId = productTypeId;
	}

	public String getProductTypeString(){
		return this.productTypeId.getId().toString();
	}

	/**
	* Returns value of startProduction
	* @return
	*/
	public String getStartProduction() {
		return startProduction;
	}

	/**
	* Sets new value of startProduction
	* @param
	*/
	public void setStartProduction(String startProduction) {
		this.startProduction = startProduction;
	}

	/**
	* Returns value of commandId
	* @return
	*/
	public Command getCommandId() {
		return commandId;
	}

	/**
	* Sets new value of commandId
	* @param
	*/
	public void setCommandId(Command commandId) {
		this.commandId = commandId;
	}

	/**
	* Returns value of productLineId
	* @return
	*/
	public ProductLine getProductLineId() {
		return productLineId;
	}

	/**
	* Sets new value of productLineId
	* @param
	*/
	public void setProductLineId(ProductLine productLineId) {
		this.productLineId = productLineId;
	}

	/**
	* Returns value of boxId
	* @return
	*/
	public Box getBoxId() {
		return boxId;
	}

	/**
	* Sets new value of boxId
	* @param
	*/
	public void setBoxId(Box boxId) {
		this.boxId = boxId;
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

	public Integer getProductTypeWidth(){
		return ProductType.find.byId(this.getProductTypeString()).getWidth();
	}

	public Integer getProductTypeHeight(){
		return ProductType.find.byId(this.getProductTypeString()).getHeight();
	}

	/*public String getProductTypeName(){
		return ProductType.find.byId(this.getBoxTypeId()).getName() + "_" + getBoxNumber();
	}*/

}
