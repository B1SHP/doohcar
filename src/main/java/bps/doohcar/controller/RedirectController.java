package bps.doohcar.controller;

import java.io.IOException;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bps.doohcar.enums.TipoUrl;
import bps.doohcar.repositories.mysql.EventosRepository;
import bps.doohcar.repositories.mysql.LocaisRepository;
import bps.doohcar.repositories.mysql.PropagandaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1/dooh-car/redirect")
@Tag(
    name = "APIs DOOH-CAR: REDIRECIONAMENTO",
    description = "CONTÉM TODOS OS ENDPOINTS COM REDIRECIONAMENTO"
)
public class RedirectController {

    @Autowired
    private LocaisRepository locaisRepository;

    @Autowired
    private PropagandaRepository propagandaRepository;

    @Autowired
    private EventosRepository eventoRepository;

    @Value("${key}")
    private String key;

    @GetMapping("/redireciona")
    @Operation(summary = "API UTILIZADA PARA O REDIRECIONAMENTO DE URLS", description = "Tipos: 1 -> Propaganda, 2 -> Locais, 3 -> Eventos")
    public void redirect(
        HttpServletResponse response, 
        @RequestParam(required = true) Integer tipo, 
        @RequestParam(required = true) Long id, 
        @RequestParam(required = true) String key, 
        @RequestParam(required = false) String url, 
        @RequestParam(required = false) String nome
        ) throws IOException{

        if(key == null || !Pattern.matches(this.key, key)){

            return;

        }

        String urlFinal = null;

        if(TipoUrl.PROPAGANDA.key == tipo){

            urlFinal = propagandaRepository.coletaUrl(id); 

            propagandaRepository.aumentaContagem(id);

        } else if(TipoUrl.LOCAL.key == tipo){

            urlFinal = locaisRepository.coletaUrl(id);

            locaisRepository.aumentaContagem(id);

        } else if(TipoUrl.EVENTO.key == tipo){

            long contagem = eventoRepository.coleta(id);

            if(contagem == 0){

                eventoRepository.cria(id, nome);

            } else {

                eventoRepository.aumentaContagem(id);

            }

            urlFinal = url;

        }

        response.sendRedirect(urlFinal);

    }
    
}
