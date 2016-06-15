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
        Float eval = calcEval(bxTypes, commandes);

        return ok(views.html.stats.render(eval, inst, bxTypes, commandes));
    }

    /*public Result calculNbBox() {
        //long countBoxes = Box.find.where().eq("box_type_id,instance_id", box_type.getId(),instance_id).findRowCount();
        return ok(views.html.stats.render("0");
    }*/

    ///////////////////////////////////////////////////////////////////////////////////////////
    //Calcul de l'eval de la solution (formule du Sujet) cf Modele fichier solution sur drive//
    ///////////////////////////////////////////////////////////////////////////////////////////
    public Float calcEval(List<BoxType> bxTypes, List<Command> commandes) {
        Float eval = new Float(0);
        for (BoxType box_type : bxTypes) {
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

    public Solution getFieldSol(String instance_id) {
        Solution solution = (Solution) Solution.find.where().ilike("instance_id", instance_id);
        return solution;
    }
}
