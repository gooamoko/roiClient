package ru.gooamoko.roiClient.service.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.gooamoko.roiClient.client.RoiClient;
import ru.gooamoko.roiClient.client.RoiServiceClient;
import ru.gooamoko.roiClient.entity.PetitionEntity;
import ru.gooamoko.roiClient.model.LevelModel;
import ru.gooamoko.roiClient.model.PetitionModel;
import ru.gooamoko.roiClient.model.StatusModel;
import ru.gooamoko.roiClient.repository.PetitionRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.Callable;

public class AddOrUpdatePetitionProcessor implements Callable<Integer> {
    private static final Logger log = LoggerFactory.getLogger(AddOrUpdatePetitionProcessor.class);
    private final RoiClient roiClient;
    private final RoiServiceClient roiServiceClient;
    private final PetitionRepository repository;
    private final PetitionModel model;

    public AddOrUpdatePetitionProcessor(RoiClient roiClient, RoiServiceClient roiServiceClient,
                                        PetitionRepository repository, PetitionModel model) {
        this.roiClient = roiClient;
        this.roiServiceClient = roiServiceClient;
        this.repository = repository;
        this.model = model;
    }

    @Override
    public Integer call() {
        Long id = model.getId();
        Optional<PetitionEntity> optionalEntity = repository.findById(id);
        if (optionalEntity.isEmpty()) {
            // Получаем текст инициативы
            boolean success;
            try {
                HttpEntity<String> petition = roiClient.getPetition(id);
                if (petition.hasBody()) {
                    String petitionText = petition.getBody();
                    log.info(petitionText);
                    ResponseEntity<String> result = roiServiceClient.save(petitionText);
                    success = successResult(result);
                } else {
                    success = false;
                }
            } catch (Exception e) {
                log.error("Ошибка при сохранении инициативы.", e);
                success = false;
            }

            if (success) {
                // Создаем новую инициативу
                PetitionEntity newEntity = createEntity(model);
                repository.save(newEntity);
                return 1;
            }
        } else {
            // Инициатива уже есть. Просто обновим дату
            PetitionEntity entity = optionalEntity.get();
            entity.setUpdateTimestamp(LocalDateTime.now());
            repository.save(entity);
        }

        return 0;
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

    private boolean successResult(ResponseEntity<String> result) {
        return HttpStatus.OK.equals(result.getStatusCode());
    }
}
