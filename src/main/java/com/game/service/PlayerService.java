package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayerRep;
import com.game.repository.PlayerSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class PlayerService {
    private final PlayerRep playerRep;

    @Autowired
    public PlayerService(PlayerRep playerRep) {
        this.playerRep = playerRep;
    }

    public boolean notNull(Player player){
        return player.getName() != null
                && player.getTitle() != null
                && player.getRace() != null
                && player.getProfession() != null
                && player.getBirthday() != null;
    }
    public boolean isParametersValid(Player player){
        return !player.getName().isEmpty() && player.getName().length() <= 12
                && player.getTitle().length() <= 30
                && player.getExperience() > 0 && player.getExperience() <= 10000000
                && player.getBirthday().getTime() > 0
                && player.getBirthday().getYear() > 100 && player.getBirthday().getYear() < 1100;
    }

    public Pageable returnPageable(Map<String, String> param) {
        PlayerOrder order;
        int pageNumber;
        int pageSize;

        if(param.containsKey("order")){
            order = PlayerOrder.valueOf(param.get("order"));
        }
        else {
            order = PlayerOrder.ID;
        }
        if(param.containsKey("pageNumber")){
            pageNumber = Integer.parseInt(param.get("pageNumber"));
        }
        else {
            pageNumber = 0;
        }
        if(param.containsKey("pageSize")){
            pageSize = Integer.parseInt(param.get("pageSize"));        }
        else {
            pageSize = 3;
        }

        return PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName()));
    }

    public Specification<Player> returnSpecForCount(Map<String, String> param) {
        Specification<Player> specification = Specification.where(null);
        for(Map.Entry<String,String> entry: param.entrySet()){
            String key = entry.getKey();
            String value = entry.getValue();
            switch (key){
                case ("name"):
                    specification = specification.and(PlayerSpec.nameContain(value));
                    break;
                case ("title"):
                    specification = specification.and(PlayerSpec.titleContain(value));
                    break;
                case ("race"):
                    specification = specification.and(PlayerSpec.raceEquals(Race.valueOf(value)));
                    break;
                case ("profession"):
                    specification = specification.and(PlayerSpec.professionEquals(Profession.valueOf(value)));
                    break;
                case ("after"):
                    specification = specification.and(PlayerSpec.dateAfter(Long.valueOf(value)));
                    break;
                case ("before"):
                    specification = specification.and(PlayerSpec.dateBefore(Long.valueOf(value)));
                    break;
                case ("banned"):
                    specification = specification.and(PlayerSpec.banned(Boolean.valueOf(value)));
                    break;
                case ("minExperience"):
                    specification = specification.and(PlayerSpec.findMinExperience(Integer.valueOf(value)));
                    break;
                case ("maxExperience"):
                    specification = specification.and(PlayerSpec.findMaxExperience(Integer.valueOf(value)));
                    break;
                case ("minLevel"):
                    specification = specification.and(PlayerSpec.findMinLevel(Integer.valueOf(value)));
                    break;
                case ("maxLevel"):
                    specification = specification.and(PlayerSpec.findMaxLevel(Integer.valueOf(value)));
                    break;
            }
        }

        return specification;
    }

    public Page<Player> getPlayersWith(Specification<Player> specification, Pageable pageable) {
        return playerRep.findAll(specification, pageable);
    }

    public Integer playersCount(Specification<Player> specification) {
        return (int) playerRep.count(specification);
    }

    public Player updateFields(Player oldPlayer, Player updPlayer){
        if(updPlayer.getName() != null){
            oldPlayer.setName(updPlayer.getName());
        }
        if(updPlayer.getTitle() != null){
            oldPlayer.setTitle(updPlayer.getTitle());
        }
        if(updPlayer.getRace() != null){
            oldPlayer.setRace(updPlayer.getRace());
        }
        if(updPlayer.getProfession() != null){
            oldPlayer.setProfession(updPlayer.getProfession());
        }
        if(updPlayer.getBirthday() != null){
            oldPlayer.setBirthday(updPlayer.getBirthday());
        }
        if(updPlayer.isBanned() != null){
            oldPlayer.setBanned(updPlayer.isBanned());
        }
        if(updPlayer.getExperience() != null){
            oldPlayer.setExperience(updPlayer.getExperience());
        }

        return oldPlayer;
    }
    public Player save(Player player){
        changeLvl(player);
        return playerRep.save(player);
    }

    private void changeLvl(Player player){
        player.setLevel((int) ((Math.sqrt(2500 + 200 * player.getExperience()) - 50) / 100));

        player.setUntilNextLevel(50 * (player.getLevel() + 1) * (player.getLevel() + 2) - player.getExperience());
    }

    public Optional<Player> findById(Long id){
        return playerRep.findById(id);
    }

    public boolean isExist(Long id){
     return playerRep.existsById(id);
    }

    public void deleteById(Long id){
        playerRep.deleteById(id);
    }

    public boolean isIdValid(Long id){
        return id > 0;
    }
}
