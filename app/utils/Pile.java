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

	public Pile(Integer boxId, Integer productTypeId, Integer productWidth, Integer productHeight, Integer boxHeightMax ){
		// Déclaration d'une nouvelle pile
		this.boxId = boxId;
		this.productTypeId = productTypeId;

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
}
