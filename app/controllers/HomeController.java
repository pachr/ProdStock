package controllers;

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
            script();
            visualisation("1");
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


    public Result script() {
        // ***********  Algorithme de gestion de la production ***************

        // On récupère le numéro d'instance passé en paramètre
        String instance_id = "1";




        // On get l'objet instance à partir de son id
        Instance instance = Instance.find.byId(instance_id);

        // On regarde si il y a déjà une solution pour l'instance
        if(Solution.find.where().ilike("Instance_id", instance_id).findList().size() > 0){
          return redirect(controllers.routes.StatsController.stats());
        }

        // On récupère dans des variables que l'on utilisera tout au long de l'algorithe : la liste des types de prodiots, la liste des lignes de production, les type de box
        List<ProductType> productTypeList = ProductType.find.where().ilike("Instance_id", instance_id).findList();
        List<ProductLineType> productLineTypeList = ProductLineType.find.where().ilike("Instance_id", instance_id).findList();
        List<BoxType> boxTypeList = BoxType.find.where().ilike("INSTANCE_ID", instance_id ).findList();

        // On get  notre unique product line (qui contient dans un de ces champs le nombre de lignes de production à disposition)
        ProductLineType productLineType = productLineTypeList.get(0);

        // On ordonne les commandes de facon a placer celle avec la date d'expéition la plus urgente en premier
        List<Command> commandList = Command.find.where().ilike("INSTANCE_ID", instance_id).orderBy("SENDING_TDATE asc").findList();

        // Compteur pour suivre l'avancé du temps dans la production
        Integer globalTime = 0;

        // Variable pour suivre les pénalités (qui consituera une deux deux moities de l'éval)
        double feeEval2 = 0;

         // On get le nombre de ligne de production a créer à parir du champ product line number correspondent
        Integer nbLigneProductionAvailable = ProductLineType.find.where().ilike("INSTANCE_ID", instance_id ).findList().get(0).getProductLineNumber();

        // A partir du nombre de lignes de production on va créer chacune des lignes de pro en base en indiquant l'instance et en faisant le lien avec le type de ligne de prod
        // On se créé un tableau de ligne de production pour pouvoir y accéder facilement
        List<ProductLine> listProductionLine = new ArrayList<ProductLine>();
        // On boucle sur le nombre de ligne de production a disposition
        for(Integer cptProductLine = 0; cptProductLine < nbLigneProductionAvailable; cptProductLine++){
          // on déclare une nouvelle ProductLine que l'on va sauvegarder en base
          ProductLine prodLine = new ProductLine();
          Integer prodLineName = cptProductLine + 1;
          prodLine.setName(prodLineName.toString());
          prodLine.setProductLineNumber(productLineType.getId());
          prodLine.setInstanceId(instance);
          prodLine.save();

          // Ajout du ProductLine
          listProductionLine.add(prodLine);
        }


        // Pour la production, on va d'abord traiter les commandes dont on va créer les produits en parallèle sur les lignes de production et le stockage une fois que la commande est finie
        for(Integer cptCommande = 0; cptCommande < commandList.size(); cptCommande++){
        //for(int nbLigneProduction = 0; nbLigneProduction < 5; nbLigneProduction++){ // DEBUG
          // On enregistre localement les commandes
          Command currentCommand = commandList.get(cptCommande);

          // On instancie deux tableaux de type de produit
          // productTypeListCommand receuille tous les types de produits liés à cette commande ordonné par product type id pour les gérer à la suite
          List<ProductType> productTypeListCommand = new ArrayList();

          // On déclare un deuxième tableau pour avoir la liste des product type
          List<ProductType> productTypeListCommandUtil = new ArrayList();

          // On va générer les produit type utiles, qui sont bien reliés au produit.
          // On commance par get la list des produits pour cette commandes

          // sale à changer
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

          // On récupère la liste des product type distinct associés à la commande.


          // On va boucler sur les types de produits pour pouvoir commencer à les créer
          Integer cptProductType = 0;
          // On boucle sur la liste de product type en prenant comme pas le nombre de ligne de productionTime
          while(cptProductType < nbProductTypeId){
            // Pour la production, on va utiliser toutes les lignes de prodution mise à notre disposition
            // On va en retenir la durée max pour savoir quel temps ajouter à notre chronomètre total.
            // l'idée étant de parcourir la boucle while avec un pas équivalent à n ligne de production.
            // et de parcourir les produits du même type dans une boucle lors de la production
            // Chronomètre local pour chaque type de produit
            Integer maxProductionTDate = 0;

            // On souhaite ensuite boucler sur nos lignes de production pour répartir sur toutes les lignes à notre disposition

            // On doit gérer le cas ou il nous reste moins de type de produit encore a produire que de chaines de production disponible
            Integer nbLigneProdUtil = 0;
            if(cptProductType + nbLigneProductionAvailable > productTypeListCommandUtil.size()){
              // Si on a besoin de moins des n lignes de prod on rétablit le nombre réel de ligne de prodiction à utiliser
              nbLigneProdUtil = nbProductTypeId - cptProductType;
            }
            else{
              nbLigneProdUtil = nbLigneProductionAvailable;
            }

            // On définit une liste d'integer pour enregistrer le temps de production sur chacune de nos lignes de production
            List<Integer> suiviTDate = new ArrayList();

            // On boucle sur les lignes de production pour commencer la production parallèle
            for(Integer cptLigneProductionUtil = 0; cptLigneProductionUtil < nbLigneProdUtil; cptLigneProductionUtil++){
                // Chronomètre local
                Integer localProductionTime = 0;

                // On get le temps de set up dans du type de produit sur la chaine de production
                Integer setUpTime = ProductType.find.byId(productTypeList.get(cptProductType + cptLigneProductionUtil).getId().toString() ).getSetUpTime();

                // On met à jour le chronomètre local en lui ajoutant le temps de set up
                localProductionTime += setUpTime;

                // Get des produits à produire sur la ligne de production (lié à la commande et à un type de produit)
                List<Product> productListProductType = Product.find.where().ilike("command_id", currentCommand.getId().toString()).ilike("PRODUCT_TYPE_ID", productTypeListCommandUtil.get(cptProductType + cptLigneProductionUtil).getId().toString() ).findList();

                // On boucle sur ces produits
                for(Integer cptProduct = 0; cptProduct < productListProductType.size(); cptProduct++){

                  // On enregistre le produit local actuellement en train d'être traité
                  Product currentProduct = productListProductType.get(cptProduct);
                  ProductType currentProductType = productTypeList.get(cptProductType + cptLigneProductionUtil);

                  // On realise ensuite l'update du produit en indiquant le temps de début de production (chronomètre global + temps de production )
                  Integer productStartProduction = globalTime + localProductionTime;
                  currentProduct.setStartProduction(productStartProduction.toString());
                  // On get la ligne de production lié à l'objet pour l'enregistrer dans le produit
                  currentProduct.setProductLineId(listProductionLine.get(cptLigneProductionUtil));
                  // On save le produit
                  currentProduct.save();

                  // Get du temps de production associé au type de produit que l'on est en train de produire
                  Integer productTimeProduction = ProductType.find.byId(currentProductType.getId().toString() ).getProductionTime();

                  // On met à jour le temps de produciton pour l'ensemble de la production du set de produits
                  localProductionTime += productTimeProduction;


                  // On va ensuite effectuer la phase de rangement dans les box du produit

                  // variable qui reccuillera la valeure de la box finalement retenue
                  Box productBox = new Box();

                  // Product type id
                  Integer productTypeId = currentProduct.getProductTypeId().getId();
                  ProductType productType = ProductType.find.byId(productTypeId.toString());

                  // On récupère la liste des box déjà existant dans la base lié à la commande
                  List<Box> listBoxCommand = Box.find.where().ilike("Command_id", currentCommand.getId().toString()).findList();

                  // Si on doit acheter un nouveau box on prendra par défaut le plus grand // Pas implémenté
                  BoxType boxToBuy = BoxType.find.where().ilike("INSTANCE_ID", instance_id).orderBy("height*width desc").findList().get(1);

                  // Si on ne trouve pas de box lié à la commande, on regarde si il y en a un lié à l'instance et vide
                  if(listBoxCommand.size() == 0){
                    // si il n'y a pas de box lié a cette commande on va en acheter un
                    Logger.debug("Premier box pour la commande " + currentCommand.getName());
                    // On achète la box
                    productBox = new Box();
                    // On set le type de box, la commande et l'insrance
                    productBox.setBoxTypeId(boxToBuy.getId().toString());
                    productBox.setCommandId(currentCommand.getId().toString());
                    productBox.setInstanceId(instance);

                    // On veut savoir combien de box ont été créé de ce type la
                    Integer nbBoxForType = Box.find.where().ilike("Command_id", currentCommand.getId().toString()).ilike("Box_type_id", boxToBuy.getId().toString()).findList().size();
                    productBox.setBoxNumber(nbBoxForType + 1);

                    // On save la box
                    productBox.save();

                    // On doit déclarer une nouvelle pile pour la placer dans ce box
                    Pile pile = new Pile();

                    // On set les différentes propriétés de la pile // A implementer dans la classe pile un constucteur
                    pile.setWidth(productType.getWidth());
                    pile.setHeightMax(boxToBuy.getHeight());
                    pile.setHeight(productType.getHeight());
                    pile.setBoxId(productBox);
                    pile.setCommandPileId(currentCommand);
                    pile.setProductTypeId(productType);
                    pile.setMaxEmpilement(productType.getMaxUnit());
                    pile.setNbProduct(1);

                    pile.save();
                  }
                  else{
                    // On a trouvé des box liés a ma commande on va regarder si on peut trouver un box avec lameme commande, type de produit, taillle de la pile et stock mini de produit
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
                      for(Integer n = 0; n < listBoxCommand.size(); n++){
                        if(!listBoxCommand.get(n).isOverwidthed(productType.getWidth(), boxToBuy.getWidth() )){
                          productBox = listBoxCommand.get(n);
                          // On se créé une nouvelle pile et l'ajoute dans la pox
                          Pile pile = new Pile();

                          pile.setWidth(productType.getWidth());
                          pile.setHeightMax(boxToBuy.getHeight());
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
                        for(Integer n = 0; n < listBoxCommand.size(); n++){
                          if(listBoxCommand.get(n).getCurrentWidth() == 0){
                            // Alors on peut utiliser ce box
                            Logger.debug("Recyclage de box");

                            productBox = listBoxCommand.get(n);
                            // On se créé une nouvelle pile et l'ajoute dans la pox
                            Pile pile = new Pile();

                            pile.setWidth(productType.getWidth());
                            pile.setHeightMax(boxToBuy.getHeight());
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
                          productBox.setBoxTypeId(boxToBuy.getId().toString());
                          productBox.setCommandId(currentCommand.getId().toString());
                          productBox.setInstanceId(instance);

                          // On met à jour le nombre de box de ce type pour une commande donnée
                          Integer nbBoxForType = Box.find.where().ilike("Command_id", currentCommand.getId().toString()).ilike("Box_type_id", boxToBuy.getId().toString()).findList().size();
                          Logger.debug(nbBoxForType.toString());
                          productBox.setBoxNumber(nbBoxForType + 1);

                          // On save
                          productBox.save();

                          // On doit déclarer une nouvelle pile dans laquelle on assure la
                          Pile pile = new Pile();

                          pile.setWidth(productType.getWidth());
                          pile.setHeightMax(boxToBuy.getHeight());
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
                // On teste laquelle des 3 lignes de production a mis le plus de temps à s'effectuer
                // On retient la plus longue pour le démarrage de la nouvelle sérié
                // Si la date est plus longue la variable maxProductionTime est remplacé par le chronomètre de la production locale
                if(localProductionTime > maxProductionTDate){
                  maxProductionTDate = localProductionTime;
                  Logger.debug(maxProductionTDate.toString());
                }

                // On met à jour le a durée de fonctionnement de la ligne de production pour l'utiliser après
                suiviTDate.add(localProductionTime);

            }



            // Dans notre algorithme on ajoute le temps de tous les produits. Pour le parralélisme,
            // On garde seulement le temps le plus grand (celui pendant laquelle au moins une des lignes de production était en fct)
            // Et on retranche le temps des deux autres
            globalTime += maxProductionTDate;

            // on doit soustraire le temps gagné
            for(Integer a = 0; a < suiviTDate.size(); a++){
              if(suiviTDate.get(a) != maxProductionTDate){
                globalTime -= suiviTDate.get(a);
              }
            }


            cptProductType += nbLigneProdUtil;
            //Logger.debug(i.toString());
            Logger.debug("Temps de production : " + globalTime);
          }
          // On a fini de traiter les produits de la commande et de les ranger dans des box.
          // On va maintenant vider les box
          //On trouvé la liste des box associés à la commande
          List<Box> boxToFree = Box.find.where().ilike("command_id", currentCommand.getId().toString()).findList();
          for (Integer n = 0; n < boxToFree.size(); n++ ){
            // On libère le box en mettant sa longieur à 0
            boxToFree.get(n).setCurrentWidth(0);
          }

          // On va mettre à jour la date d'expédition de la commande
          // Pour avoir le temps on se base sur le chronomètre global de la production + le temps minimimum ou la commande doit être stocké
          Integer realTdate = globalTime + currentCommand.getMinTime();

          // On regarde si on est en avance. Si oui on attendra avec les box la date de livraison. cela retient plus les box mais les pénalitées d'avance coute tres cher ..
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

        return redirect(controllers.routes.StatsController.stats());
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
            return ok(compteur.toString());
        } else {
            flash("error", "Missing file");
            return badRequest();
        }
    }

}
