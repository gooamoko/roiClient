package ru.gooamoko.roiClient.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "roiServiceClient", url = "http://localhost:8080/petitions/")
public interface RoiServiceClient {

    @RequestMapping(method = RequestMethod.POST, value = "save",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    HttpEntity<String> save(@RequestBody String petitionDocument);
}
