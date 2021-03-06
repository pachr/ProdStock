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
    private Integer id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "BOX_NUMBER")
    private Integer boxNumber;
    @JoinColumn(name = "BOX_TYPE_ID", referencedColumnName = "ID")
    @ManyToOne
    private String boxTypeId;
    @JoinColumn(name = "COMMAND_ID", referencedColumnName = "ID")
    @ManyToOne
    private String commandId;
    @JoinColumn(name = "INSTANCE_ID", referencedColumnName = "ID")
    @ManyToOne
    private Instance instanceId;
    @OneToMany(mappedBy="boxId")
    private Pile pile;
    @OneToMany(mappedBy = "boxId")
    private List<Product> produitCollection;

    public static Finder<String, Box> find = new Finder<String,Box>(Box.class);

    public static Integer currentWidth = 0;








  public Boolean isOverwidthed(Integer productWidth, Integer boxMaxWidth){
    if(this.currentWidth + productWidth > boxMaxWidth){
      // On dépasse
      return true;
    }
    else{
      return false;
    }
  }

  public List<Product> getBoxProduct() {
    return Product.find.where().ilike("INSTANCE_ID", Integer.toString(this.getInstanceId().getId())).ilike("Box_id", Integer.toString(this.getId())).orderBy("Start_Production asc").findList();
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
	* Returns value of boxNumber
	* @return
	*/
	public Integer getBoxNumber() {
		return boxNumber;
	}

	/**
	* Sets new value of boxNumber
	* @param
	*/
	public void setBoxNumber(Integer boxNumber) {
		this.boxNumber = boxNumber;
	}

	/**
	* Returns value of boxTypeId
	* @return
	*/
	public String getBoxTypeId() {
		return boxTypeId;
	}

	/**
	* Sets new value of boxTypeId
	* @param
	*/
	public void setBoxTypeId(String boxTypeId) {
		this.boxTypeId = boxTypeId;
	}

	/**
	* Returns value of commandId
	* @return
	*/
	public String getCommandId() {
		return commandId;
	}

	/**
	* Sets new value of commandId
	* @param
	*/
	public void setCommandId(String commandId) {
		this.commandId = commandId;
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
	* Returns value of pile
	* @return
	*/
	public Pile getPile() {
		return pile;
	}

	/**
	* Sets new value of pile
	* @param
	*/
	public void setPile(Pile pile) {
		this.pile = pile;
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
	* Returns value of g
	* @return
	*/

	public static Integer getCurrentWidth() {
		return currentWidth;
	}

	/**
	* Sets new value of currentWidth
	* @param
	*/
	public static void setCurrentWidth(Integer currentWidth) {
		Box.currentWidth = currentWidth;
	}

	public Integer getBoxTypeWidth(){
		return BoxType.find.byId(this.getBoxTypeId()).getWidth();
	}

	public Integer getBoxTypeHeight(){
		return BoxType.find.byId(this.getBoxTypeId()).getHeight();
	}

	public String getBoxTypeName(){
		return BoxType.find.byId(this.getBoxTypeId()).getName() + "_" + getBoxNumber();
	}
}
