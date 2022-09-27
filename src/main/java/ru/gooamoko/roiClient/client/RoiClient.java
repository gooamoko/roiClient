package ru.gooamoko.roiClient.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.gooamoko.roiClient.model.PetitionListDataModel;

@FeignClient(name = "roiClient", url = "https://www.roi.ru/api/")
public interface RoiClient {

    @RequestMapping(method = RequestMethod.GET, value = "/petitions/poll.json")
    PetitionListDataModel getPoll();
}
