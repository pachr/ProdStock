package controllers;

import models.*;
import play.mvc.*;

import views.html.*;

import java.nio.charset.Charset;
import java.io.*;
import java.util.Arrays;
import java.util.List;

public class StatsController extends Controller {

    public Result stats(String instanceId) {
        //double eval = calcEval();
        Instance inst = Instance.find.byId(instanceId);
        List<BoxType> bxTypes = BoxType.find.where().eq("INSTANCE_ID", inst.getId()).findList();
        List<Command> commandes = Command.find.where().eq("instance_id", inst.getId()).findList();
        List<Solution> solutions = Solution.find.where().ilike("instance_id", instanceId).findList();
        Float eval = solutions.get(0).getEvalScore();
        return ok(views.html.stats.render(eval, inst, bxTypes, commandes));
    }

    /*public Result calculNbBox() {
        //long countBoxes = Box.find.where().eq("box_type_id,instance_id", box_type.getId(),instance_id).findRowCount();
        return ok(views.html.stats.render("0");
    }*/

    public Result visualize(String instanceId, String commandId){
        Instance inst = Instance.find.byId(instanceId);
        Command cmd = Command.find.byId(commandId);
        List<Box> boxes = Box.find.where().ilike("command_id", commandId).findList();
        List<Product> products = Product.find.where().ilike("command_id", commandId).findList();
        List<Pile> piles = Pile.find.where().ilike("box_command_id", commandId).findList();
        return ok(views.html.visualize.render(inst, cmd, boxes, products, piles));
    }

}
