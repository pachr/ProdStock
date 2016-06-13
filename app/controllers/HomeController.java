package controllers;

import play.mvc.*;

import views.html.*;

import java.nio.charset.Charset;
import java.io.*;
import java.util.Arrays;

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
            String[] arrayLine = null;
            Integer emptyLines = 0;
            File file = instanceFile.getFile();
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            DataInputStream dis = null;

            try {
              fis = new FileInputStream(file);

              // Here BufferedInputStream is added for fast reading.
              bis = new BufferedInputStream(fis);
              dis = new DataInputStream(bis);

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

                        break;
                  case 1:

                        break;
                  case 2:
                  case 3:

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
            return ok(Arrays.toString(arrayLine));
        } else {
            flash("error", "Missing file");
            return badRequest();
        }
    }

}
