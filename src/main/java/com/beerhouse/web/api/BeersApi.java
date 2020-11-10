package com.beerhouse.web.api;

import com.beerhouse.entity.Beer;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.List;

@Api(value = "beers")
@RequestMapping(value = "")
public interface BeersApi {

    @ApiOperation(value = "", nickname = "beersGet", notes = "", response = Beer.class, responseContainer = "List", tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Status 200", response = Beer.class, responseContainer = "List") })
    @RequestMapping(value = "/beers",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.GET)
    ResponseEntity<List<Beer>> beersGet();


    @ApiOperation(value = "", nickname = "beersIdDelete", notes = "", tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Status 204") })
    @RequestMapping(value = "/beers/{id}",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.DELETE)
    ResponseEntity<Void> beersIdDelete(@ApiParam(value = "",required=true) @PathVariable("id") String id);


    @ApiOperation(value = "", nickname = "beersIdGet", notes = "", response = Beer.class, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Status 200", response = Beer.class) })
    @RequestMapping(value = "/beers/{id}",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.GET)
    ResponseEntity<Beer> beersIdGet(@ApiParam(value = "",required=true) @PathVariable("id") String id);


    @ApiOperation(value = "", nickname = "beersIdPatch", notes = "", tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Status 200") })
    @RequestMapping(value = "/beers/{id}",
        produces = { "application/json" }, 
        method = RequestMethod.PATCH)
    ResponseEntity<Void> beersIdPatch(@ApiParam(value = "",required=true) @PathVariable("id") String id,@ApiParam(value = "" ,required=true )  @Valid @RequestBody Beer body);


    @ApiOperation(value = "", nickname = "beersIdPut", notes = "", tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Status 200") })
    @RequestMapping(value = "/beers/{id}",
        produces = { "application/json" }, 
        method = RequestMethod.PUT)
    ResponseEntity<Void> beersIdPut(@ApiParam(value = "",required=true) @PathVariable("id") String id,@ApiParam(value = "" ,required=true )  @Valid @RequestBody Beer body);


    @ApiOperation(value = "", nickname = "beersPost", notes = "", tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Status 201") })
    @RequestMapping(value = "/beers",
        produces = { "application/json" }, 
        method = RequestMethod.POST)
    ResponseEntity<Void> beersPost(@ApiParam(value = "" ,required=true )  @Valid @RequestBody Beer body);

}
