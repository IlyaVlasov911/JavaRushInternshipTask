package com.game.repository;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

public class PlayerSpec {

    public static Specification<Player> nameContain(String name){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + name + "%"));
    }

    public static Specification<Player> titleContain(String title){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("title"), "%" + title + "%"));
    }

    public static Specification<Player> raceEquals(Race race){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("race"), race));
    }

    public static Specification<Player> professionEquals(Profession profession){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("profession"), profession));
    }

    public static Specification<Player> dateAfter(Long after){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("birthday"), new Date(after)));
    }

    public static Specification<Player> dateBefore(Long before){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("birthday"), new Date(before)));
    }

    public static Specification<Player> banned(Boolean banned){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("banned"), banned));
    }

    public static Specification<Player> findMinExperience(Integer minExp){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("experience"), minExp));
    }

    public static Specification<Player> findMaxExperience(Integer maxExp){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("experience"), maxExp));
    }

    public static Specification<Player> findMinLevel(Integer minLvl){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("level"), minLvl));
    }

    public static Specification<Player> findMaxLevel(Integer maxLvl){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("level"), maxLvl));
    }
}
