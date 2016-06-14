package models;

import com.avaje.ebean.Model;

import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * Created by Paul-Alexandre on 11/06/2016.
 */

@Entity
public class Solution extends Model {

    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 55)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "FEE")
    private Float fee;
    @Column(name = "SENDING_DATE")
    private Integer sendingDate;
    @Column(name = "EVAL_SCORE")
    private Float evalScore;
    @JoinColumn(name = "INSTANCE_ID", referencedColumnName = "ID")
    @ManyToOne
    private Instance instanceId;

    public static Finder<String, Solution> find = new Finder<String,Solution>(Solution.class);


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
	* Returns value of sendingDate
	* @return
	*/
	public Integer getSendingDate() {
		return sendingDate;
	}

	/**
	* Sets new value of sendingDate
	* @param
	*/
	public void setSendingDate(Integer sendingDate) {
		this.sendingDate = sendingDate;
	}

	/**
	* Returns value of evalScore
	* @return
	*/
	public Float getEvalScore() {
		return evalScore;
	}

	/**
	* Sets new value of evalScore
	* @param
	*/
	public void setEvalScore(Float evalScore) {
		this.evalScore = evalScore;
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
