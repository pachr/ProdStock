package controllers;

import models.Instance;
import play.mvc.*;

import views.html.*;

import java.nio.charset.Charset;
import java.io.*;
import java.util.Arrays;
import java.util.List;

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

    public Result upload() {
        Http.MultipartFormData<File> body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart<File> instanceFile = body.getFile("instance-file");
        if (instanceFile != null) {
            String fileName = instanceFile.getFilename();
            String contentType = instanceFile.getContentType();
            String content = null;
            String readLine = null;
            String instance_id = null;
            String[] arrayLine = null;

            Integer emptyLines = 0;
            Integer nbProduct = 0;
            File file = instanceFile.getFile();
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            DataInputStream dis = null;

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
                  arrayLine = readLine.split("   ", -1);
                  switch (emptyLines) {
                  case 0:
                    nbProduct = arrayLine[0];
                    String[] arrayProduct = new String[nbProduct];
                    ProductLineType plt = new ProductLineType();
                    plt.setProductLineNumber(arrayLine[3]);
                    plt.instanceId(instance_id);
                    plt.save();
                    break;
                  case 1:
                    ProductType pt = new ProductType();
                    pt.setInstanceId(instance_id);
                    pt.setSetUpTime(arrayLine[1]);
                    pt.setProductionTime(arrayLine[2]);
                    pt.setHeight(arrayLine[3]);
                    pt.setWidth(arrayLine[4]);
                    pt.setMaxUnit(arrayLine[5]);

                    arrayProduct.add(arrayLine[1]);
                    break;
                  case 2:
                    Command cmd = new Command();
                    cmd.setInstanceId(instance_id);
                    cmd.setName(arrayLine[0]);
                    cmd.setMinTime(arrayLine[1]);
                    cmd.setSendingTdate(arrayLine[2]);
                    cmd.setFee(arrayLine[3]);
                    cmd.save();


                    for (i=0; i<=nbProduct; i++) {
                      Product p = new Product();
                      p.setInstanceId(instance_id);
                      p.setName(arrayProduct[i]);
                      ProductType pt = ProductType.find.where().ilike("Instance_id", instance_id).ilike("Product_name", arrayProduct[i]).get();
                      p.setProductTypeId(pt.getId());
                      p.setCommandId(cmd.getId());
                      p.save();
                    }
                  case 3:
                    BoxType bt = new BoxType();
                    bt.setInstanceId(instanceId);
                    bt.setName(arrayLine[0]);
                    bt.setHeight(arrayLine[1]);
                    bt.setWidth(arrayLine[2]);
                    bt.setPrice(arrayLine[3]);
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
