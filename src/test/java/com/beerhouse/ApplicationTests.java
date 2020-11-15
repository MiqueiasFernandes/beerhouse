package com.beerhouse;

import com.beerhouse.domain.Beer;
import com.beerhouse.service.BeerService;
import com.beerhouse.service.dto.BeerDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static com.beerhouse.entity.ISort.SEPARADOR;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "test")
public class ApplicationTests {

    private static final Integer id = 1;
    private static final String name = "ABC sadf CCC asd";
    private static final String alcoholContent = "asfd asfdrSKDJJKS SDFFDS";
    private static final String category = "ABSD sdf SAD";
    private static final String ingredients = "asf;asd f;asg ;adgfa;gdfsgg;as dg;sdafgerd";
    private static final Double price = 122.987;
    private static final Integer id_invalid_a = null;
    private static final Integer id_invalid_b = -1;
    private static final Integer id_invalid_c = 999999999;
    private static final String name_invalid_a = null;
    private static final String name_invalid_b = "a";
    private static final String name_invalid_c = " ";
    private static final String name_invalid_d = RandomStringUtils.randomAlphanumeric(101);
    private static final String alcoholContent_invalid = "12345678901234567890123456789012345678901234567890-";
    private static final String category_invalid = "12345678901234567890123456789012345678901234567890-";
    private static final String ingredients_invalid = RandomStringUtils.randomAlphanumeric(1001);
    private static final Double price_invalid_a = null;
    private static final Double price_invalid_b = -1.0;
    private static final BeerDTO cerveja = new BeerDTO()
            .id(null)
            .name(name)
            .alcoholContent(alcoholContent)
            .category(category)
            .ingredients(ingredients)
            .price(price);
    private static int numbers = 1;
    private static final BeerDTO cerveja_com_id = cerveja().id(id);
    private static final BeerDTO cerveja_com_id_invalido_a = cerveja().id(id_invalid_a);
    private static final BeerDTO cerveja_com_id_invalido_b = cerveja().id(id_invalid_b);
    private static final BeerDTO cerveja_com_name_invalido_a = cerveja().name(name_invalid_a);
    private static final BeerDTO cerveja_com_name_invalido_b = cerveja().name(name_invalid_b);
    private static final BeerDTO cerveja_com_name_invalido_c = cerveja().name(name_invalid_c);
    private static final BeerDTO cerveja_com_name_invalido_d = cerveja().name(name_invalid_d);
    private static final BeerDTO cerveja_com_alcoholContent_invalid = cerveja()
            .alcoholContent(alcoholContent_invalid);
    private static final BeerDTO cerveja_com_category_invalid = cerveja().category(category_invalid);
    private static final BeerDTO cerveja_com_ingredients_invalid = cerveja()
            .ingredients(ingredients_invalid);
    private static final BeerDTO cerveja_com_price_invalido_a = cerveja().price(price_invalid_a);
    private static final BeerDTO cerveja_com_price_invalido_b = cerveja().price(price_invalid_b);
    private static final List<BeerDTO> lista_de_cervejas = Arrays.asList(
            cerveja().name("DDDDD").price(4.0),
            cerveja().name("AAAAA").price(3.0),
            cerveja().name("CCCCC").price(1.0),
            cerveja().name("BBBBB").price(2.0)
    );
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BeerService beerService;

    private static final BeerDTO cerveja() {
        return new BeerDTO(cerveja.name("nome" + numbers++).toBeer());
    }

    private static final Beer cervejaOk() {
        return cerveja().toBeer();
    }

    void clearDB() throws Exception {
        //Removendo cervejas cadastradas
        mockMvc.perform(get("/beers")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(mvcResult -> {
                    Beer[] beers = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Beer[].class);
                    for (Beer beer : beers) {
                        beerService.delete(beer.getId());
                    }
                });
    }

    @Test
    void cadastrarCervejaCorretamente_then_Ok() throws Exception {
        Beer toSave = cervejaOk();

        mockMvc.perform(post("/beers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(toSave)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", instanceOf(Integer.TYPE)))
                .andExpect(jsonPath("$.name", is(toSave.getName())))
                .andExpect(jsonPath("$.alcoholContent", is(toSave.getAlcoholContent())))
                .andExpect(jsonPath("$.category", is(toSave.getCategory())))
                .andExpect(jsonPath("$.ingredients", is(toSave.getIngredients())))
                .andExpect(jsonPath("$.price", is(toSave.getPrice())));

        toSave = cervejaOk();
        Beer beer = beerService.save(toSave);
        Assertions.assertNotNull(beer.getId());
        Assertions.assertEquals(beer, toSave);
    }

