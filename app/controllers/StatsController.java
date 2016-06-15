package controllers;

import models.*;
import play.mvc.*;

import views.html.*;

import java.nio.charset.Charset;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import play.Logger;

import models.*;


public class StatsController extends Controller {

    public Result stats(String instanceId) {
        //double eval = calcEval();
        Instance inst = Instance.find.byId(instanceId);
        List<BoxType> bxTypes = BoxType.find.where().eq("INSTANCE_ID", inst.getId()).findList();
        List<Command> commandes = Command.find.where().eq("instance_id", inst.getId()).findList();
        List<Solution> solutions = Solution.find.where().ilike("instance_id", instanceId).findList();
        Float eval = solutions.get(0).getEvalScore();
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

    public Result visualize(String instanceId, String commandId){
        Instance inst = Instance.find.byId(instanceId);
        Command cmd = Command.find.byId(commandId);
        List<Box> boxes = Box.find.where().ilike("command_id", commandId).findList();
        List<Product> products = Product.find.where().ilike("command_id", commandId).findList();
        List<Pile> piles = Pile.find.where().ilike("box_command_id", commandId).findList();
        return ok(views.html.visualize.render(inst, cmd, boxes, products, piles));
    }

}
