package ru.gooamoko.roiClient.service;

public interface RoiService {

    /**
     * Выполняет обработку списка инициатив на голосовании.
     */
    void processPoll();

    /**
     * обрабатывает список устаревших инициатив
     */
    void processOld();
}
