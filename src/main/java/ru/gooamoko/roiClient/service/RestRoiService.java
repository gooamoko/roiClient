package ru.gooamoko.roiClient.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.gooamoko.roiClient.client.RoiClient;
import ru.gooamoko.roiClient.client.RoiServiceClient;
import ru.gooamoko.roiClient.entity.PetitionEntity;
import ru.gooamoko.roiClient.model.PetitionListDataModel;
import ru.gooamoko.roiClient.model.PetitionModel;
import ru.gooamoko.roiClient.repository.PetitionRepository;
import ru.gooamoko.roiClient.service.process.AddOrUpdatePetitionProcessor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class RestRoiService implements RoiService {
    private final static Logger log = LoggerFactory.getLogger(RestRoiService.class);
    private final static int DEFAULT_THREADS_COUNT = 6;
    private final RoiClient roiClient;
    private final RoiServiceClient roiServiceClient;
    private final PetitionRepository repository;
    private final int threadsCount;

    public RestRoiService(RoiClient roiClient, RoiServiceClient roiServiceClient,
                          PetitionRepository repository, @Value("${processing.threads.count}") Integer threadsCount) {
        this.roiClient = roiClient;
        this.roiServiceClient = roiServiceClient;
        this.repository = repository;
        this.threadsCount = threadsCount == null ? DEFAULT_THREADS_COUNT : threadsCount;
    }

    @Override
    public void processPoll() {
        PetitionListDataModel poll = roiClient.getPoll();
        List<PetitionModel> petitionsList = poll.getData();
        long time = System.currentTimeMillis();
        log.info("Получено инициатив: {}.", petitionsList.size());
        int recordsAdded = 0;
        ExecutorService threadPool = Executors.newFixedThreadPool(threadsCount);
        try {
            ExecutorCompletionService<Integer> executorService = new ExecutorCompletionService<>(threadPool);
            for (PetitionModel model : petitionsList) {
                executorService.submit(new AddOrUpdatePetitionProcessor(roiClient, roiServiceClient, repository, model));
            }

            // Получаем результаты
            for (int i = 0; i < petitionsList.size(); i++) {
                Future<Integer> future = executorService.take();
                recordsAdded += future.get();
            }
        } catch (Exception e) {
            log.error("Ошибка обработки списка инициатив.", e);
        } finally {
            threadPool.shutdown();
        }

        log.info("Добавлено инициатив в БД: {}. Потоков для обработки: {}. Добавление заняло {} мс.",
                recordsAdded, threadsCount, System.currentTimeMillis() - time);
    }

    @Override
    public void processOld() {
        LocalDateTime dateTime = LocalDateTime.now().minusDays(7);
        List<PetitionEntity> oldEntities = repository.getOldPetitions(dateTime);
        if (oldEntities == null || oldEntities.isEmpty()) {
            return;
        }

        for (PetitionEntity entity : oldEntities) {
            boolean success;
            try {
                ResponseEntity<String> result = roiServiceClient.deleteById(entity.getId());
                success = successResult(result);
            } catch (Exception e) {
                log.error("Ошибка при удалении инициативы.", e);
                success = false;
            }

            if (success) {
                repository.delete(entity);
            }
        }
    }


    private boolean successResult(ResponseEntity<String> result) {
        return HttpStatus.OK.equals(result.getStatusCode());
    }
}
