package controllers;

import models.Instance;
import play.mvc.*;

import views.html.*;

import java.nio.charset.Charset;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import models.Solution;
import models.Command;
import models.BoxType;
import models.Box;

public class StatsController extends Controller {

public Result stats() {
	//double eval = calcEval();
	String toto = "je suis la";
	Instance inst = Instance.find.byId("1");
	List<BoxType> bxTypes = BoxType.find.where().eq("INSTANCE_ID", inst.getId()).findList();
	List<Command> commandes = Command.find.where().eq("instance_id", inst.getId()).findList();
	Float eval = calcEval(bxTypes,commandes);

    return ok(views.html.stats.render(eval, inst, bxTypes,commandes));
}

/*public Result calculNbBox() {
	//long countBoxes = Box.find.where().eq("box_type_id,instance_id", box_type.getId(),instance_id).findRowCount();
	return ok(views.html.stats.render("0");
}*/

///////////////////////////////////////////////////////////////////////////////////////////
//Calcul de l'eval de la solution (formule du Sujet) cf Modele fichier solution sur drive//
/////////////////////////////////////////////////////////////////////////////////////////// 
public Float calcEval(List<BoxType> bxTypes, List<Command> commandes){
    Float eval = new Float(0);
    for(BoxType box_type: bxTypes) {
        eval = eval + box_type.calculateCout();
    }
    for (Command commande : commandes) {
        eval = eval + commande.calculateFee();
    }
    return eval;
 }

////////////////////////////////////////////////////////////////////////////////////////////////
//Faire une fonction pour get tout les fields d'une solution. (cf Modele fichier solution sur //
//drive pour les fields a retrieve). On lui passera comme parametre juste instance id         //
////////////////////////////////////////////////////////////////////////////////////////////////

public Solution getFieldSol(String instance_id){
	Solution solution = (Solution) Solution.find.where().ilike("instance_id", instance_id);
	return solution;
}

//////////////////////////////////////////////////////////////////////////////////
//Creer un joli tableau representant toute les donnees d'une instance           //
//(instance + Commande -> produit de la commande + ligne de prod + boxes etc …) //
//////////////////////////////////////////////////////////////////////////////////

/*public void AffichageTableau(Instance instance) {
	int instance_id = instance.getId();
	List<BoxType> bxType = BoxType.find.where().ilike("INSTANCE_ID", String.Valueof(instance_id));
  for (BoxType box_type : BoxType.find.where().ilike("INSTANCE_ID", String.Valueof(instance_id))) {

    long countBoxes = Box.find.where().ilike("box_type_id,instance_id", box_type.getId(),instance_id).findRowCount();
    //<tr>
    //<td>box_type.getId();</td>
    //<td>box_type.getPrice();</td>
    //<td>countBoxes</td>
    //<td>box_type.getPrice() * countBoxes</td>
    //</tr>
  }
  for (Command com : Command.find.where().ilike("instance_id", instance_id)) {
    long ecart = Math.abs(com.getSendingTdate() - com.getRealTdate());
    //<tr>
    //<td>com.getId();</td>
    //<td>com.getSendingTdate();</td>
    //<td>com.getRealTdate();</td>
    //<td>com.getFee();</td>
    //<td>ecart</td>
    //<td>ecart * com.getFee();</td>
    //</tr>
  }
}*/

/////////////////////////////////////////////////////////////////////////////////////////////
//Lister toute les instances dans un dropdown et creer un bouton qui genere sont tableau   //
//de donnee quand elle a ete selectionne                                                   //
/////////////////////////////////////////////////////////////////////////////////////////////
public void ListerInstance(){
	//<select name="instance">
	for( Instance instance: Instance.find.all()){
		//
			//<option>instance.getName();</option>
			//<
				//creer boutton qui genere ???
		//
	}
	//</select>

}

}