    @Test
    void cadastrarCervejaComNome2X_then_Error() throws Exception {
        Beer cerverja_para_recadastrar = cervejaOk();

        //Cadastrar a cerveja noramalmente
        beerService.save(cerverja_para_recadastrar);

        //Quando tentar cadastrar de novo
        cerverja_para_recadastrar.setId(null);
        mockMvc.perform(post("/beers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cerverja_para_recadastrar)))
                //Então recebe error
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title", is("Nome de cerveja ja existe")))
                .andExpect(jsonPath("$.errorKey", is("nameexists")));

        //Internamente tambem proibe nome duplicado
        cerverja_para_recadastrar.setId(null);
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> beerService.save(cerverja_para_recadastrar));
    }

    @Test
    void cadastrarCervejaComId_then_Error() throws Exception {
        mockMvc.perform(post("/beers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cerveja_com_id.toBeer())))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title", is("O Campo Id deve estar vazio")))
                .andExpect(jsonPath("$.errorKey", is("idexists")));
    }

    @Test
    void cadastrarCervejaComCampoInvalido_then_Error() throws Exception {
        List<BeerDTO> invalids = Arrays.asList(
                cerveja_com_name_invalido_a, cerveja_com_name_invalido_b,
                cerveja_com_name_invalido_c, cerveja_com_name_invalido_d,
                cerveja_com_alcoholContent_invalid,
                cerveja_com_category_invalid,
                cerveja_com_ingredients_invalid,
                cerveja_com_price_invalido_a, cerveja_com_price_invalido_b,
                new BeerDTO()
        );
        for (BeerDTO cerveja_invalida : invalids) {
            mockMvc.perform(post("/beers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(cerveja_invalida.toBeer())))
                    .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(status().is(anyOf(is(400), is(500))));
        }
    }

    @Test
    void listarCervejas() throws Exception {

        clearDB();

        Beer[] beers = new Beer[lista_de_cervejas.size()];
        int cont = 0;
        for (BeerDTO beerDTO : lista_de_cervejas) {
            beers[cont++] = beerService.save(beerDTO.toBeer());
        }

        mockMvc.perform(get("/beers")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(
                        result -> {
                            Beer[] bs = objectMapper.readValue(result.getResponse().getContentAsString(), Beer[].class);
                            Assertions.assertArrayEquals(beers, bs);

                        }
                );

    }

    @Test
    void contabilizarCervejas() throws Exception {

        clearDB();
        int size = new Random().nextInt(10) + 1;
        for (int i = 0; i < size; i++) {
            beerService.save(cervejaOk());
        }

        mockMvc.perform(get("/beers/count")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(String.valueOf(size)));

    }

    @Test
    void contabilizarByCriterioCervejas() throws Exception {

        HashMap<String, String> map = new HashMap<>();

        // name : 1
        beerService.save(cerveja().name("NAME1").toBeer());
        map.put("name", "NAME1");

        // alcoholContent : 2
        beerService.save(cerveja().alcoholContent("alcoholContent2").toBeer());
        map.put("alcoholContent", "alcoholContent2");

        // category : 3
        beerService.save(cerveja().category("category3").toBeer());
        map.put("category", "category3");

        // ingredients : 4
        beerService.save(cerveja().ingredients("ingredients4").toBeer());
        map.put("ingredients", "ingredients4");

        //price : 5
        beerService.save(cerveja().price(55.6).toBeer());
        map.put("price", "55.6");

        for (Map.Entry entry : map.entrySet()) {
            mockMvc.perform(get("/beers/count?" + entry.getKey() + ".equals=" + entry.getValue()))
                    .andExpect(status().isOk())
                    .andExpect(content().string(String.valueOf(1)));
        }

        // id > 4
        mockMvc.perform(get("/beers/count?id.greaterThan=0"))
                .andExpect(status().isOk())
                .andDo(result -> assertThat(
                        Integer.valueOf(result.getResponse().getContentAsString()),
                        greaterThan(4))
                );
    }

    @Test
    void paginarListaCervejas() throws Exception {

        clearDB();

        for (int i = 1; i <= 10; i++) {
            beerService.save(cervejaOk());
        }

        mockMvc.perform(get("/beers?page=0&size=3")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(
                        result -> {
                            Beer[] bs = objectMapper.readValue(result.getResponse().getContentAsString(), Beer[].class);
                            Assertions.assertEquals(3, bs.length);
                        }
                );
        // P0: 1 2 3 4     P1: 5 6 7 8   P2: 9 0
        mockMvc.perform(get("/beers?page=2&size=4")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(
                        result -> {
                            Beer[] bs = objectMapper.readValue(result.getResponse().getContentAsString(), Beer[].class);
                            Assertions.assertEquals(2, bs.length);
                        }
                );

        // Page is 0-based, then page 4 = []
        mockMvc.perform(get("/beers?page=3&size=4")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(
                        result -> {
                            Beer[] bs = objectMapper.readValue(result.getResponse().getContentAsString(), Beer[].class);
                            Assertions.assertEquals(0, bs.length);
                        }
                );
    }

    @Test
    void ordenarListaCervejas() throws Exception {
        clearDB();
        for (BeerDTO beerDTO : lista_de_cervejas) {
            beerService.save(beerDTO.toBeer());
        }

        mockMvc.perform(get("/beers?sort=name" + SEPARADOR + "asc")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[0].name").value(is("AAAAA")))
                .andExpect(jsonPath("$.[1].name").value(is("BBBBB")))
                .andExpect(jsonPath("$.[2].name").value(is("CCCCC")))
                .andExpect(jsonPath("$.[3].name").value(is("DDDDD")));

        mockMvc.perform(get("/beers?sort=name" + SEPARADOR + "desc")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[0].name").value(is("DDDDD")))
                .andExpect(jsonPath("$.[1].name").value(is("CCCCC")))
                .andExpect(jsonPath("$.[2].name").value(is("BBBBB")))
                .andExpect(jsonPath("$.[3].name").value(is("AAAAA")));

        mockMvc.perform(get("/beers?sort=price" + SEPARADOR + "asc")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[0].price").value(is(1.0)))
                .andExpect(jsonPath("$.[1].price").value(is(2.0)))
                .andExpect(jsonPath("$.[2].price").value(is(3.0)))
                .andExpect(jsonPath("$.[3].price").value(is(4.0)));

        mockMvc.perform(get("/beers?sort=price" + SEPARADOR + "desc")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[0].price").value(is(4.0)))
                .andExpect(jsonPath("$.[1].price").value(is(3.0)))
                .andExpect(jsonPath("$.[2].price").value(is(2.0)))
                .andExpect(jsonPath("$.[3].price").value(is(1.0)));

    }

    @Test
    void atualizarCervejaCorretamente_then_Ok() throws Exception {

        Beer toUpdate = cervejaOk();
        toUpdate = beerService.save(toUpdate);

        toUpdate.setName("Skin kariol");
        toUpdate.setIngredients("novos ingredientes");
        toUpdate.setCategory("nova categoria");
        toUpdate.setAlcoholContent("sem alcol");
        toUpdate.setPrice(235.768);

        Beer beer = beerService.update(toUpdate).orElse(null);
        Assertions.assertNotNull(beer.getId());
        Assertions.assertEquals(beer, toUpdate);

        toUpdate.setName("Skin kariol nova");
        toUpdate.setIngredients("novos ingredientes nova");
        toUpdate.setCategory("nova categoria nova");
        toUpdate.setAlcoholContent("sem alcol nova");
        toUpdate.setPrice(545158.456);

        mockMvc.perform(put("/beers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(toUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(toUpdate.getId())))
                .andExpect(jsonPath("$.name", is(toUpdate.getName())))
                .andExpect(jsonPath("$.alcoholContent", is(toUpdate.getAlcoholContent())))
                .andExpect(jsonPath("$.category", is(toUpdate.getCategory())))
                .andExpect(jsonPath("$.ingredients", is(toUpdate.getIngredients())))
                .andExpect(jsonPath("$.price", is(toUpdate.getPrice())));

    }

    @Test
    void atualizarCervejaComIdInvalido_then_Error() throws Exception {

        Beer toUpdate = cervejaOk();
        beerService.save(toUpdate);

        //Id nulo
        toUpdate.setId(id_invalid_a);
        final Beer updated_a = new BeerDTO(toUpdate).toBeer();
        Assertions.assertThrows(InvalidDataAccessApiUsageException.class, () -> beerService.update(updated_a));

        mockMvc.perform(put("/beers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated_a)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title", is("Cerveja sem id")))
                .andExpect(jsonPath("$.errorKey", is("idnull")));

        // Id negativo
        toUpdate.setId(id_invalid_b);
        final Beer updated_b = new BeerDTO(toUpdate).toBeer();
        Assertions.assertThrows(InvalidDataAccessApiUsageException.class, () -> beerService.update(updated_b));

        mockMvc.perform(put("/beers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated_b)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title", is("Id invalido " + updated_b.getId())))
                .andExpect(jsonPath("$.errorKey", is("idinvalid")));
    }

    @Test
    void atualizarCervejaInexistente_then_Error() throws Exception {

        Beer toUpdate = cervejaOk();
        beerService.save(toUpdate);

        toUpdate.setId(id_invalid_c);
        final Beer updated = new BeerDTO(toUpdate).toBeer();
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> beerService.update(updated));

        mockMvc.perform(put("/beers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(toUpdate)))
                .andExpect(status().isNotFound());
    }

    @Test
    void atualizarCervejaComCampoInvalido() {

    }

    @Test
    void atualizarCervejaNomeExistente_then_Error() throws Exception {

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


}