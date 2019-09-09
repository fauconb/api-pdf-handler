package api.fileManager;

import api.Fichier;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.org.glassfish.gmbal.Description;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;



// The Java class will be hosted at the URI path "/getAllPdfList"
@Path("/pdf")
public class PdfInfoDispatcher {
    // The Java method will process HTTP GET requests
    @GET
    // The Java method will produce content identified by the MIME Media type "text/plain"
    @Path("/getAllPdfList")
    @Produces("text/plain")
    public String getClichedMessage() {
        return this.getAllFiles();
        // Return some cliched textual content
    }
    private String getAllFiles(){
        System.out.println("toto");
        File folder = new File("c:/test/pdf");
        File[] listOfFiles = folder.listFiles();
        List<Fichier> fileList = new ArrayList<>();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                Fichier tempFile = new Fichier();
                tempFile.setSize(getFileSizeMegaBytes(listOfFiles[i]));
                tempFile.setName(listOfFiles[i].getName());
                tempFile.setPath(listOfFiles[i].getAbsolutePath());
                fileList.add(tempFile);
            } else if (listOfFiles[i].isDirectory()) {
                System.out.println("Directory " + listOfFiles[i].getName());
            }
        }
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        String json = gson.toJson(fileList);
        System.out.println(json);
        return json;
    }

    @GET
    @Description("Method en GET qui prend en parametre ?file='NomDuFichier'")
    @Path("/download")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadFileWithGet(@QueryParam("file") String file) {
        String path = "c:/test/pdf";
        File fileDownload = new File(path + File.separator + file);
        Response.ResponseBuilder response = Response.ok((Object) fileDownload);
        response.header("Content-Disposition", "attachment;filename=" + file);
        return response.build();
    }

    private static String getFileSizeMegaBytes(File file) {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        double size = (double) file.length() / (1024 * 1024);
        String res = df.format(size);
        return res + " mb";
    }
}
