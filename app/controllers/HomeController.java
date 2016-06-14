package controllers;

import models.*;
import play.mvc.*;
import utils.*;

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
        return ok(index.render("ProdStock Project"));
    }

    public Result script() {
     // Instance uploadé que l'on reçcoit en paramètre.
        String instance_id = "1";
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

        // On se créé une liste de piles pour pouvoir suivre l'évolution
        List<Pile> listPile = new ArrayList();

        //for(int j = 0; j < productTypeList.size(); j++){
        for(int j = 0; j < 1 ; j++){
            // On get la liste des produits d'un type
            ProductType productType = productTypeList.get(j);
            Integer productTypeId = productType.getId();
            List<Product> productList = Product.find.where().ilike("PRODUCT_TYPE_ID", productTypeId.toString()).findList();

            // On va maintenant le produire
            // On incrémente du temps de setup
            tempsProduction += productType.getSetUpTime();

            // On boucle sur le nombre de produit
            for (int i = 0; i < productList.size(); i++) {
              //for (int i = 0; i < 40; i++) {
                // Au niveau du product on commence par recalculer le temps
                tempsProduction += productTypeList.get(j).getProductionTime();
                Product p = productList.get(i);
                // On réalise ensuite l'update pour mettre à jour le start date du product et la ligne de production sur lequel le produit a été créé
                p.setStartProduction(tempsProduction.toString());
                p.setProductLineId(prodLine);
                p.save();

                // On récupère le commande lié au produit
                Command command = p.getCommandId();

                // variable qui reccuillera la valeure de la boxTypeId
                Box productBox = new Box();

                // On récupère la liste des box
                List<Box> listBox = Box.find.where().ilike("Command_id", command.getId().toString()).findList();

                // Si on doit acheter un nouveau box on prendra par défaut le plus grand
                BoxType boxMaxSize = BoxType.find.where().ilike("INSTANCE_ID", instance_id).orderBy("height*width desc").findList().get(0);
                // Si il n' ya pas de box pour cette commande on en achète un
                if(listBox.size() == 0){
                  Logger.debug("Premier box pour la commande " + command.getName());
                  // On achète la box
                  productBox = new Box();
                  productBox.setBoxTypeId(boxMaxSize.getId().toString());
                  productBox.setCommandId(command.getId().toString());
                  productBox.setInstanceId(instance);
                  productBox.save();

                  // On doit déclarer une nouvelle pile dans laquelle on assure la
                  Pile pile = new Pile(productBox.getId(), productTypeId, productType.getWidth(), productType.getHeight(), boxMaxSize.getHeight());
                  listPile.add(pile);
                }
                else{
                  // On teste si il y a une pile dispo de la bonne taille
                  Boolean endStatementFlag = false;
                  for(Integer n = 0; n <listPile.size(); n++){
                    if(listPile.get(n).checkProductTypeId(productTypeId)){
                      if(!listPile.get(n).isPileOversized(productType.getHeight())){
                        Logger.debug("On a joute le produit a la pile trouvé");
                        // On ajoute dans la pile en mettant à jour sa taille
                        listPile.get(n).addProduct(productType.getHeight());
                        // On retourve la box pour pouvoir l'enregistrer derrière
                        productBox = Box.find.byId(listPile.get(n).getBoxId().toString());
                        endStatementFlag = true;
                      }
                    }
                  }

                  // On a pas trouvé de pile pouvant être accueillir
                  // On va donc chercher un box de libre et voir si on peut y ajouter une pile
                  if(endStatementFlag != true){
                    // On parcout la liste des box
                    for(Integer n = 0; n < listBox.size(); n++){
                      if(!listBox.get(n).isOverwidthed(productType.getWidth(), boxMaxSize.getWidth() )){
                        Logger.debug("On ajoute une nouvelle pile dans un box libre en largeur");
                        productBox = listBox.get(n);
                        // On se créé une nouvelle pile et l'ajoute dans la pox
                        Pile pile = new Pile(productBox.getId(), productTypeId, productType.getWidth(), productType.getHeight(), boxMaxSize.getHeight());
                        listPile.add(pile);

                        // On met à jour la taille du box en ajoutant à la largeur, la largeur du produit
                        productBox.setCurrentWidth(productBox.getCurrentWidth() + productType.getWidth());
                        endStatementFlag = true;
                      }
                    }

                    // Si on est pas sorti c'est qu'on a pas trouvé de pile ni de box pour l'acceuillir
                    // On doit donc acheter un nouveau box et créer une nouvelle pile que l'on range dedans
                    if(endStatementFlag != true){
                      Logger.debug("On a aucun box / pile on en achete un nouveau");
                      // On achète la box
                      productBox = new Box();
                      productBox.setBoxTypeId(boxMaxSize.getId().toString());
                      productBox.setCommandId(command.getId().toString());
                      productBox.setInstanceId(instance);
                      productBox.save();

                      // On doit déclarer une nouvelle pile dans laquelle on assure la
                      Pile pile = new Pile(productBox.getId(), productTypeId, productType.getWidth(), productType.getHeight(), boxMaxSize.getHeight());
                      listPile.add(pile);
                    }
                  }
                }
                // On a placé trouvé une nouvelle box / pile et mis à jour leurs états
                // On va donc maintenant mettre à jour en base le produit et le box associé
                /*p.setBoxId(productBox);
                p.save();

                // On get toutes les produits de la commande dont le champ box id est nul => Ils n'ont pas été affecté la commande n'est pas complète
                List<Product> productCommandList = Product.find.where().ilike("command_id", command.getId().toString()).ilike("box_id", null).findList();*/
                Logger.debug("fin traitement produit");
              }
            }
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
