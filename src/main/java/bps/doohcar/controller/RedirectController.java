package bps.doohcar.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bps.doohcar.repositories.mysql.LocaisRepository;
import bps.doohcar.repositories.mysql.PropagandaRepository;
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

    @GetMapping("/redireciona")
    public void redirect(HttpServletResponse response, @RequestParam(required = true) Boolean propaganda, @RequestParam(required = true) Long id ) throws IOException{

        String url = null;

        if(propaganda){

            url = propagandaRepository.coletaUrl(id); 

        } else {

            url = locaisRepository.coletaUrl(id);

        }

        response.sendRedirect(url);

    }
    
}
