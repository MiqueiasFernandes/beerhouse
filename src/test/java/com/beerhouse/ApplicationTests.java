package com.beerhouse;

import com.beerhouse.domain.Beer;
import com.beerhouse.entity.dto.BeerDTO;
import com.beerhouse.service.BeerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;
import java.util.Map.Entry;

import static com.beerhouse.config.SecurityConfiguration.SENHA;
import static com.beerhouse.config.SecurityConfiguration.USUARIO;
import static com.beerhouse.entity.ISort.SEPARADOR;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(classes = Application.class)
@ActiveProfiles(profiles = "test")
@AutoConfigureMockMvc
@WithMockUser(username = USUARIO, password = SENHA)
public class ApplicationTests {

    private static final String API = "/beers";

    private static final Integer id = 1;
    private static final String name = "Craft Beer";
    private static final String alcoholContent = "baixo teor alcolico";
    private static final String category = "Artesanal";
    private static final String ingredients = "Alcool, água, cevada e malte.";
    private static final Double price = 8.5;
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
    private static final Double price_invalid_b = .0;
    private static final BeerDTO cerveja = new BeerDTO()
            .id(null)
            .name(name)
            .alcoholContent(alcoholContent)
            .category(category)
            .ingredients(ingredients)
            .price(price);
    private static int numbers = 1;
    private static final BeerDTO cerveja_com_id = cerveja().id(id);
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

    private static BeerDTO cerveja() {
        return new BeerDTO(cerveja.name("nome" + numbers++).toBeer());
    }

