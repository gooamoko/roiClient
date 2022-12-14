package ru.gooamoko.roiClient.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "roiServiceClient", url = "http://localhost:8080/petitions/")
public interface RoiServiceClient {

    @RequestMapping(method = RequestMethod.POST, value = "save",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    ResponseEntity<String> save(@RequestBody String petitionDocument);

    @RequestMapping(method = RequestMethod.GET, value = "delete")
    ResponseEntity<String> deleteById(@RequestParam("id") Long id);
}
