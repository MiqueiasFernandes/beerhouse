# API de gerenciamento de 🍺 artesanais

acesse: [beerhouse.mikeias.net](http://beerhouse.mikeias.net/)

- API Rest Full em :9000/beerhouse
   - PATCH para atualizar apenas o preço
   - Paginação, Ordenação e Filtro
   
### Frameworks Spring e JHipster
- Desacoplamento do Spring da API BeerHouse:
   - com.beerhouse.api.BeerAPI : Definição da API
   - com.beerhouse.api.web.rest.BeerSpringAPI : realização da API com Spring

### Banco relacional embutido H2
- Criação/leitura de de sql para popular o banco
- Acesso ao H2 pela web em /h2-console
 
## Swagger
- Acesso a documentação em /swagger-ui/index.html 

## Testes
- 22 testes unitarios para consulta, editação e remoção 

## Acessar arquivos pela web

![alt text](http://beerhouse.mikeias.net/beerhouse.png)

