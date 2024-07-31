package bps.doohcar.utlis;

import java.io.FileOutputStream;
import java.util.Base64;

public abstract class ImagemUtils {

    public static final String criaImagem(String urlNginx, String imagem, String nome) {

        try {
            
            if(imagem == null || imagem.length() < 2){

                return "";

            }

            String cleanBase64 = imagem.replaceAll("data:image/png;base64,", "");

            FileOutputStream fileOutputStream = new FileOutputStream("/pictures/" + nome + ".png");

            fileOutputStream.write(Base64.getDecoder().decode(cleanBase64));

            fileOutputStream.close();

            return urlNginx + nome + ".png";

        } catch (Exception e) {

            e.printStackTrace();

            return null;

        }

    }
    
}
