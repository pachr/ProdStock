package controllers;

import models.*;
import play.mvc.*;

import views.html.*;

import java.nio.charset.Charset;
import java.io.*;
import java.util.*;
import play.Logger;


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
                    Logger.debug(arrayLine[0]);
                    nbProduct = Integer.parseInt(arrayLine[0]);
                    ProductLineType plt = new ProductLineType();
                    plt.setProductLineNumber(Integer.parseInt(arrayLine[3]));
                    plt.setInstanceId(inst);
                    plt.save();
                    Logger.debug("case 0");
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
                    Logger.debug("case 1");
                    Command cmd = new Command();
                    cmd.setInstanceId(inst);
                    cmd.setName(arrayLine[0]);
                    cmd.setMinTime(Integer.parseInt(arrayLine[1]));
                    cmd.setSendingTdate(Integer.parseInt(arrayLine[2]));
                    cmd.setFee(Float.parseFloat(arrayLine[3]));
                    cmd.save();


                    for (Integer i=0; i < nbProduct; i++) {
                      Logger.debug("i");
                      Product p = new Product();
                      p.setInstanceId(inst);
                      p.setName(arrayProduct.get(i));
                      Logger.debug(arrayProduct.get(i));
                      List<ProductType> ptt = ProductType.find.where().ilike("Instance_id", Integer.toString(instance_id)).ilike("Name", arrayProduct.get(i)).findList();
                      Logger.debug("i");
                      p.setProductTypeId(ptt.get(0));
                      p.setCommandId(cmd);
                      p.save();

                    }
                    Logger.debug("fin case 2");
                    break;
                  case 3:
                    Logger.debug("case 3");
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
            return ok("YO");
        } else {
            flash("error", "Missing file");
            return badRequest();
        }
    }

}
