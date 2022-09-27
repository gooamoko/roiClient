package ru.gooamoko.roiClient.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.gooamoko.roiClient.client.RoiClient;
import ru.gooamoko.roiClient.model.PetitionListDataModel;
import ru.gooamoko.roiClient.model.PetitionModel;

import java.util.List;

@Service
public class RestRoiService implements RoiService {
    private final static Logger log = LoggerFactory.getLogger(RestRoiService.class);
    private final RoiClient roiClient;

    public RestRoiService(RoiClient roiClient) {
        this.roiClient = roiClient;
    }

    @Override
    public void processPoll() {
        PetitionListDataModel poll = roiClient.getPoll();
        List<PetitionModel> petitionsList = poll.getData();
        log.info("У нас {} инициатив на голосовании.", petitionsList.size());
    }
}
