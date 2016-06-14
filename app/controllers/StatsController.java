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
 
    public Result index() {
        return ok(index.render("yo"));
    }


/*public double calcEval(){
double eval = 0;

    for(BoxType box_type: BoxType.find.all()) {
        eval = eval + Box.count("box_type_id = ?",box_type.getId()) * box_type.getPrice();
    }
    for (Command commande : Command.find.all()) {
        eval = eval + Abs(commande.getSendingTdate() - commande.getRealTdate());
    }
    return eval;
 } 

public Solution getFieldSol(String instance_id){
	Solution solution = new Solution();
	solution = Solution.find.where().ilike("instance_id", instance_id);
	return solution;
}*/
 
}