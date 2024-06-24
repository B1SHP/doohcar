package bps.doohcar.utlis;

import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.util.Base64;

public abstract class ImagemUtils {

    public static final String criaImagem(String urlNginx, String imagem, String nome) {

        try {
            
            if(imagem == null || imagem.length() < 2){

                return "";

            }

            String novoNome = nome == null || nome.length() < 1 ? LocalDateTime.now().toString() : nome;

            String cleanBase64 = imagem.replaceAll("data:image/png;base64,", "");

            FileOutputStream fileOutputStream = new FileOutputStream("/home/bruno/Documents/Estudo/job?/dooh-car/pictures/" + novoNome + ".png");

            fileOutputStream.write(Base64.getDecoder().decode(cleanBase64));

            fileOutputStream.close();

            return urlNginx + novoNome + ".png";

        } catch (Exception e) {

            e.printStackTrace();

            return null;

        }

    }
    
}
