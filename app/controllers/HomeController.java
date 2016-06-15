package controllers;

import controllers.*;
import models.*;
import play.mvc.*;

import views.html.*;
import play.Logger;

import java.nio.charset.Charset;
import java.io.*;
import java.util.*;

import play.data.DynamicForm;
import play.data.Form;

import java.io.FileOutputStream;
import java.io.FileNotFoundException;


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

    public Result visualisation(String instance_id) {
        //String instance_id = "1";
        Instance instance = Instance.find.byId(instance_id);
        String response = "fdp";
        //Si l'instance est nulle on reexecute le script
        if (instance == null) {
            treatment(instance_id);
            visualisation(instance_id);
        }

        String name = instance.getName();

        try {
            PrintWriter writer = new PrintWriter(name.replace(".txt", ".sol"), "UTF-8");

            Solution sol = Solution.find.where().ilike("Instance_id", instance_id).findList().get(0);
            writer.println(sol.getEvalScore());
            writer.println("");
            List<BoxType> bxtList = BoxType.find.where().ilike("INSTANCE_ID", instance_id).findList();
            List<Box> boxList = Box.find.where().ilike("INSTANCE_ID", instance_id).findList();


            for (Integer j = 0; j < bxtList.size(); j++) {
                Integer compteur = 0;
                for (Integer i = 0; i < boxList.size(); i++) {
                    if (bxtList.get(j).getName().equals(boxList.get(i).getName())) {
                        compteur++;
                    }
                }
                String box = bxtList.get(j).getName() + " " + compteur;
                writer.println(box);
            }

            writer.println("");
            List<Command> commandList = Command.find.where().ilike("INSTANCE_ID", instance_id).findList();
            for (Integer k = 0; k < commandList.size(); k++) {
                String command = commandList.get(k).getName() + " " + commandList.get(k).getRealTdate();
                writer.println(command);
            }

            writer.println("");
            for (Integer l = 0; l < commandList.size(); l++) {

                List<Product> productList = Product.find.where().ilike("INSTANCE_ID", instance_id).ilike("Command_id", commandList.get(l).getId().toString()).findList();
                for (Integer m = 0; m < productList.size(); m++) {
                    //manque le numero de la box achetee
                    String product = commandList.get(l).getName() + " " + productList.get(m).getName() + " " +
                            productList.get(m).getProductLineId().getName() + " " + productList.get(m).getStartProduction() + " " +
                            productList.get(m).getBoxId().getName() + " ";
                    writer.println(product);
                }

            }

            writer.close();

        } catch (FileNotFoundException fnfe) {
            System.out.println(fnfe.getMessage());
        } catch (IOException ioe) {
            //Handle exception here, most of the time you will just log it.
            ioe.getMessage();
        }
        return ok(response);
    }


    public Result treatment(String instance_id){
        // Instance uploadé que l'on reçcoit en paramètre.
        //String instance_id = instanceId;
        Instance instance = Instance.find.byId(instance_id);
        List<ProductType> productTypeList = ProductType.find.where().ilike("Instance_id", instance_id).findList();
        List<ProductLineType> productLineTypeList = ProductLineType.find.where().ilike("Instance_id", instance_id).findList();
        ProductLineType productLineType = productLineTypeList.get(0);

        // On ordonne les commandes de facon a placer celle avec la date d'expéition la plus basse
        List<Command> commandList = Command.find.where().ilike("INSTANCE_ID", instance_id).orderBy("SENDING_TDATE asc").findList();
        List<BoxType> boxTypeList = BoxType.find.where().ilike("INSTANCE_ID", instance_id ).findList();

        // Compteur pour suivre l'avancé du temps dans la production
        Integer tempsProduction = 0;

        // Variable pour suivre les pénalités
        double feeEval2 = 0;

        Integer cpt = 0;

         // On get le nombre de ligne de production a créer
        Integer nbLigneProd = ProductLineType.find.where().ilike("INSTANCE_ID", instance_id ).findList().get(0).getProductLineNumber();
        // on se fait une liste de prodLine
        List<ProductLine> listProdLine = new ArrayList<ProductLine>();
        for(Integer k = 0; k < nbLigneProd; k++){
          ProductLine prodLine = new ProductLine();
          prodLine.setName(k.toString());
          prodLine.setProductLineNumber(productLineType.getId());
          prodLine.setInstanceId(instance);
          prodLine.save();

          listProdLine.add(prodLine);
        }


        // On boucle sur la liste des commandes
        for(int j = 0; j < commandList.size(); j++){
        //for(int j = 0; j < 5; j++){
          // On traite la première commande, a plus urgent. On créé une liste de produits qui correspondent a cette commandes
          Command currentCommand = commandList.get(j);

          // On va utiliser la liste de type de produits pour boucler dessus. Cela va permettre de ne pas perdre les temps de set up
          // On garde que ceux ou il y a bien des produits
          List<ProductType> productTypeListCommand = new ArrayList();
          List<ProductType> productTypeListCommandUtil = new ArrayList();
          // On va générer les produit type utiles, qui sont bien reliés au produit.
          // On commance par get la list des produits pour cette commandes
          List<Product> productListCommand = Product.find.where().ilike("command_id", currentCommand.getId().toString()).orderBy("PRODUCT_TYPE_ID").findList();

          Integer currentProductTypeId = 0;
          Integer nbProductTypeId = 0;
          for(Integer s = 0; s < productListCommand.size(); s++){
            Integer productTypeId = productListCommand.get(s).getProductTypeId().getId();

            if(productTypeId != currentProductTypeId || s == 0){
              nbProductTypeId++;
              productTypeListCommandUtil.add(productListCommand.get(s).getProductTypeId());
            }

            currentProductTypeId = productTypeId;
          }

          // On va boucler sur la liste des product type et les réaliser avec notre nombre de lignes
          Integer i = 0;
          // On boucle sur la liste de product type en prenant comme pas le nombre de ligne de productionTime
          while(i < nbProductTypeId){
            // On se set une variable pour retenir le temps de la ligne de production la plus longue
            Integer maxProductionTDate = 0;

            // A l'intérieur on va boucler sur la liste des nbLigneProd pour pouvoir y créer chaque produits
            // Si on a qu'un produit a créeron ne doit pas utiliser les n lignes de production
            //Logger.debug("i : " + i.toString());
            //Integer d = productTypeListCommandUtil.size();
            //Logger.debug("product type util : " + d.toString());
            //Logger.debug("nb prod : " + nbLigneProd.toString());
            Integer nbLigneProdFinal = 0;
            if(i + nbLigneProd > productTypeListCommandUtil.size()){
              // Si on a besoin de moins des n lignes de prod on rétablit le bon nombre
              nbLigneProdFinal = nbProductTypeId - i;
            }
            else{
              nbLigneProdFinal = nbLigneProd;
            }

            //Logger.debug("nbprod " + nbLigneProdFinal.toString());
            // List pour suivre les temps de traitement
            List<Integer> suiviTDate = new ArrayList();
            // on boucle sur le nombre de ligne de prod
            // nbLigneProd - 1 car on se base sur 0
            for(Integer l = 0; l < nbLigneProdFinal; l++){
                Integer localProductionTDate = 0;
                // Pour un product type, on ajoute le temps de set up
                Integer setUpTime = ProductType.find.byId(productTypeList.get(i + l).getId().toString() ).getSetUpTime();
                localProductionTDate += setUpTime;
                // Pour chaque product type on boucle sur la liste des produits.
                List<Product> productListProductType = Product.find.where().ilike("command_id", currentCommand.getId().toString()).ilike("PRODUCT_TYPE_ID", productTypeListCommandUtil.get(i + l).getId().toString() ).findList();
                // On boucle sur la liste des produits correspondant a la commande et au product type

                for(Integer v = 0; v < productListProductType.size(); v++){

                  // Boucle de la liste des produits. On va mettre à jour leur temps de traitement
                  // On ajoute le temps de productionTime
                  Product currentProduct = productListProductType.get(v);
                  Integer productTDateProduction = ProductType.find.byId(productTypeList.get(i + l).getId().toString() ).getProductionTime();
                  // On met à jour le temps de produciton par produit
                  localProductionTDate += productTDateProduction;

                  // On réalise ensuite l'update pour mettre à jour le start date du product et la ligne de production sur lequel le produit a été créé
                  // Il faut mettre l-1 pour partir de l'indice 0 du tableau de lignes de production
                  currentProduct.setStartProduction(productTDateProduction.toString());
                  currentProduct.setProductLineId(listProdLine.get(l));
                  currentProduct.save();

                //  Logger.debug("Temps de production : " + tempsProduction);

                  cpt++;

                  // On range le produit dans une box

                  // variable qui reccuillera la valeure de la boxTypeId
                  Box productBox = new Box();

                  // Product type id
                  Integer productTypeId = currentProduct.getProductTypeId().getId();
                  ProductType productType = ProductType.find.byId(productTypeId.toString());

                  // On récupère la liste des box
                  List<Box> listBox = Box.find.where().ilike("Command_id", currentCommand.getId().toString()).findList();

                  // Si on doit acheter un nouveau box on prendra par défaut le plus grand
                  BoxType boxMaxSize = BoxType.find.where().ilike("INSTANCE_ID", instance_id).orderBy("height*width desc").findList().get(1);



                  if(listBox.size() == 0){
                    Logger.debug("Premier box pour la commande " + currentCommand.getName());
                    // On achète la box
                    productBox = new Box();
                    productBox.setBoxTypeId(boxMaxSize.getId().toString());
                    productBox.setCommandId(currentCommand.getId().toString());
                    productBox.setInstanceId(instance);

                    // On veut savoir combien de box ont été créé de ce type la
                    Integer nbBoxForType = Box.find.where().ilike("Command_id", currentCommand.getId().toString()).ilike("Box_type_id", boxMaxSize.getId().toString()).findList().size();
                    productBox.setBoxNumber(nbBoxForType + 1);

                    // On save la box
                    productBox.save();



                    // On doit déclarer une nouvelle pile dans laquelle on assure la
                    Pile pile = new Pile();

                    pile.setWidth(productType.getWidth());
                    pile.setHeightMax(boxMaxSize.getHeight());
                    pile.setHeight(productType.getHeight());
                    pile.setBoxId(productBox);
                    pile.setCommandPileId(currentCommand);
                    pile.setProductTypeId(productType);
                    pile.setMaxEmpilement(productType.getMaxUnit());
                    pile.setNbProduct(1);

                    pile.save();
                    Logger.debug("create pile");

                  }
                  else{
                    // On teste si il y a une pile dispo de la bonne taille pour la bonne commande et du bon type de produit
                    Boolean endStatementFlag = false;
                    List<Pile> listPile  = Pile.find.where().ilike("BOX_COMMAND_ID", currentCommand.getId().toString()).findList();

                    for(Integer n = 0; n <listPile.size(); n++){
                      if(listPile.get(n).getCommandPileId().getId() == currentCommand.getId()){
                        if(listPile.get(n).checkProductTypeId(productTypeId)){
                          if(!listPile.get(n).isPileOversized(productType.getHeight())){
                            if(!listPile.get(n).isOverStackingCnt()){
                              Logger.debug("meme commade");
                              // On ajoute dans la pile en mettant à jour sa taille
                              listPile.get(n).updateHeight(productType.getHeight());
                              // On met à jour le nombre d'élément empilé
                              listPile.get(n).addProduct();
                              // On retourve la box pour pouvoir l'enregistrer derrière
                              productBox = Box.find.byId(listPile.get(n).getBoxId().getId().toString());

                              endStatementFlag = true;

                              Logger.debug("On a trouvé une pile de la meme commande, product type de taille" + listPile.get(n).toString());
                            }
                          }
                        }
                      }
                    }

                    // On a pas trouvé de pile pouvant être accueillir
                    // On va donc chercher un box de libre et voir si on peut y ajouter une pile
                    if(endStatementFlag != true){
                      // On parcout la liste des box
                      for(Integer n = 0; n < listBox.size(); n++){
                        if(!listBox.get(n).isOverwidthed(productType.getWidth(), boxMaxSize.getWidth() )){
                          productBox = listBox.get(n);
                          // On se créé une nouvelle pile et l'ajoute dans la pox
                          Pile pile = new Pile();

                          pile.setWidth(productType.getWidth());
                          pile.setHeightMax(boxMaxSize.getHeight());
                          pile.setHeight(productType.getHeight());
                          pile.setBoxId(productBox);
                          pile.setCommandPileId(currentCommand);
                          pile.setProductTypeId(productType);
                          pile.setMaxEmpilement(productType.getMaxUnit());
                          pile.setNbProduct(1);

                          pile.save();

                          Logger.debug("create pile");

                          // On met à jour la taille du box en ajoutant à la largeur, la largeur du produit
                          productBox.setCurrentWidth(productBox.getCurrentWidth() + productType.getWidth());
                          Logger.debug("On ajoute une nouvelle pile dans un box libre de largeur" + productBox.getCurrentWidth().toString());
                          endStatementFlag = true;
                        }
                      }
                      // Si on est pas sorti c'est qu'on a pas trouvé de pile ni de box pour l'acceuillir
                      // On doit donc acheter un nouveau box et créer une nouvelle pile que l'on range dedans
                      if(endStatementFlag != true){
                        // On teste si un box est libre
                        for(Integer n = 0; n < listBox.size(); n++){
                          if(listBox.get(n).getCurrentWidth() == 0){
                            // Alors on peut utiliser ce box
                            Logger.debug("Recyclage de box");

                            productBox = listBox.get(n);
                            // On se créé une nouvelle pile et l'ajoute dans la pox
                            Pile pile = new Pile();

                            pile.setWidth(productType.getWidth());
                            pile.setHeightMax(boxMaxSize.getHeight());
                            pile.setHeight(productType.getHeight());
                            pile.setBoxId(productBox);
                            pile.setCommandPileId(currentCommand);
                            pile.setProductTypeId(productType);
                            pile.setMaxEmpilement(productType.getMaxUnit());
                            pile.setNbProduct(1);

                            pile.save();

                            // On met à jour la taille du box en ajoutant à la largeur, la largeur du produit
                            productBox.setCurrentWidth(productBox.getCurrentWidth() + productType.getWidth());

                            endStatementFlag = true;
                          }

                        }

                        if(endStatementFlag != true){
                          Logger.debug("On a aucun box / pile on en achete un nouveau");
                          // On achète la box
                          productBox = new Box();
                          productBox.setBoxTypeId(boxMaxSize.getId().toString());
                          productBox.setCommandId(currentCommand.getId().toString());
                          productBox.setInstanceId(instance);

                          // On met à jour le nombre de box de ce type pour une commande donnée
                          Integer nbBoxForType = Box.find.where().ilike("Command_id", currentCommand.getId().toString()).ilike("Box_type_id", boxMaxSize.getId().toString()).findList().size();
                          Logger.debug(nbBoxForType.toString());
                          productBox.setBoxNumber(nbBoxForType + 1);

                          // On save
                          productBox.save();

                          // On doit déclarer une nouvelle pile dans laquelle on assure la
                          Pile pile = new Pile();

                          pile.setWidth(productType.getWidth());
                          pile.setHeightMax(boxMaxSize.getHeight());
                          pile.setHeight(productType.getHeight());
                          pile.setBoxId(productBox);
                          pile.setCommandPileId(currentCommand);
                          pile.setProductTypeId(productType);
                          pile.setMaxEmpilement(productType.getMaxUnit());
                          pile.setNbProduct(1);

                          pile.save();

                          Logger.debug("create pile");
                      }
                    }
                  }
                 }
                  // On a placé trouvé une nouvelle box / pile et mis à jour leurs états
                  // On va donc maintenant mettre à jour en base le produit et le box associé
                  currentProduct.setBoxId(productBox);
                  currentProduct.save();

                }
                // on va retenir le temps le plus grand au cours des 3 productions
                if(localProductionTDate > maxProductionTDate){
                  maxProductionTDate = localProductionTDate;
                  Logger.debug(maxProductionTDate.toString());
                }

                // On ajoute le temps de production
                suiviTDate.add(localProductionTDate);

            }



            //Logger.debug("max" + maxProductionTDate);
            // Fin du product type a l'intérieur de la commande
            // Une fois les lignes de production parcourue, on met à jour le temps de production


            tempsProduction += maxProductionTDate;

            // on doit soustraire le temps gagné
            for(Integer a = 0; a < suiviTDate.size(); a++){
              if(suiviTDate.get(a) != maxProductionTDate){
                tempsProduction -= suiviTDate.get(a);
              }
            }


            i += nbLigneProdFinal;
            //Logger.debug(i.toString());
            Logger.debug("Temps de production : " + tempsProduction);
          }
          // Fin de la commande
          // On a fini la commande on va libérér ses box
          // Quand la commande est finie on procède au vidage des box utilisés e
          List<Box> boxToFree = Box.find.where().ilike("command_id", currentCommand.getId().toString()).findList();
          for (Integer n = 0; n < boxToFree.size(); n++ ){
            // On libère le box en mettant sa longieur à 0
            boxToFree.get(n).setCurrentWidth(0);
          }

          // On met à jour la date réele de la commande
          Integer realTdate = tempsProduction + currentCommand.getMinTime();

          // On regarde si on est en avance. Si oui on attendra avec les box
          if(realTdate < currentCommand.getSendingTdate()){
            realTdate = currentCommand.getSendingTdate();
          }

          // On sauvegarde le résulatat obtenu dans la commande
          currentCommand.setRealTdate(realTdate);
          currentCommand.save();

          // Puis on calcule les pénalités
          // Pour les pénalités on doit regarder si la date d'envoie est différente de celle de la commande et ajouter la valeur absolu de la différence + le cout par unité de temps
          double intermediateFee = currentCommand.getFee() * Math.abs(currentCommand.getSendingTdate() - realTdate);
          //Logger.debug("intermediate " + String.valueOf(intermediateFee));
          feeEval2 += intermediateFee;


          Logger.debug("One command added. Time : " + currentCommand.getName().toString());
        }

        // Calcul du eval
        // On récupère la date finale
        Command lastCommandSent = Command.find.where().ilike("INSTANCE_ID", instance_id).orderBy("REAL_TDATE desc").findList().get(0);
        Integer lastRealTdate = lastCommandSent.getRealTdate();

        // On récupère la liste des box achétés pour toutes les commandes
        List<Box> allBox = Box.find.where().ilike("INSTANCE_ID", instance_id).findList();

        // variable eval 1 qui va compter le prix de tous les box
        double feeEval1 = 0;
        for(Integer n=0; n < allBox.size(); n++){
          double boxPrice = BoxType.find.where().ilike("ID", allBox.get(n).getBoxTypeId()).findList().get(0).getPrice();
          feeEval1 += boxPrice;
        }

        // On ajoute les deux eval
        double eval = feeEval1 + feeEval2 ;

        Logger.debug(String.valueOf(feeEval2));


        // On ajoute la solution dans la bdd
        Solution sol = new Solution();
        sol.setName("Sol Insance" + instance_id);
        sol.setFee(((Double) feeEval2).floatValue());
        sol.setSendingDate(lastRealTdate);
        sol.setEvalScore(((Double) eval).floatValue());
        sol.setInstanceId(instance);
        sol.save();

        Logger.debug(cpt.toString());

        return redirect(controllers.routes.StatsController.stats(instance_id));
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
            Integer compteur = 0;
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
                inst.setName(fileName);
                inst.save();
                instance_id = inst.getId();

                // dis.available() returns 0 if the file does not have more lines.
                while (dis.available() != 0) {
                    readLine = dis.readLine();
                    content = content + readLine;

                    if (readLine == null || "".equals(readLine)) {
                        emptyLines++;
                    } else {

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
                                Integer cpt = 4;
                                Command cmd = new Command();
                                cmd.setInstanceId(inst);
                                cmd.setName(arrayLine[0]);
                                cmd.setMinTime(Integer.parseInt(arrayLine[1]));
                                cmd.setSendingTdate(Integer.parseInt(arrayLine[2]));
                                cmd.setFee(Float.parseFloat(arrayLine[3]));
                                cmd.save();

                                for (Integer i = 0; i < nbProduct; i++) {


                                    if (Integer.parseInt(arrayLine[cpt]) != 0) {
                                        for (Integer j = 0; j < Integer.parseInt(arrayLine[cpt]); j++) {
                                            Product p = new Product();
                                            p.setInstanceId(inst);
                                            p.setName(arrayProduct.get(i));
                                            List<ProductType> ptt = ProductType.find.where().ilike("Instance_id", Integer.toString(instance_id)).ilike("Name", arrayProduct.get(i)).findList();

                                            p.setProductTypeId(ptt.get(0));
                                            p.setCommandId(cmd);

                                            p.save();
                                            compteur++;
                                        }
                                    }
                                    cpt++;
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
            return redirect(controllers.routes.HomeController.index());
        } else {
            flash("error", "Missing file");
            return badRequest();
        }
    }

}
