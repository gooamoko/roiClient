package ru.gooamoko.roiClient.repository;

import org.springframework.data.repository.CrudRepository;
import ru.gooamoko.roiClient.entity.PetitionEntity;

public interface PetitionRepository extends CrudRepository<PetitionEntity, Long> {
}
