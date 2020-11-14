package com.beerhouse;

import com.beerhouse.domain.Beer;
import com.beerhouse.service.BeerService;
import com.beerhouse.service.dto.BeerDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.hamcrest.core.AnyOf.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "test")
public class ApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BeerService beerService;

    private static final Integer id = 1;
    private static final String name = "ABC sadf CCC asd";
    private static final String alcoholContent = "asfd asfdrSKDJJKS SDFFDS";
    private static final String category = "ABSD sdf SAD";
    private static final String ingredients = "asf;asd f;asg ;adgfa;gdfsgg;as dg;sdafgerd";
    private static final BigDecimal price = BigDecimal.valueOf(122.987);

    private static final Integer id_invalid_a = -1;
    private static final Integer id_invalid_b = null;
    private static final String name_invalid_a = null;
    private static final String name_invalid_b = "a";
    private static final String name_invalid_c = " ";
    private static final String alcoholContent_invalid = "12345678901234567890123456789012345678901234567890-";
    private static final String category_invalid = "12345678901234567890123456789012345678901234567890-";
    private static final String ingredients_invalid = "12345678901234567890123456789012345678901234567890-";
    private static final BigDecimal price_invalid_a = null;
    private static final BigDecimal price_invalid_b = BigDecimal.valueOf(-1);

    private static final BeerDTO cerveja = new BeerDTO()
            .id(null)
            .name(name)
            .alcoholContent(alcoholContent)
            .category(category)
            .ingredients(ingredients)
            .price(price);

    private static final BeerDTO cerveja_com_id = cerveja().id(id).name("Nome1");
    private static final BeerDTO cerveja_com_id_invalido_a = cerveja().id(id_invalid_a).name("Nome2");
    private static final BeerDTO cerveja_com_id_invalido_b = cerveja().id(id_invalid_b).name("Nome3");
    private static final BeerDTO cerveja_com_name_invalido_a = cerveja().name(name_invalid_a);
    private static final BeerDTO cerveja_com_name_invalido_b = cerveja().name(name_invalid_b);
    private static final BeerDTO cerveja_com_name_invalido_c = cerveja().name(name_invalid_c);
    private static final BeerDTO cerveja_com_alcoholContent_invalid = cerveja()
            .alcoholContent(alcoholContent_invalid).name("Nome4");
    private static final BeerDTO cerveja_com_category_invalid = cerveja().category(category_invalid).name("Nome5");
    private static final BeerDTO cerveja_com_ingredients_invalid = cerveja()
            .ingredients(ingredients_invalid).name("Nome6");
    private static final BeerDTO cerveja_com_price_invalido_a = cerveja().price(price_invalid_a).name("Nome7");
    private static final BeerDTO cerveja_com_price_invalido_b = cerveja().price(price_invalid_b).name("Nome8");

    private static final List<BeerDTO> lista_de_cervejas = Arrays.asList(
            cerveja().name("DDDDD").price(BigDecimal.valueOf(4)),
            cerveja().name("AAAAA").price(BigDecimal.valueOf(3)),
            cerveja().name("CCCCC").price(BigDecimal.valueOf(1)),
            cerveja().name("BBBBB").price(BigDecimal.valueOf(2))
    );

    @Test
    void cadastrarCervejaCorretamente_then_Ok() throws Exception {
        mockMvc.perform(post("/beers")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(cerveja.id(null).name("Novo nome"))))
                .andExpect(status().isCreated());

        Beer beer = beerService.save(cerveja().id(null).name("novo nome").toBeer());
        Assertions.assertNotNull(beer);
        Assertions.assertNotNull(beer.getId());
    }

    @Test
    void cadastrarCervejaComNome2X_then_Error() throws Exception {
        Beer beer = beerService.save(cerveja.toBeer());
        Assertions.assertNotNull(beer);
        Assertions.assertNotNull(beer.getId());

        mockMvc.perform(post("/beers")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(cerveja)))
                .andExpect(status().isBadRequest());

    }

    @Test
    void cadastrarCervejaComId_then_Error() throws Exception {
        mockMvc.perform(post("/beers")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(cerveja_com_id.toBeer())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void cadastrarCervejaComCampoInvalido_then_Error() throws Exception {
//        for (BeerDTO cerveja_invalida :
//                Arrays.asList(
//                        cerveja_com_name_invalido_a, cerveja_com_name_invalido_b, cerveja_com_name_invalido_c,
//                        cerveja_com_alcoholContent_invalid,
//                        cerveja_com_category_invalid,
//                        cerveja_com_ingredients_invalid,
//                        cerveja_com_price_invalido_a, cerveja_com_price_invalido_b
//                )) {
//            mockMvc.perform(post("/beers")
//                    .contentType("application/json")
//                    .content(objectMapper.writeValueAsString(cerveja_invalida)))
//                    .andExpect(anyOf(status().isInternalServerError(), status().isBadRequest()));
//        }
    }

    @Test
    void listarCervejas() throws Exception {
    }

    @Test
    void contabilizarCervejas() throws Exception {
    }

    @Test
    void contabilizarByCriterioCervejas() throws Exception {
    }

    @Test
    void paginarListaCervejas() throws Exception {
    }

    @Test
    void ordenarListaCervejas() throws Exception {
    }

    @Test
    void paginarEOrdenarCervejas() throws Exception {
    }

    @Test
    void atualizarCervejaCorretamente_then_Ok() throws Exception {
    }

    @Test
    void atualizarCervejaInexistente_then_Error() throws Exception {
    }

    @Test
    void atualizarCerveja() throws Exception {
    }

    @Test
    void atualizarPrecoCorretamente_then_Ok() {
    }

    @Test
    void removerCervejaCadastrada_then_Ok() throws Exception {
    }

    @Test
    void removerCervejaInvalida_then_Error() throws Exception {
    }


    private static final BeerDTO cerveja(BeerDTO beerDTO) {
        return new BeerDTO(beerDTO.toBeer());
    }

    private static final BeerDTO cerveja() {
        return new BeerDTO(cerveja.toBeer());
    }


}