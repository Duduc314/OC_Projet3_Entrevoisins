package com.openclassrooms.entrevoisins.service;

import com.openclassrooms.entrevoisins.model.Neighbour;
import java.util.List;


/**
 * Neighbour API client
 */
public interface NeighbourApiService {

    /**
     * Get all my Neighbours
     * @return {@link List}
     */
    List<Neighbour> getNeighbours();

    /**
     * Deletes a neighbour
     * @param neighbour
     */
    void deleteNeighbour(Neighbour neighbour);

    /**
     * Create a neighbour
     * @param neighbour
     */
    void createNeighbour(Neighbour neighbour);

    /**
     * Update "Favorite status" of an existing Neighbour"
     * @param neighbour
     */
    void updateFavoriteStatus(Neighbour neighbour);

    /**
     * Update modified fields of an existing Neighbour"
     * @param neighbour
     */
    void updateDataNeighbour(Neighbour neighbour);
}
