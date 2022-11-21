package ru.gooamoko.roiClient.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.gooamoko.roiClient.model.PetitionListDataModel;

@FeignClient(name = "roiClient", url = "https://www.roi.ru/api/")
public interface RoiClient {

    @RequestMapping(method = RequestMethod.GET, value = "petitions/poll.json")
    PetitionListDataModel getPoll();

    @RequestMapping(method = RequestMethod.GET, value = "petition/{id}.json")
    HttpEntity<String> getPetition(@PathVariable Long id);
}
