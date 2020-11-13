package com.beerhouse.api;

import com.beerhouse.domain.Beer;
import com.beerhouse.entity.IEntity;
import com.beerhouse.entity.IPage;
import com.beerhouse.entity.ISort;
import com.beerhouse.service.dto.BeerCriteria;
import io.swagger.annotations.*;

import java.util.List;

/**
 * Especificação da API de acesso ao cadastro de {@link Beer}.
 * O Swagger foi utilizado aqui para auxiliar na especificação da API.
 * Esta especificação foi desacoplada de frameworks.
 * Para implementar essa interface deve-se ter realizado {@link IEntity} e {@link IPage}.
 */
@Api(value = "beers")
public interface BeerAPI<P extends IPage, O extends ISort> {

    /**
     * Listar {@link Beer}.
     *
     * @param filtro argumento opcional para filtrar o resultado por criterios.
     * @param pagina argumento opcional para ordenar e paginar o resultado.
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
            @ApiParam(value = "Parametro para paginar o resultado") P pagina,
            @ApiParam(value = "Parametro para ordenar o resultado") O ordem,
            @ApiParam(value = "Criterio para filtrar o resultado") BeerCriteria filtro
    );

}

