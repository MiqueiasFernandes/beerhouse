package com.beerhouse.api;

import com.beerhouse.domain.Beer;
import com.beerhouse.entity.IEntity;
import com.beerhouse.entity.IPage;
import com.beerhouse.entity.ISort;
import com.beerhouse.service.dto.BeerCriteria;
import com.beerhouse.service.dto.BeerDTO;
import io.swagger.annotations.*;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Especificação da API de acesso ao cadastro de {@link Beer}.
 * O Swagger foi utilizado aqui para auxiliar na especificação da API.
 * Esta especificação foi desacoplada de frameworks.
 * Para implementar essa interface deve-se realizar {@link IEntity}, {@link IPage} e {@link ISort}.
 */
@Api(value = "beers")
public interface BeerAPI<P extends IPage, S extends ISort> {

    /**
     * Listar {@link Beer}.
     *
     * @param page argumento opcional para paginar o resultado.
     * @param sort argumento opcional para ordenar o resultado.
     * @param criteria argumento opcional para filtrar o resultado por criterios.
     * @return uma {@link IEntity} com uma {@link List} de Beer.
     */
    @ApiOperation(
            nickname = "getAllBeers",
            response = Beer.class,
            responseContainer = "List",
            value = "Listar cervejas.")
    @ApiResponses(value = {@ApiResponse(
            code = 200,
            message = "Consulta realizada com sucesso.",
            response = Beer.class,
            responseContainer = "List")})
    IEntity<List<Beer>> getBeers(
            @ApiParam(value = "Parametro para paginar o resultado") P page,
            @ApiParam(value = "Parametro para ordenar o resultado") S sort,
            @ApiParam(value = "Criterio para filtrar o resultado") BeerCriteria criteria
    );

    /**
     * Contabilizar {@link Beer} cadastradas.
     * Esste ponto de acesso é util para contabilizar as cervejas a ser obtidas a partir de um criterio de consulta.
     *
     * @param criteria argumento opcional para determinar um criterio de filtro de resultados a contabilizar.
     * @return a {@link IEntity<Long>}.
     */
    @ApiOperation(nickname = "countBeers", response = Long.class, value = "Contabilizar cervejas.")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Contabilizado com sucesso.", response = Long.class)})
    IEntity<Long> countBeers(
            @ApiParam(value = "Criterio para filtrar o resultado") BeerCriteria criteria
    );

    /**
     * Obter a {@link Beer} pelo seu identificador "id".
     *
     * @param id um {@link Integer} que é o Id da cerveja requisitada.
     * @return uma {@link IEntity} do tipo {@link Beer}.
     */
    @ApiOperation(nickname = "getBeerById", response = Beer.class, value = "Consultar cerveja pelo Id.")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Cerveja consultada com sucesso.", response = Beer.class)})
    IEntity<Beer> getBeer(
            @ApiParam(value = "Beer Id", required = true) Integer id
    );

    /**
     * Cadastrar uma nova {@link Beer}.
     *
     * @param beer uma entidade de {@link Beer} para cadastrar, sem o campo Id preenchido.
     * @return a {@link IEntity} do tipo {@link Beer} que foi cadastrada.
     */
    @ApiOperation(
            nickname = "postBeer",
            value = "Cadastrar uma nova cerveja.",
            notes = "A entidade a ser cadastrada não pode possuir um valor no campo Id.")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Cerveja cadastrada com sucesso.", response = Beer.class)})
    IEntity<BeerDTO> postBeer(
            @ApiParam(value = "Entidade cerveja a ser cadastrada", required = true) BeerDTO beer
    ) throws URISyntaxException; // Necessario para caso corra erros ao preparar a resposta 201

    /**
     * Editar o cadastro de uma {@link Beer}.
     *
     * @param beer uma entidade de {@link Beer} cadastrada para atualizar.
     * @return a {@link IEntity} do tipo {@link Beer} atualizada.
     */
    @ApiOperation(nickname = "putBeer", value = "Atualizar cerveja cadastrada.")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Cadastro atualizado com sucesso.")})
    IEntity<BeerDTO> putBeer(
            @ApiParam(value = "Cadastro da cerveja com os campos atualizados", required = true) BeerDTO beer
    );

    /**
     * Alterar o preço de uma {@link Beer}.
     *
     * @param id    um {@link Integer} que é o Id da cerveja para atualizar o preço.
     * @param price um {@link BigDecimal} novo preço da cerveja.
     * @return a {@link IEntity} do tipo {@link Beer} com o preço atualizado.
     */
    @ApiOperation(nickname = "patchBeer", value = "Atualizar preço de uma cerveja cadastrada.")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Preço atualizado com sucesso.")})
    IEntity<BeerDTO> patchBeer(
            @ApiParam(value = "Id da cerveja a ser atualizada", required = true) Integer id,
            @ApiParam(value = "Novo preço da cerveja", required = true) BigDecimal price
    );

    /**
     * Remover uma {@link Beer} do banco de dados.
     *
     * @param id o identificador Id da cerveja a ser removida.
     * @return uma {@link IEntity} com o corpo vazio.
     */
    @ApiOperation(value = "Remover cerveja", nickname = "beersIdDelete")
    @ApiResponses(value = {@ApiResponse(code = 204, message = "Removido com sucesso.")})
    IEntity<Void> deleteBeer(
            @ApiParam(value = "Id da cerveja a ser removida", required = true) Integer id
    );

}
