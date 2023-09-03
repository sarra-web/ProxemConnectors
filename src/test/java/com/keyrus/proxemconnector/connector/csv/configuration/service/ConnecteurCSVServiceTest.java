package com.keyrus.proxemconnector.connector.csv.configuration.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.keyrus.proxemconnector.connector.csv.configuration.dto.ConnectorCSVDTO;
import com.keyrus.proxemconnector.connector.csv.configuration.dto.ProxemDto;
import com.keyrus.proxemconnector.connector.csv.configuration.enumerations.FieldType;
import com.keyrus.proxemconnector.connector.csv.configuration.model.ConnectorCSV;
import com.keyrus.proxemconnector.connector.csv.configuration.model.Field;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.keyrus.proxemconnector.connector.csv.configuration.service.csv.ConnectorCSVService.CSVDataToJSON;


class ConnecteurCSVServiceTest {

    @Test
    void extractHeader() {
    }

    @Test
    void CSVDataToJSON_test() {
        List<String> list = List.of("titre","identifiant","texte","meta");
        final var id=UUID.randomUUID().toString();
        final var configuration =
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
                                                        FieldType.Meta,
                                                        true, true
                                                )
                                                .get()
                                )
                                .collect(Collectors.toUnmodifiableSet())
                )
                .build()
                .get();

        ConnectorCSVDTO conn=new ConnectorCSVDTO(configuration);
        System.out.println( conn);
        List<ProxemDto> proxemDtos = CSVDataToJSON(conn);
        System.out.println( "voila"+proxemDtos);
        ObjectMapper objectMapper = new ObjectMapper();
       ArrayNode jsonArray = objectMapper.valueToTree(proxemDtos);
        System.out.println( jsonArray);
        String url = "https://studio3.proxem.com/validation5a/api/v1/corpus/a0e04a5f-ab7c-4b0e-97be-af263a61ba49/documents";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization","ApiKey mehdi.khayati@keyrus.com:63cdd92e-adb4-42fe-a655-8e54aeb0653f");
        HttpEntity<String> entity = new HttpEntity<>(jsonArray.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);

        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());
    }
}