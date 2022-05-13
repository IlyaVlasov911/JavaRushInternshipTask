package com.game.controller;

import com.game.entity.Player;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/rest/players")
public class PlayerController {
    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping
    public ResponseEntity<List<Player>> getPlayersList(@RequestParam Map<String, String> param){
        Specification<Player> spec = playerService.returnSpecForCount(param);
        Pageable pageable = playerService.returnPageable(param);
        return new ResponseEntity<>(playerService.getPlayersWith(spec,pageable).getContent(), HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getPlayersCount(@RequestParam Map<String, String> param){
        Specification<Player> spec = playerService.returnSpecForCount(param);
        return new ResponseEntity<>(playerService.playersCount(spec), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Player> create(@RequestBody Player player){
        if(playerService.notNull(player) && playerService.isParametersValid(player)){
            return new ResponseEntity<>(playerService.save(player), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Player> findById(@PathVariable(name = "id") Long id){
        if(!playerService.isIdValid(id)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(playerService.findById(id).isPresent()){
            return new ResponseEntity<>(playerService.findById(id).get(), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{id}")
    public ResponseEntity<Player> update(@PathVariable(name = "id") Long id, @RequestBody Player updPlayer){
        if(!playerService.isIdValid(id)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(playerService.isExist(id)){
            Player oldPlayer = playerService.findById(id).get();
            Player updated = playerService.updateFields(oldPlayer, updPlayer);
            if(playerService.isParametersValid(updated)){
                return new ResponseEntity<>(playerService.save(updated), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Player> delete(@PathVariable(name = "id") Long id){
        if(!playerService.isIdValid(id)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(playerService.isExist(id)){
            playerService.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}