    private static Beer cervejaOk() {
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

        mockMvc.perform(post(API)
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
        mockMvc.perform(post(API)
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
        mockMvc.perform(post(API)
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
            mockMvc.perform(post(API)
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

        mockMvc.perform(get(API)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
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

        mockMvc.perform(get(API + "/count")
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

        for (Entry<String, String> entry : map.entrySet()) {
            mockMvc.perform(get(API + "/count?" + entry.getKey() + ".equals=" + entry.getValue()))
                    .andExpect(status().isOk())
                    .andExpect(content().string(String.valueOf(1)));
        }

        // id > 4
        mockMvc.perform(get(API + "/count?id.greaterThan=0"))
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

        mockMvc.perform(get(API + "?page=0&size=3")
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
        mockMvc.perform(get(API + "?page=2&size=4")
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
        mockMvc.perform(get(API + "?page=3&size=4")
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

        mockMvc.perform(get(API + "?sort=name" + SEPARADOR + "asc")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[0].name").value(is("AAAAA")))
                .andExpect(jsonPath("$.[1].name").value(is("BBBBB")))
                .andExpect(jsonPath("$.[2].name").value(is("CCCCC")))
                .andExpect(jsonPath("$.[3].name").value(is("DDDDD")));

        mockMvc.perform(get(API + "?sort=name" + SEPARADOR + "desc")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[0].name").value(is("DDDDD")))
                .andExpect(jsonPath("$.[1].name").value(is("CCCCC")))
                .andExpect(jsonPath("$.[2].name").value(is("BBBBB")))
                .andExpect(jsonPath("$.[3].name").value(is("AAAAA")));

        mockMvc.perform(get(API + "?sort=price" + SEPARADOR + "asc")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[0].price").value(is(1.0)))
                .andExpect(jsonPath("$.[1].price").value(is(2.0)))
                .andExpect(jsonPath("$.[2].price").value(is(3.0)))
                .andExpect(jsonPath("$.[3].price").value(is(4.0)));

        mockMvc.perform(get(API + "?sort=price" + SEPARADOR + "desc")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[0].price").value(is(4.0)))
                .andExpect(jsonPath("$.[1].price").value(is(3.0)))
                .andExpect(jsonPath("$.[2].price").value(is(2.0)))
                .andExpect(jsonPath("$.[3].price").value(is(1.0)));

    }

    @Test
    void consultarPorId_then_Ok() throws Exception {
        Beer beer = cervejaOk();
        beerService.save(beer);

        Optional<Beer> consultada = beerService.findOne(beer.getId());
        Assertions.assertTrue(consultada.isPresent());
        Assertions.assertEquals(beer, consultada.get());

        mockMvc.perform(get(API + "/" + beer.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(beer.getId())))
                .andExpect(jsonPath("$.name", is(beer.getName())))
                .andExpect(jsonPath("$.alcoholContent", is(beer.getAlcoholContent())))
                .andExpect(jsonPath("$.category", is(beer.getCategory())))
                .andExpect(jsonPath("$.ingredients", is(beer.getIngredients())))
                .andExpect(jsonPath("$.price", is(beer.getPrice())));
    }

    @Test
    void consultarPorIdErrado_then_Error() throws Exception {

        //id nulo
        Assertions.assertThrows(InvalidDataAccessApiUsageException.class, () -> beerService.findOne(id_invalid_a));
        //id nulo no GET vai para listar, id null é invalido para Integer

        //id negativo
        Assertions.assertFalse(beerService.findOne(id_invalid_b).isPresent());
        mockMvc.perform(get(API + "/" + id_invalid_b)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        //id inexistente
        Assertions.assertFalse(beerService.findOne(id_invalid_c).isPresent());
        mockMvc.perform(get(API + "/" + id_invalid_c)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

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
        Assertions.assertEquals(beer, toUpdate);

        toUpdate.setName("Skin kariol nova");
        toUpdate.setIngredients("novos ingredientes nova");
        toUpdate.setCategory("nova categoria nova");
        toUpdate.setAlcoholContent("sem alcol nova");
        toUpdate.setPrice(545158.456);

        mockMvc.perform(put(API)
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
        Assertions.assertThrows(
                InvalidDataAccessApiUsageException.class,
                () -> beerService.update(updated_a)
        );

        mockMvc.perform(put(API)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated_a)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title", is("Cerveja sem id")))
                .andExpect(jsonPath("$.errorKey", is("idnull")));

        // Id negativo
        toUpdate.setId(id_invalid_b);
        final Beer updated_b = new BeerDTO(toUpdate).toBeer();
        Assertions.assertFalse(() -> beerService.update(updated_b).isPresent());

        mockMvc.perform(put(API)
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
        Assertions.assertFalse(() -> beerService.update(updated).isPresent());

        mockMvc.perform(put(API)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(toUpdate)))
                .andExpect(status().isNotFound());
    }

    @Test
    void atualizarCervejaNomeExistente_then_Error() throws Exception {

        Beer toUpdate_a = cervejaOk();
        Beer toUpdate_b = cervejaOk();
        beerService.save(toUpdate_a);
        beerService.save(toUpdate_b);

        toUpdate_b.setName(toUpdate_a.getName());
        final Beer updated = new BeerDTO(toUpdate_b).toBeer();
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> beerService.update(updated));

        mockMvc.perform(put(API)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title", is("Nome já cadastrado para outra cerveja.")))
                .andExpect(jsonPath("$.errorKey", is("nameexistis")));
    }

    @Test
    void atualizarCervejaComCampoInvalido() throws Exception {

        Beer beer = cervejaOk();
        beerService.save(beer);

        List<BeerDTO> invalids = Arrays.asList(
                cerveja_com_name_invalido_a, cerveja_com_name_invalido_b,
                cerveja_com_name_invalido_c, cerveja_com_name_invalido_d,
                cerveja_com_alcoholContent_invalid,
                cerveja_com_category_invalid,
                cerveja_com_ingredients_invalid,
                cerveja_com_price_invalido_a, cerveja_com_price_invalido_b
        );
        for (BeerDTO cerveja_invalida : invalids) {
            Beer invalid = new BeerDTO(cerveja_invalida.toBeer()).toBeer();
            invalid.setId(beer.getId());
            mockMvc.perform(post(API)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(cerveja_invalida.toBeer())))
                    .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                    .andExpect(status().is(anyOf(is(400), is(500))));
        }
    }

    @Test
    void atualizarPrecoCorretamente_then_Ok() throws Exception {
        Beer beer = cervejaOk();
        beerService.save(beer);

        Beer beerToPatch = new Beer();
        beerToPatch.setId(beer.getId());
        double newPrice = beer.getPrice() + 854.96;
        beerToPatch.setPrice(newPrice);

        mockMvc.perform(patch(API)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beerToPatch)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price", is(newPrice)));
    }

    @Test
    void patchCervejaComIdInvalido_then_Error() throws Exception {
        Beer beer = new Beer();
        beer.setPrice(price);

        //Id nulo
        beer.setId(id_invalid_a);
        mockMvc.perform(patch(API)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.[0].field", is("id")))
                .andExpect(jsonPath("$.fieldErrors.[0].message", is("O id deve ser informado")));

        //Id negativo
        beer.setId(id_invalid_b);
        mockMvc.perform(patch(API)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.[0].field", is("id")))
                .andExpect(jsonPath("$.fieldErrors.[0].message", is("O id deve ser maior que 0")));

        //Id inexistente
        beer.setId(id_invalid_c);
        mockMvc.perform(patch(API)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isNotFound());

    }

    @Test
    void patchCervejaComPriceInvalido_then_Error() throws Exception {
        Beer beer = cervejaOk();
        beerService.save(beer);

        //nulo
        beer.setPrice(price_invalid_a);
        mockMvc.perform(patch(API)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.[0].field", is("price")))
                .andExpect(jsonPath("$.fieldErrors.[0].message", is("O campo price deve ser informado")));


        //menor que .01
        beer.setPrice(price_invalid_b);
        mockMvc.perform(patch(API)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.[0].field", is("price")))
                .andExpect(jsonPath("$.fieldErrors.[0].message", is("O campo price deve ser no minimo 0.01")));

    }

    @Test
    void removerCervejaCadastrada_then_Ok() throws Exception {
        Beer beer = cervejaOk();
        beerService.save(beer);
        beerService.delete(beer.getId());
        Assertions.assertFalse(beerService.findOne(beer.getId()).isPresent());

        beer = cervejaOk();
        beerService.save(beer);
        mockMvc.perform(delete(API + "/" + beer.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        mockMvc.perform(get(API + "/" + beer.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void removerCervejaInvalida_then_Error() throws Exception {
        //id nulo
        Assertions.assertThrows(InvalidDataAccessApiUsageException.class, () -> beerService.delete(id_invalid_a));
        mockMvc.perform(delete(API)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());

        //id negativo
        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> beerService.delete(id_invalid_b));
        mockMvc.perform(delete(API + "/" + id_invalid_b)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title", is("Id invalido " + id_invalid_b)))
                .andExpect(jsonPath("$.errorKey", is("idinvalid")));

        //id inexistente
        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> beerService.delete(id_invalid_c));
        mockMvc.perform(delete(API + "/" + id_invalid_c)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testBeerDTOtoBeer() {
        BeerDTO beerDTO = new BeerDTO();

        beerDTO.setId(582);
        beerDTO.setPrice(45.6);
        beerDTO.setName("AABCCsdgfagredsdddCC");
        beerDTO.setAlcoholContent("XXGAASDDDD");
        beerDTO.setCategory("KKSKDFJFJJFFFFFF");
        beerDTO.setIngredients("NMSJNDdfsfasd");

        Beer beer = beerDTO.toBeer();

        Assertions.assertEquals(beerDTO.getId(), beer.getId());
        Assertions.assertEquals(beerDTO.getPrice(), beer.getPrice());
        Assertions.assertEquals(beerDTO.getName(), beer.getName());
        Assertions.assertEquals(beerDTO.getAlcoholContent(), beer.getAlcoholContent());
        Assertions.assertEquals(beerDTO.getCategory(), beer.getCategory());
        Assertions.assertEquals(beerDTO.getIngredients(), beer.getIngredients());

    }

}