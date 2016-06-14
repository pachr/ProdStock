package utils;

import java.io.*;
import java.util.*;
import play.Logger;


public class Pile{

	// largeur reste toujours la même alors que la hauteur évolue au cours du tempps
	private Integer height;
	private Integer width;
	private Integer heightMax;
	private Integer boxId;
	private Integer productTypeId;
	private Integer commandId;

	public Pile(Integer boxId, Integer productTypeId, Integer commandId, Integer productWidth, Integer productHeight, Integer boxHeightMax ){
		// Déclaration d'une nouvelle pile
		this.boxId = boxId;
		this.productTypeId = productTypeId;
		this.commandId = commandId;

		this.width = productWidth;
		this.height = productHeight;

		this.heightMax = boxHeightMax;
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
		if(this.productTypeId == productTypeId){
			return true;
		}
		else{return false;}

	}

	public void addProduct(Integer productHeight){
		this.height = this.height + productHeight;
	}
	/**
	* Create string representation of Pile for printing
	* @return
	*/
	@Override
	public String toString() {
		return "Pile [height=" + height + ", width=" + width + ", heightMax=" + heightMax + ", boxId=" + boxId + ", productTypeId=" + productTypeId + "]";
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
	public Integer getBoxId() {
		return boxId;
	}

	/**
	* Sets new value of boxId
	* @param
	*/
	public void setBoxId(Integer boxId) {
		this.boxId = boxId;
	}

	/**
	* Returns value of productTypeId
	* @return
	*/
	public Integer getProductTypeId() {
		return productTypeId;
	}

	/**
	* Sets new value of productTypeId
	* @param
	*/
	public void setProductTypeId(Integer productTypeId) {
		this.productTypeId = productTypeId;
	}

	/**
	* Returns value of commandId
	* @return
	*/
	public Integer getCommandId() {
		return commandId;
	}

	/**
	* Sets new value of commandId
	* @param
	*/
	public void setCommandId(Integer commandId) {
		this.commandId = commandId;
	}
}
