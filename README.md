# Testando uma API REST para cadastro de entidades simples

## @Transactional

A anotação `@Transactional` em um método de teste geralmente indica que uma transação será iniciada antes do início do teste e revertida (rollback) ao final do teste. Isso é feito para garantir que o banco de dados retorne ao seu estado original após a execução do teste, evitando alterações permanentes no banco de dados devido aos testes.

## @Commit

As transações gerenciadas por testes são revertidas ao fim de sua execução. Para garantir que a mesma seja confirmada, é necessário que o método ou classe de teste seja anotado com `@Commit`.

## Transactional.TxType.NEVER ou Transactional.TxType.NOT_SUPPORT.

Para garantir que não existam transações em classes ou métodos de testes de maneira explícita, podemos utilizar a anotação `@Transactional` com argumento `Transactional.TxType.NEVER` ou `Transactional.TxType.NOT_SUPPORT`.

## @ActiveProfiles("seuambiente")

O Spring Test oferece suporte à configuração de perfis de desenvolvimento por meio da anotação `@ActiveProfiles`. Essa anotação pode ser usada em nível de classe ou método para ativar perfis específicos durante a execução dos testes.

Ao utilizar `@ActiveProfiles`, você pode especificar quais perfis deseja ativar, permitindo que você configure o ambiente de teste de acordo com as necessidades do perfil em questão.

## @DirtiesContext

Para a maioria dos casos, um mesmo contexto pode abranger todos os testes, mas se for necessário substituir um bean ou construir um novo contexto para uma classe ou método, podemos utilizar a anotação `@DirtiesContext` indicando para o Spring que este contexto está inadequado, para que seja limpo do cache e outro seja construído.

## @AutoConfigureMockMvc

Spring Test oferece um cliente HTTP próprio para testes de integração considerando toda a complexidade do ecossistema Spring. O `MockMvc` oferece de forma simples estrutura para testar beans referentes aos seguintes estereótipos: `@Controller`, `@RestController`, `@RestControllerAdvice`, `@ControllerAdvice`. Também oferece facilidade para testar com Filtros do Spring. Para construir testes de integração utilizando o `MockMvc`, devemos configurá-lo, e isto é feito anotando com `@AutoConfigureMockMvc` a nível de classe.

## @mockMvcRequestBuilders

O `MockMvcRequestBuilders` é a abstração responsável por definir uma requisição HTTP. É através deste que indicamos qual o endereço, verbo, headers e corpo da requisição.

## @MockMvcResultMatchers

O Spring Test fornece a classe `MockMvcResultMatchers` para realizar asserções (asserts) sobre a resposta HTTP ao usar o `MockMvc` para testar controladores (controllers) em ambientes Spring MVC. `MockMvcResultMatchers` é responsável por auxiliar no momento da verificação dos elementos da resposta HTTP.

Por exemplo, você pode usar métodos como `status()`, `content()`, `json()`, `header()`, entre outros, para validar diferentes aspectos da resposta HTTP gerada pelo controlador durante o teste.

Portanto, ao realizar testes com o `MockMvc`, `MockMvcResultMatchers` é uma ferramenta valiosa para verificar se a resposta HTTP está em conformidade com as expectativas do teste.

Para fazer bons testes, devemos garantir que estes sejam independentes, ou seja, que o resultado de um não interfira no outro. Uma forma de garantir esta independência é utilizar uma estratégia de limpeza de ambiente, onde antes de executar cada teste faremos a limpeza do banco aplicando a operação de `deleteAll()`.
# Construindo Testes de Integração com MockMvc do Spring Test

## Três Etapas nos Testes de Integração:

O texto destaca três etapas principais nos testes de integração:

1. **Cenário:**
   Preparação dos componentes necessários para o cenário de teste.

2. **Ação:**
   Execução da ação que está sendo testada.

3. **Corretude:**
   Verificação se o resultado da ação é o esperado.


