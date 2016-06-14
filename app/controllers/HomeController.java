package controllers;

import models.*;
import play.mvc.*;

import views.html.*;
import play.Logger;

import java.nio.charset.Charset;
import java.io.*;
import java.util.*;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
    public Result index() {
        List<Instance> instances = Instance.find.all();
        return ok(views.html.index.render("ProdStock Project", instances));
    }

    public Result script() {
      // Instance uploadé que l'on reçcoit en paramètre.
       /* String instance_id = "1";
        Instance instance = Instance.find.byId(instance_id);
        List<ProductType> productTypeList = ProductType.find.where().ilike("Instance_id", instance_id).findList();
        List<ProductLineType> productLineTypeList = ProductLineType.find.where().ilike("Instance_id", instance_id).findList();
        ProductLineType productLineType = productLineTypeList.get(0);
        List<Command> commandList = Command.find.where().ilike("INSTANCE_ID", instance_id).findList();
        List<BoxType> boxTypeList = BoxType.find.where().ilike("INSTANCE_ID", instance_id ).findList();

        Integer tempsProduction = 0;

         // On créé la ligne de production, pour l'instant il y en a qu'une
        ProductLine prodLine = new ProductLine();
        prodLine.setName("Unique Product Line");
        prodLine.setProductLineNumber(productLineType.getId());
        prodLine.setInstanceId(instance);
        prodLine.save();
    
        for(int j = 0; j < productTypeList.size(); j++){
            // On get la liste des produits d'un type
            List<Product> productList = Product.find.where().ilike("PRODUCT_TYPE_ID", productTypeList.get(j).getId().toString()).findList();

            // On va maintenant le produire
            // On incrémente du temps de setup
            tempsProduction += productTypeList.get(j).getSetUpTime();

            // On boucle sur le nombre de produit
            //for (int i = 0; i < productList.size(); i++) {
              for (int i = 0; i < 1; i++) {
                // Au niveau du product on commence par recalculer le temps
                tempsProduction += productTypeList.get(j).getProductionTime();
                Product p = productList.get(i);
                // On réalise ensuite l'update pour mettre à jour le start date du product et la ligne de production sur lequel le produit a été créé
                p.setStartProduction(tempsProduction.toString());
                p.setProductLineId(prodLine);
                p.save();

                // On récupère le commande lié au produit
                Command c = p.getCommandId();

                // On regarde si il y a déjà un box associé à cette commande
               /*Integer nbBoxCommand = Box.find.where().ilike("Command_id", c.getId().toString()).findList().size();
                if(nbBoxCommand == 0){
                  // On doit acheter un nouveau box pour cette commande --> Par défaut on va choisir le plus grand
                  BoxType boxMaxSize = BoxType.find.where().ilike("INSTANCE_ID", instance_id).orderBy("height*width desc").findList().get(0);
                  Logger.debug(boxMaxSize.getId().toString());
                }

             }
        }*/
      return ok("FDP");
    }

    public Result upload() {
        Http.MultipartFormData<File> body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart<File> instanceFile = body.getFile("instance-file");
        if (instanceFile != null) {
            String fileName = instanceFile.getFilename();
            String contentType = instanceFile.getContentType();
            String content = null;
            String readLine = null;
            Integer instance_id = 0;
            String[] arrayLine = null;

            Integer emptyLines = 0;
            Integer nbProduct = 0;
            File file = instanceFile.getFile();
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            DataInputStream dis = null;
            List<String> arrayProduct = new ArrayList<String>();

            try {
              fis = new FileInputStream(file);

              // Here BufferedInputStream is added for fast reading.
              bis = new BufferedInputStream(fis);
              dis = new DataInputStream(bis);

              Instance inst = new Instance();
              inst.setName(file.getName());
              inst.save();
              instance_id = inst.getId();

              // dis.available() returns 0 if the file does not have more lines.
              while (dis.available() != 0) {
                readLine = dis.readLine();
                content = content + readLine;

                if (readLine == null || "".equals(readLine)) {
                  emptyLines ++;
                } else{

                  arrayLine = readLine.split(" +", -1);

                  switch (emptyLines) {
                  case 0:
                    nbProduct = Integer.parseInt(arrayLine[0]);
                    ProductLineType plt = new ProductLineType();
                    plt.setProductLineNumber(Integer.parseInt(arrayLine[3]));
                    plt.setInstanceId(inst);
                    plt.save();
                    break;
                  case 1:

                    ProductType pt = new ProductType();
                    pt.setInstanceId(inst);
                    pt.setName(arrayLine[0]);
                    pt.setSetUpTime(Integer.parseInt(arrayLine[1]));
                    pt.setProductionTime(Integer.parseInt(arrayLine[2]));
                    pt.setHeight(Integer.parseInt(arrayLine[3]));
                    pt.setWidth(Integer.parseInt(arrayLine[4]));
                    pt.setMaxUnit(Integer.parseInt(arrayLine[5]));
                    pt.save();

                    arrayProduct.add(arrayLine[0]);


                    break;
                  case 2:
                    Command cmd = new Command();
                    cmd.setInstanceId(inst);
                    cmd.setName(arrayLine[0]);
                    cmd.setMinTime(Integer.parseInt(arrayLine[1]));
                    cmd.setSendingTdate(Integer.parseInt(arrayLine[2]));
                    cmd.setFee(Float.parseFloat(arrayLine[3]));
                    cmd.save();


                    for (Integer i=0; i < nbProduct; i++) {
                      Product p = new Product();
                      p.setInstanceId(inst);
                      p.setName(arrayProduct.get(i));
                      List<ProductType> ptt = ProductType.find.where().ilike("Instance_id", Integer.toString(instance_id)).ilike("Name", arrayProduct.get(i)).findList();
                      Logger.debug("i");
                      p.setProductTypeId(ptt.get(0));
                      p.setCommandId(cmd);
                      p.save();

                    }
                    break;
                  case 3:
                    BoxType bt = new BoxType();
                    bt.setInstanceId(inst);
                    bt.setName(arrayLine[0]);
                    bt.setHeight(Integer.parseInt(arrayLine[1]));
                    bt.setWidth(Integer.parseInt(arrayLine[2]));
                    bt.setPrice(Float.parseFloat(arrayLine[3]));
                    bt.save();
                    break;
                  default:
                    break;
                  }
                }

              }

              // dispose all the resources after using them.
              fis.close();
              bis.close();
              dis.close();

            } catch (FileNotFoundException e) {
              e.printStackTrace();
            } catch (IOException e) {
              e.printStackTrace();
            }
            return ok("Upload success");
        } else {
            flash("error", "Missing file");
            return badRequest();
        }
    }

}
