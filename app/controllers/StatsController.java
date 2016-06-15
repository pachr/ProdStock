package controllers;

import models.Instance;
import play.mvc.*;

import views.html.*;

import java.nio.charset.Charset;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import play.Logger;

import models.*;

import java.util.HashMap;

public class StatsController extends Controller {

    public Result stats() {
        //double eval = calcEval();
        Instance inst = Instance.find.byId("1");
        List<BoxType> bxTypes = BoxType.find.where().eq("INSTANCE_ID", inst.getId()).findList();
        List<Command> commandes = Command.find.where().eq("instance_id", inst.getId()).findList();
        Float eval = Solution.find.where().eq("instance_id", inst.getId()).findList().get(0).getEvalScore();
        List<ProductLine> plList = ProductLine.find.where().eq("instance_id", inst.getId()).findList();
        List<Box> boxList = Box.find.where().eq("instance_id", inst.getId()).findList();
        Logger.debug(Integer.toString(boxList.get(1).getId()));
        return ok(views.html.stats.render(eval, inst, bxTypes, commandes, plList, boxList));
    }

    // public HashMap<String, HashMap<String, HashMap<String, String>>> lineGant(Instance inst) {
    //   allLines = new HashMap<String, HashMap<String, HashMap<String, String>>>();
    //   List<ProductLine> plt = ProductLineType.find.where().eq("INSTANCE_ID", inst.getId()).findList();
    //
    //   for (Integer j=0; j< plt.size(); j++) {
    //       products = new HashMap<String, String>();
    //       line = new HashMap<String, HashMap<String, String>>();
    //       id = new HashMap<String, String>();
    //       name = new HashMap<String, String>();
    //       id = new HashMap<String, String>();
    //       start = new HashMap<String, String>();
    //       end = new HashMap<String, String>();
    //
    //       ProductLine pl = plt.get(j);
    //       List<Product> p = Product.find.where().ilike("INSTANCE_ID", inst.getId()).ilike("Product_line_id", pl.getId()).orderBy("Start_Production", asc).findList();
    //       for (Integer i=0; i<p.size(); i++) {
    //         Integer endDate = 0;
    //         endDate = p.get(i).getProductType().getProductionTime();
    //         String date = null;
    //         date = Integer.toString(p.get(i).getStartProduction()) + "/" + Integer.toString(endDate);
    //         products.put(p.get(i).getName(), date);
    //       }
    //       id.put("id", pl.getId());
    //       line.put("id", id);
    //       name.put("name", pl.getName());
    //       line.put("name", name);
    //       line.put("products", products);
    //       start.put("start", p.get(0).getStartProduction());
    //       line.put("start", start);
    //       start.put("start", p.get(0).getStartProduction());
    //       line.put("start", start);
    //
    //       allLines.put(pl.getName(), line);
    //
    //   }
    //   return allLines;
    //
    //
    // }

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
