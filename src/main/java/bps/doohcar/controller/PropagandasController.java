package bps.doohcar.controller;

import bps.doohcar.dtos.propagandas.geral.PropagandaDto;
import bps.doohcar.dtos.propagandas.requests.AlteraPropagandaRequest;
import bps.doohcar.dtos.propagandas.requests.ColetaPropagandaRequest;
import bps.doohcar.dtos.propagandas.responses.ColetaPropagandaResponse;
import bps.doohcar.dtos.propagandas.requests.ExcluiPropagandaRequest;

import bps.doohcar.dtos.propagandas.requests.CriaPropagandaRequest;
import bps.doohcar.dtos.propagandas.responses.CriaPropagandaResponse;
import bps.doohcar.dtos.ResponseObject;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import bps.doohcar.repositories.mysql.PropagandaRepository;
import bps.doohcar.utlis.ImagemUtils;

@RestController
@RequestMapping("/api/v1/dooh-car/propagandas")
@Tag(
    name = "APIs DOOH-CAR: PROPAGANDAS",
    description = "CONTÉM TODOS OS ENDPOINTS RELACIONADOS A PROPAGANDAS"
)
public class PropagandasController {

    @Autowired
    private PropagandaRepository propagandaRepository;

    @Value("${key}")
    private String key;

    @Value("${url.nginx}")
    private String urlNginx;

    @PutMapping("/altera")
    @Operation(
        summary = "API UTILIZADA PARA A ALTERAÇÃO DE PROPAGANDAS",
        responses = {
            @ApiResponse(
                responseCode = "400",
                content = @Content(
                    schema = @Schema(implementation = ResponseObject.class)
                )
            ),
            @ApiResponse(
                responseCode = "404",
                content = @Content(
                    schema = @Schema(implementation = ResponseObject.class)
                )
            ),
            @ApiResponse(
                responseCode = "200",
                content = @Content(
                    schema = @Schema(implementation = CriaPropagandaResponse.class)
                )
            )
        }
    )
    public ResponseEntity<Object> alteraPropagandas(@RequestBody AlteraPropagandaRequest request, @RequestHeader("key") String key){

        if(key == null || !Pattern.matches(this.key, key)){

            return ResponseObject.error("Chave não autorizada ou null", HttpStatus.UNAUTHORIZED);

        }

        ResponseEntity<Object> validate = request.validate();

        if(validate == null){

            return validate;

        }


        PropagandaDto propagandaDto = propagandaRepository.coletaPropaganda(request.id());

        if(propagandaDto == null){

            return ResponseObject.error("Esta propaganda não existe", HttpStatus.NOT_FOUND);

        }

        String nome = request.titulo() == null ? 
            LocalDateTime.now(ZoneOffset.UTC).toString()
            :
            request.titulo() + "_" + LocalDateTime.now(ZoneOffset.UTC)
        ;

        String urlImagem = null;

        if(request.imagem() != null){

            urlImagem = ImagemUtils.criaImagem(urlNginx, request.imagem(), nome);

        }

        String urlVideo = null;

        if(request.urlVideo() != null){

            urlVideo = "https://drive.google.com/uc?export=download&id=" + request.urlVideo().split("/")[5];

        }

        propagandaRepository.alteraAnuncio(request, urlImagem, urlVideo, nome);

        return ResponseObject.success("Propaganda alterada com sucesso", HttpStatus.OK);

    }

    @DeleteMapping("/exclui")
    @Operation(
        summary = "API UTILIZADA PARA A REMOÇÃO DE PROPAGANDAS",
        responses = {
            @ApiResponse(
                responseCode = "400",
                content = @Content(
                    schema = @Schema(implementation = ResponseObject.class)
                )
            ),
            @ApiResponse(
                responseCode = "404",
                content = @Content(
                    schema = @Schema(implementation = ResponseObject.class)
                )
            ),
            @ApiResponse(
                responseCode = "200",
                content = @Content(
                    schema = @Schema(implementation = CriaPropagandaResponse.class)
                )
            )
        }
    )
    public ResponseEntity<Object> excluiPropagadas(@RequestBody ExcluiPropagandaRequest request, @RequestHeader("key") String key){

        if(key == null || !Pattern.matches(this.key, key)){

            return ResponseObject.error("Chave não autorizada ou null", HttpStatus.UNAUTHORIZED);

        }

        ResponseEntity<Object> validate = request.validate();

        if(validate != null){

            return validate;

        }

        PropagandaDto propagandaDto = propagandaRepository.coletaPropaganda(request.id());

        if(propagandaDto == null){

            return ResponseObject.error("Propaganda não existe", HttpStatus.NOT_FOUND);

        }

        propagandaRepository.excluiPropaganda(request.id());

        return ResponseObject.success("Propaganda deletada com sucesso", HttpStatus.OK);

    }

