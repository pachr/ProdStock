package models;

import com.avaje.ebean.Model;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

/**
 * Created by Paul-Alexandre on 13/06/2016.
 */

@Entity
public class ProductLineType extends Model {

    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 55)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "PRODUCT_LINE_NUMBER")
    private Integer productLineNumber;
    @JoinColumn(name = "INSTANCE_ID", referencedColumnName = "ID")
    @ManyToOne
    private Instance instanceId;

    public static Finder<String, ProductLineType> find = new Finder<String,ProductLineType>(ProductLineType.class);


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

}
