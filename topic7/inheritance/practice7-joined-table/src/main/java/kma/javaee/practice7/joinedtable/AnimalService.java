package kma.javaee.practice7.joinedtable;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kma.javaee.practice7.joinedtable.entities.Animal;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnimalService {

    private final EntityManager entityManager;

    @Transactional
    public <T extends Animal> T saveNewAnimal(T animal) {
        return entityManager.merge(animal);
    }

    public <T extends Animal> T getAnimalById(int id, Class<T> animalClazz) {
        return entityManager.find(animalClazz, id);
    }

    @Transactional
    public <T extends Animal> void deleteAnimalById(int id, Class<T> animalClazz) {
        entityManager.remove(entityManager.find(animalClazz, id));
    }

    public List<? extends Animal> getAllAnimals() {
        return entityManager.createQuery("FROM Animal", Animal.class)
            .getResultList();
    }

}
