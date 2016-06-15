package models;

import com.avaje.ebean.Model;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

/**
 * Created by Paul-Alexandre on 11/06/2016.
 */

@Entity
public class Command extends Model {

    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 55)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "MIN_TIME")
    private Integer minTime;
    @Column(name = "SENDING_TDATE")
    private Integer sendingTdate;
    @Column(name = "REAL_TDATE")
    private Integer realTdate;
    @Column(name = "FEE")
    private Float fee;
    @Column(name = "PRODUCT_ID_QUANTITY")
    private Integer productIdQuantity;
    @JoinColumn(name = "INSTANCE_ID", referencedColumnName = "ID")
    @ManyToOne
    private Instance instanceId;
    @OneToOne(mappedBy="commandId")
    private Pile pile;

    public static Finder<String, Command> find = new Finder<String, Command>(Command.class);


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
	* Returns value of minTime
	* @return
	*/
	public Integer getMinTime() {
		return minTime;
	}

	/**
	* Sets new value of minTime
	* @param
	*/
	public void setMinTime(Integer minTime) {
		this.minTime = minTime;
	}

	/**
	* Returns value of sendingTdate
	* @return
	*/
	public Integer getSendingTdate() {
		return sendingTdate;
	}

	/**
	* Sets new value of sendingTdate
	* @param
	*/
	public void setSendingTdate(Integer sendingTdate) {
		this.sendingTdate = sendingTdate;
	}

	/**
	* Returns value of realTdate
	* @return
	*/
	public Integer getRealTdate() {
		return realTdate;
	}

	/**
	* Sets new value of realTdate
	* @param
	*/
	public void setRealTdate(Integer realTdate) {
		this.realTdate = realTdate;
	}

	/**
	* Returns value of fee
	* @return
	*/
	public Float getFee() {
		return fee;
	}

	/**
	* Sets new value of fee
	* @param
	*/
	public void setFee(Float fee) {
		this.fee = fee;
	}

	/**
	* Returns value of productIdQuantity
	* @return
	*/
	public Integer getProductIdQuantity() {
		return productIdQuantity;
	}

	/**
	* Sets new value of productIdQuantity
	* @param
	*/
	public void setProductIdQuantity(Integer productIdQuantity) {
		this.productIdQuantity = productIdQuantity;
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