    @PostMapping("/cria")
    @Operation(
        summary = "API UTILIZADA PARA CRIAR PROPAGANDAS",
        responses = {
            @ApiResponse(
                responseCode = "400",
                content = @Content(
                    schema = @Schema(implementation = ResponseObject.class)
                )
            ),
            @ApiResponse(
                responseCode = "409",
                content = @Content(
                    schema = @Schema(implementation = ResponseObject.class)
                )
            ),
            @ApiResponse(
                responseCode = "507",
                content = @Content(
                    schema = @Schema(implementation = ResponseObject.class)
                )
            ),
            @ApiResponse(
                responseCode = "500",
                content = @Content(
                    schema = @Schema(implementation = ResponseObject.class)
                )
            ),
            @ApiResponse(
                responseCode = "201",
                content = @Content(
                    schema = @Schema(implementation = CriaPropagandaResponse.class)
                )
            )
        }
    )
    public ResponseEntity<Object> criaPropagandas(@RequestBody CriaPropagandaRequest request, @RequestHeader("key") String key){

        if(key == null || !Pattern.matches(this.key, key)){

            return ResponseObject.error("Chave não autorizada ou null", HttpStatus.UNAUTHORIZED);

        }

        ResponseEntity<Object> validate = request.validate();

        if(validate != null){

            return validate;

        }

        if(request.imagem() != null){

            boolean verificaSeOLocalDaImagemEstaDisponivel = propagandaRepository.verificaSeOLocalDaImagemEstaDisponivel(request.telaDeDisplay());

            if(!verificaSeOLocalDaImagemEstaDisponivel){

                return ResponseObject.error("Esta tela ja possui uma propaganda, coloque ela em outra tela ou remova a imagem existente", HttpStatus.CONFLICT);

            }

        }

        String nome = request.titulo() == null ? 
            LocalDateTime.now(ZoneOffset.UTC).toString()
            :
            request.titulo() + "_" + LocalDateTime.now(ZoneOffset.UTC)
        ;

        String urlImagem = null;

        if(request.imagem() != null){

            urlImagem = ImagemUtils.criaImagem(urlNginx, request.imagem(), nome);

        }

        String urlVideo = null;

        if(request.urlVideo() != null){

            urlVideo = "https://drive.google.com/uc?export=download&id=" + request.urlVideo().split("/")[5];

        }

        Long id = propagandaRepository.criaPropagada(request, urlImagem, urlVideo, nome);

        if(id == null || id == 0){

            return ResponseObject.error("Erro ao guardar a propaganda no banco de dados", HttpStatus.INTERNAL_SERVER_ERROR);

        }

        return CriaPropagandaResponse.answer(id);

    }

    @PostMapping("/coleta")
    @Operation(
        summary = "API UTILIZADA PARA A COLETA DE PROPAGANDAS",
        responses = {
            @ApiResponse(
                responseCode = "400",
                content = @Content(
                    schema = @Schema(implementation = ResponseObject.class)
                )
            ),
            @ApiResponse(
                responseCode = "404",
                content = @Content(
                    schema = @Schema(implementation = ResponseObject.class)
                )
            ),
            @ApiResponse(
                responseCode = "200",
                content = @Content(
                    schema = @Schema(implementation = ColetaPropagandaResponse.class)
                )
            )
        }
    )
    public ResponseEntity<Object> coletaPropagandas(@RequestBody ColetaPropagandaRequest request, @RequestHeader("key") String key){

        if(key == null || !Pattern.matches(this.key, key)){

            return ResponseObject.error("Chave não autorizada ou null", HttpStatus.UNAUTHORIZED);

        }

        ResponseEntity<Object> validate = request.validate();

        if(validate != null){

            return validate;

        }

        Map<Integer, PropagandaDto> map = new HashMap<>();
        List<PropagandaDto> videos = new ArrayList<>();

        if(request.id() != null){

            PropagandaDto propagandaDto = propagandaRepository.coletaPropaganda(request.id(), key);

            map.put(propagandaDto.telaDeDisplay(), propagandaDto);

        } else if(request.tela() != null){

            PropagandaDto propagandaDto = propagandaRepository.coletaPropagandaComTela(request.tela(), key);

            map.put(propagandaDto.telaDeDisplay(), propagandaDto);

        } else if(request.limit() != null && request.offset() != null){

            List<PropagandaDto> propagandas = propagandaRepository.coletaPropagandas(request.limit(), request.offset(), key);

            for(PropagandaDto propagandaDto : propagandas){

                if(propagandaDto.telaDeDisplay() == 0){

                    videos.add(propagandaDto);

                } else {

                    map.put(propagandaDto.telaDeDisplay(), propagandaDto);

                }

            }

        }

        if((map == null || map.size() < 1) && (videos == null || videos.size() < 1)){

            return ResponseObject.error("Nenhuma propaganda encontrada", HttpStatus.NOT_FOUND);

        }

        return ColetaPropagandaResponse.answer(map, videos);

    }
    
}
