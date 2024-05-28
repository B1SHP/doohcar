package bps.doohcar.controller;

import bps.doohcar.dtos.propagandas.geral.PropagandaDto;
import bps.doohcar.dtos.propagandas.requests.ColetaPropagandaRequest;
//import bps.doohcar.dtos.propagandas.requests.AlteraPropagandaRequest;
import bps.doohcar.dtos.propagandas.responses.ColetaPropagandaResponse;
import bps.doohcar.dtos.propagandas.requests.ExcluiPropagandaRequest;

import bps.doohcar.dtos.propagandas.requests.CriaPropagandaRequest;
import bps.doohcar.dtos.propagandas.responses.CriaPropagandaResponse;
import bps.doohcar.dtos.ResponseObject;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import bps.doohcar.repositories.mysql.PropagandaRepository;

@RestController
@RequestMapping("/api/v1/dooh-car/propagandas")
@Tag(
    name = "APIs DOOH-CAR: PROPAGANDAS",
    description = "CONTÉM TODOS OS ENDPOINTS RELACIONADOS A PROPAGANDAS"
)
public class PropagandasController {

    @Autowired
    private PropagandaRepository propagandaRepository;

//    @PutMapping("/altera")
//    public ResponseEntity<Object> alteraPropagandas(@RequestBody AlteraPropagandaRequest request){
//
//    }

    @DeleteMapping("/exclui")
    public ResponseEntity<Object> excluiPropagadas(@RequestBody ExcluiPropagandaRequest request){

        ResponseEntity<Object> validate = request.validate();

        if(validate != null){

            return validate;

        }

        PropagandaDto propagandaDto = propagandaRepository.coletaPropaganda(request.id());

        if(propagandaDto == null){

            return ResponseObject.error("Propaganda não existe", HttpStatus.NOT_FOUND);

        }

        propagandaRepository.excluiPropaganda(request.id());

        return ResponseObject.success("Propaganda alterada com sucesso", HttpStatus.OK);

    }

    @PostMapping("/cria")
    public ResponseEntity<Object> criaPropagandas(@RequestBody CriaPropagandaRequest request){

        ResponseEntity<Object> validate = request.validate();

        if(validate != null){

            return validate;

        }

        Long id = propagandaRepository.criaPropagada(request);

        if(id == null || id == 0){

            return ResponseObject.error("Erro ao guardar a propaganda no banco de dados", HttpStatus.INTERNAL_SERVER_ERROR);

        }

        return CriaPropagandaResponse.answer(id);

    }

    @PostMapping("/coleta")
    public ResponseEntity<Object> coletaPropagandas(@RequestBody ColetaPropagandaRequest request){

        ResponseEntity<Object> validate = request.validate();

        if(validate != null){

            return validate;

        }

        List<PropagandaDto> propagandas = new ArrayList<>();
        Long quantidade = null;

        if(request.id() != null){

            PropagandaDto propagandaDto = propagandaRepository.coletaPropaganda(request.id());

            if(propagandaDto != null){

                propagandas.add(propagandaDto);

            }

        } else if(request.limit() != null && request.offset() != null){

            propagandas.addAll(
                propagandaRepository.coletaPropagandas(request.limit(), request.offset())
            );

            quantidade = propagandaRepository.contaPropagandas();

        }

        if(propagandas.size() == 0){

            return ResponseObject.error("Nenhuma propaganda encontrada", HttpStatus.NOT_FOUND);

        }

        return ColetaPropagandaResponse.answer(propagandas, quantidade);

    }
    
}
