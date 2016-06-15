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
    @Column(name = "HEIGHT")
    private Integer height;
    @Column(name = "MAX_EMPIL")
    private Integer maxEmpilement;
    @Column(name = "NB_PRODUCT")
    private Integer nbProduct;
    @Column(name = "HEIGHT_MAX")
    private Integer heightMax;
    @JoinColumn(name = "BOX_ID", referencedColumnName = "ID")
    @ManyToOne
    private Box boxId;
    @JoinColumn(name = "BOX_COMMAND_ID", referencedColumnName = "ID")
    @ManyToOne
    private Command commandPileId;
    @JoinColumn(name = "PRODUCT_TYPE_ID", referencedColumnName = "ID")
    @ManyToOne
    private ProductType productTypeId;

    public static Finder<String, Pile> find = new Finder<String,Pile>(Pile.class);

	public static Finder<String, Pile> getFind() {
		return find;
	}

	public static void setFind(Finder<String, Pile> find) {
		Pile.find = find;
	}

	public Boolean isPileOversized(Integer productHeight){

		// On teste si après l'ajout du produit on va dépasser ou non la taille de la pile
		// On renvoie 0 si on dépasse la taille max avec le nouveau produit. 1 si on ne dépasse pas
		if(this.height + productHeight > this.heightMax){
			return true;
		}
		else{
			return false;
		}
	}

	public boolean checkProductTypeId(Integer productTypeId){
		if(this.productTypeId.getId() == productTypeId){
			return true;
		}
		else{return false;}

	}

  public void updateHeight(Integer productHeight){
		this.height = this.height + productHeight;
	}

  public Boolean isOverStackingCnt(){
    if(this.nbProduct + 1 > this.maxEmpilement){
      return true;
    }
    else{return false;}
  }

  public void addProduct(){
    this.nbProduct++;
  }




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
	* Returns value of maxEmpilement
	* @return
	*/
	public Integer getMaxEmpilement() {
		return maxEmpilement;
	}

	/**
	* Sets new value of maxEmpilement
	* @param
	*/
	public void setMaxEmpilement(Integer maxEmpilement) {
		this.maxEmpilement = maxEmpilement;
	}

	/**
	* Returns value of nbProduct
	* @return
	*/
	public Integer getNbProduct() {
		return nbProduct;
	}

	/**
	* Sets new value of nbProduct
	* @param
	*/
	public void setNbProduct(Integer nbProduct) {
		this.nbProduct = nbProduct;
	}

	/**
	* Returns value of heightMax
	* @return
	*/
	public Integer getHeightMax() {
		return heightMax;
	}

	/**
	* Sets new value of heightMax
	* @param
	*/
	public void setHeightMax(Integer heightMax) {
		this.heightMax = heightMax;
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
	* Returns value of commandPileId
	* @return
	*/
	public Command getCommandPileId() {
		return commandPileId;
	}

	/**
	* Sets new value of commandPileId
	* @param
	*/
	public void setCommandPileId(Command commandPileId) {
		this.commandPileId = commandPileId;
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

	public Integer getProductTypeWidth(){
		return ProductType.find.byId(this.getProductTypeId().getId().toString()).getWidth();
	}

	public Integer getProductTypeHeight(){
		return ProductType.find.byId(this.getProductTypeId().getId().toString()).getHeight();
	}
}
