package ru.gooamoko.roiClient.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import ru.gooamoko.roiClient.client.RoiClient;
import ru.gooamoko.roiClient.client.RoiServiceClient;
import ru.gooamoko.roiClient.entity.PetitionEntity;
import ru.gooamoko.roiClient.model.LevelModel;
import ru.gooamoko.roiClient.model.PetitionListDataModel;
import ru.gooamoko.roiClient.model.PetitionModel;
import ru.gooamoko.roiClient.model.StatusModel;
import ru.gooamoko.roiClient.repository.PetitionRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RestRoiService implements RoiService {
    private final static Logger log = LoggerFactory.getLogger(RestRoiService.class);
    private final RoiClient roiClient;
    private final RoiServiceClient roiServiceClient;
    private final PetitionRepository repository;

    public RestRoiService(RoiClient roiClient, RoiServiceClient roiServiceClient, PetitionRepository repository) {
        this.roiClient = roiClient;
        this.roiServiceClient = roiServiceClient;
        this.repository = repository;
    }

    @Override
    public void processPoll() {
        PetitionListDataModel poll = roiClient.getPoll();
        List<PetitionModel> petitionsList = poll.getData();
        log.info("Получено инициатив: {}.", petitionsList.size());
        int recordsAdded = 0;
        for (PetitionModel model : petitionsList) {
            Optional<PetitionEntity> optionalEntity = repository.findById(model.getId());
            if (optionalEntity.isEmpty()) {
                PetitionEntity newEntity = createEntity(model);
                repository.save(newEntity);
                recordsAdded += 1;

                // Получаем текст инициативы
                HttpEntity<String> petition = roiClient.getPetition(newEntity.getId());
                if (petition.hasBody()) {
                    String petitionText = petition.getBody();
                    log.info(petitionText);
                    roiServiceClient.save(petitionText);
                }
            } else {
                // Инициатива уже есть. Просто обновим дату
                PetitionEntity entity = optionalEntity.get();
                entity.setUpdateTimestamp(LocalDateTime.now());
                repository.save(entity);
            }
        }
        log.info("Добавлено инициатив в БД: {}.", recordsAdded);
    }

    @Override
    public void processOld() {
        LocalDateTime dateTime = LocalDateTime.now().minusDays(7);
        List<PetitionEntity> oldEntities = repository.getOldPetitions(dateTime);
        if (oldEntities == null || oldEntities.isEmpty()) {
            return;
        }

        for (PetitionEntity entity : oldEntities) {
            roiServiceClient.deleteById(entity.getId());
            repository.delete(entity);
        }
    }


    private PetitionEntity createEntity(PetitionModel model) {
        PetitionEntity entity = new PetitionEntity();
        entity.setId(model.getId());
        entity.setTitle(model.getTitle());
        entity.setUpdateTimestamp(LocalDateTime.now());
        LevelModel level = model.getLevel();
        if (level != null) {
            entity.setLevelId(level.getId().intValue());
        }
        StatusModel status = model.getStatus();
        if (status != null) {
            entity.setStatusId(status.getId().intValue());
        }
        return entity;
    }
}
