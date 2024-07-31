package bps.doohcar.utlis;

import bps.doohcar.dtos.locais.requests.ColetaLocaisRequest;
import bps.doohcar.dtos.propagandas.requests.AlteraPropagandaRequest;

public abstract class MysqlUtils {

    public static final String alteraPropaganda(AlteraPropagandaRequest request, String urlImagem, String urlVideo){

        StringBuilder sb = new StringBuilder();

        boolean virgula = false;

        if(request.titulo() != null && request.titulo().length() > 0){

            sb.append("titulo = '").append(request.titulo()).append("'");

            virgula = true;

        } 

        if(urlVideo != null && urlVideo.length() > 0){

            sb.append(virgula ? ",\n" : "").append("url_video = '").append(urlVideo).append("'");

            virgula = true;

        } 

        if(request.urlRedirecionamento() != null && request.urlRedirecionamento().length() > 0){

            sb.append(virgula ? ",\n" : "").append("url_redirecionamento = '").append(request.urlRedirecionamento()).append("'");

            virgula = true;

        }

        if(urlImagem != null && urlImagem.length() > 0){

            sb.append(virgula ? ",\n" : "").append("url_imagem = '").append(urlImagem).append("'");

        }

        return sb.toString();

    }

    public static final String modificaColetaLocaisRequest(ColetaLocaisRequest request){

        StringBuilder sb = new StringBuilder();

        sb.append("notas_quantidade > ").append(request.quantidadeReviews()).append("\n");

        if(request.tipo() != null){

            sb.append(" AND tipo").append(" = ").append(request.tipo()).append("\n");

        }

        if(request.query() != null){

            sb.append("AND (");

            for(String query : request.query()){

                sb.append(" ( nome LIKE '%").append(query).append("%'\n");
                sb.append(" OR JSON_SEARCH(cozinha, 'all', '%").append(query).append("')) OR");

            }

            sb.replace(sb.length() - 2, sb.length(), "");

            sb.append(")");

        }

        if(request.periodoDto() != null){

            sb.append("AND( ( JSON_EXTRACT(dias, '$[0].abre') < '").append(request.periodoDto().inicio()).append("' AND") 
               .append(" JSON_EXTRACT(dias, '$[0].fecha') > '").append(request.periodoDto().fim()).append("')\n OR");

            sb.append("( JSON_EXTRACT(dias, '$[1].abre') < '").append(request.periodoDto().inicio()).append("' AND") 
               .append(" JSON_EXTRACT(dias, '$[1].fecha') > '").append(request.periodoDto().fim()).append("')\n OR");

            sb.append("( JSON_EXTRACT(dias, '$[2].abre') < '").append(request.periodoDto().inicio()).append("' AND") 
               .append(" JSON_EXTRACT(dias, '$[2].fecha') > '").append(request.periodoDto().fim()).append("')\n OR");

            sb.append("( JSON_EXTRACT(dias, '$[3].abre') < '").append(request.periodoDto().inicio()).append("' AND") 
               .append(" JSON_EXTRACT(dias, '$[3].fecha') > '").append(request.periodoDto().fim()).append("')\n OR");

            sb.append("( JSON_EXTRACT(dias, '$[4].abre') < '").append(request.periodoDto().inicio()).append("' AND") 
               .append(" JSON_EXTRACT(dias, '$[4].fecha') > '").append(request.periodoDto().fim()).append("')\n OR");

            sb.append("( JSON_EXTRACT(dias, '$[5].abre') < '").append(request.periodoDto().inicio()).append("' AND") 
               .append(" JSON_EXTRACT(dias, '$[5].fecha') > '").append(request.periodoDto().fim()).append("')\n OR");

            sb.append("( JSON_EXTRACT(dias, '$[6].abre') < '").append(request.periodoDto().inicio()).append("' AND") 
               .append(" JSON_EXTRACT(dias, '$[6].fecha') > '").append(request.periodoDto().fim()).append("'))");

        }

        return sb.toString();

    }
    
}
