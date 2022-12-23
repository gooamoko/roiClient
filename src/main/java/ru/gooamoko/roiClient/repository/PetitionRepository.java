package ru.gooamoko.roiClient.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.gooamoko.roiClient.entity.PetitionEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface PetitionRepository extends CrudRepository<PetitionEntity, Long> {

    @Query("select pe from PetitionEntity pe where pe.updateTimestamp < :date")
    List<PetitionEntity> getOldPetitions(LocalDateTime date);
}
