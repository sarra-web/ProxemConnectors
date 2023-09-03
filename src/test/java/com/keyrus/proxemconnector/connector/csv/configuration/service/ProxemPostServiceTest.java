package com.keyrus.proxemconnector.connector.csv.configuration.service;

import com.keyrus.proxemconnector.connector.csv.configuration.repository.project.ProjectJDBCDatabaseRepository;
import com.keyrus.proxemconnector.connector.csv.configuration.service.csv.ConnectorCSVService;

class ProxemPostServiceTest {
    private final ConnectorCSVService connectorCSVService;
    private final ProjectJDBCDatabaseRepository projectDatabaseRepository;

    ProxemPostServiceTest(ConnectorCSVService connectorCSVService, ProjectJDBCDatabaseRepository projectDatabaseRepository) {
        this.connectorCSVService = connectorCSVService;
        this.projectDatabaseRepository = projectDatabaseRepository;
    }

  //  @Test
    /*void updatePost_test() {
        List<FieldType> list = List.of(FieldType.Title, FieldType.Identifier, FieldType.Text, FieldType.Meta);
        final var id = UUID.randomUUID().toString();
        final var connnectorCSV =
                ConnectorCSV.Builder
                        .builder()
                        .withId(id)
                        .withName(UUID.randomUUID().toString())
                        .withSeparator(";")
                        .withEncoding(StandardCharsets.UTF_8.name())
                        .withpath("email.csv")
                        .withquotingCaracter(UUID.randomUUID().toString())
                        .withescapingCaracter(UUID.randomUUID().toString())
                        .withContainsHeaders(new Random().nextBoolean())
                        .withHeaders(
                                IntStream.iterate(1, it -> it + 1)
                                        .limit(4)
                                        .mapToObj(it ->
                                                Field.of(
                                                                UUID.randomUUID().toString(),
                                                                id,
                                                                UUID.randomUUID().toString(),
                                                                it,
                                                                UUID.randomUUID().toString(),
                                                                list.get(it - 1),
                                                                false,true
                                                        )
                                                        .get()
                                        )
                                        .collect(Collectors.toUnmodifiableSet())
                        )
                        .build()
                        .get();


        List<ProxemDto> proxemDtos = CSVDataToJSON(new ConnectorCSVDTO(connnectorCSV));
        System.out.println(proxemDtos);
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode jsonArray = objectMapper.valueToTree(proxemDtos);
        System.out.println(jsonArray);
       String url = "https://studio3.proxem.com/validation5a/api/v1/corpus/a0e04a5f-ab7c-4b0e-97be-af263a61ba49/documents";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization","ApiKey mehdi.khayati@keyrus.com:63cdd92e-adb4-42fe-a655-8e54aeb0653f");
        HttpEntity<String> entity = new HttpEntity<>(jsonArray.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);

        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());
    }*/
    /*@Test
   // @Scheduled(cron = "2 * * * * *")
    //@Async
    void updatePost_test2() {
        projectDatabaseRepository.save(new ProjectDAO("proj","proj","proj"));
        List<FieldType> list = List.of(FieldType.Title, FieldType.Identifier, FieldType.Text, FieldType.Meta);
        final var id = UUID.randomUUID().toString();
        final var connnectorCSV =
                ConnectorCSV.Builder
                        .builder()
                        .withId(id)
                        .withName(UUID.randomUUID().toString())
                        .withSeparator(";")
                        .withEncoding(StandardCharsets.UTF_8.name())
                        .withpath("email.csv")
                        .withquotingCaracter("\"")
                        .withescapingCaracter("\\")
                        .withContainsHeaders(true)
                        .withProjectName("proj")
                        .withHeaders(
                                IntStream.iterate(1, it -> it + 1)
                                        .limit(4)
                                        .mapToObj(it ->
                                                Field.of(
                                                                UUID.randomUUID().toString(),
                                                                id,
                                                                UUID.randomUUID().toString(),
                                                                it,
                                                                UUID.randomUUID().toString(),
                                                                list.get(it - 1),
                                                                true, true
                                                        )
                                                        .get()
                                        )
                                        .collect(Collectors.toUnmodifiableSet())
                        )
                        .build()
                        .get();

        ConnectorCSVDTO dto=(new ConnectorCSVDTO(connnectorCSV));
        System.out.println(dto);
        List<ProxemDto> proxemDtos =connectorCSVService.updatePost( CSVDataToJSON(dto));
        System.out.println(proxemDtos );


    }*/







    }
