package com.mapleApiTest.projectOne.controller.character;

import com.mapleApiTest.projectOne.domain.character.CharactersItemEquip;
import com.mapleApiTest.projectOne.dto.ItemInfo.HatStatInfoDTO;
import com.mapleApiTest.projectOne.dto.ItemInfo.ItemSimulationDTO;
import com.mapleApiTest.projectOne.dto.character.request.*;
//import com.mapleApiTest.projectOne.dto.character.response.CharacterInfo;
import com.mapleApiTest.projectOne.dto.item.*;
import com.mapleApiTest.projectOne.service.character.CharacterService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
public class CharacterController {

    @Value("${external.api.key}")
    private String apiKey;

    @Value("${external.api.url}")
    private String apiUrl;

    private final CharacterService characterService;

    public CharacterController(CharacterService characterService) {
        this.characterService = characterService;
    }


    @GetMapping("/maplestory/v1/id")
    public CompletableFuture<String> getCharacterOcid(@RequestParam String charactersName) {
        GetCharactersInfo getCharactersInfo = new GetCharactersInfo(charactersName);
        return characterService.getCharacterOcid(getCharactersInfo);
    }

    /////////////////////////////////

    @GetMapping("/maplestory/v1/character/basic")
    public CompletableFuture<CharactersInfoDTO> getCharacterInfo(HttpServletRequest request, @RequestParam String charactersName, String date) {
        GetCharactersInfo getCharactersInfo = new GetCharactersInfo(charactersName, date);
        CompletableFuture<String> CompletableFutureOcid = characterService.getCharacterOcid(getCharactersInfo);
        String ocid = CompletableFutureOcid.join();
        String Url = request.getRequestURI();
        return characterService.getCharactersInfo(getCharactersInfo, Url, apiKey, ocid);

    }

    @GetMapping("/maplestory/v1/character/stat")
    public CompletableFuture<CharactersStatInfoDTO> getCharacterStatInfo(HttpServletRequest request, @RequestParam String charactersName, String date) {
        GetCharactersInfo getCharactersInfo = new GetCharactersInfo(charactersName, date);
        CompletableFuture<String> CompletableFutureOcid = characterService.getCharacterOcid(getCharactersInfo);
        String ocid = CompletableFutureOcid.join();
        String Url = request.getRequestURI();
        return characterService.getCharactersStatInfo(getCharactersInfo, Url, apiKey, ocid);

    }


    @GetMapping("/maplestory/v1/character/item-equipment")
    public CompletableFuture<CharactersItemEquipDTO> getCharacterItemEquipInfo(HttpServletRequest request, @RequestParam String charactersName, String date) {
        GetCharactersInfo getCharactersInfo = new GetCharactersInfo(charactersName, date);
        CompletableFuture<String> CompletableFutureOcid = characterService.getCharacterOcid(getCharactersInfo);
        String ocid = CompletableFutureOcid.join();
        String Url = request.getRequestURI();
        return characterService.getCharactersItemEquip(getCharactersInfo, Url, apiKey, ocid);

    }


    @GetMapping("/totalInfo")
    public CompletableFuture<CharactersTotalInfoDTO> getCharacterItemEquipInfo(@RequestParam String charactersName, String date) {
        GetCharactersInfo getCharactersInfo = new GetCharactersInfo(charactersName, date);

        return characterService.getCharactersTotalInfo(getCharactersInfo);

    }

    @GetMapping("/HatInfo")
    public CompletableFuture<CharactersHatInfoDTO> getCharactersHatInfo(@RequestParam String charactersName, String date) {
        GetCharactersInfo getCharactersInfo = new GetCharactersInfo(charactersName, date);

        return characterService.getCharactersHatInfo(getCharactersInfo);

    }

    @GetMapping("/TopInfo")
    public CompletableFuture<CharactersTopInfoDTO> getCharactersTopInfo(@RequestParam String charactersName, String date) {
        GetCharactersInfo getCharactersInfo = new GetCharactersInfo(charactersName, date);

        return characterService.getCharactersTopInfo(getCharactersInfo);
    }


//    @GetMapping("/ItemInfoTest")
//    public void getCharactersMedalInfo(@RequestParam String charactersName, String date) {
//        GetCharactersInfo getCharactersInfo = new GetCharactersInfo(charactersName, date);
//
//        characterService.someServiceMethod(getCharactersInfo);
//    }

    @GetMapping("/charactersBaseInfo")
    public String getCharactersBaseInfo(@RequestParam String charactersName, String date, int addAllStat, Double addBossDamage, int addAtMgPower, int petAtMgPower, int mainStatBase, int mainStatSkill, int mainStatPerBase, int mainStatPerSkill, int mainStatNonPer, int subStatBase, int subStatSkill, int subStatPerBase, int subStatPerSkill, int subStatNonPer, int atMgPowerBase, int atMgPowerSkill, int atMgPowerPerBase, int atMgPowerPerSkill, Double criticalDamageBase, Double criticalDamageSkill, Double damageBase, Double damageSkill, Double BossDamageBase, Double BossDamageSkill, boolean isFree) {
        GetCharactersInfo getCharactersInfo = new GetCharactersInfo(charactersName, date);
        GetCharactersTotalInfoDTO charactersTotalInfoDTO = new GetCharactersTotalInfoDTO(addAllStat, addBossDamage, addAtMgPower, petAtMgPower, mainStatBase, mainStatSkill, mainStatPerBase, mainStatPerSkill, mainStatNonPer, subStatBase, subStatSkill, subStatPerBase, subStatPerSkill, subStatNonPer, atMgPowerBase, atMgPowerSkill, atMgPowerPerBase, atMgPowerPerSkill, criticalDamageBase, criticalDamageSkill, damageBase, damageSkill, BossDamageBase, BossDamageSkill, isFree);

        return characterService.getCharactersCombat(charactersTotalInfoDTO, getCharactersInfo);
    }


    @GetMapping("/equipSimulation")
    public void getEquipSimulation(@RequestParam int itemLevel, int starForce, int itemUpgrade) {

        characterService.getEquipSimulation(itemLevel, starForce, itemUpgrade);

    }


    @GetMapping("/charactersChangeBaseInfo")
    public String getCharactersChangeCombat(@RequestParam String charactersName, String date, int addAllStat, Double addBossDamage, int addAtMgPower, int petAtMgPower, int mainStatBase, int mainStatSkill, int mainStatPerBase, int mainStatPerSkill, int mainStatNonPer, int subStatBase, int subStatSkill, int subStatPerBase, int subStatPerSkill, int subStatNonPer, int atMgPowerBase, int atMgPowerSkill, int atMgPowerPerBase, int atMgPowerPerSkill, Double criticalDamageBase, Double criticalDamageSkill, Double damageBase, Double damageSkill, Double BossDamageBase, Double BossDamageSkill, boolean isFree, int itemLevel, int starForce, int itemUpgrade) {

        GetCharactersInfo getCharactersInfo = new GetCharactersInfo(charactersName, date);
        HatStatInfoDTO hatStatInfoDTO = new HatStatInfoDTO(itemLevel);

        int ChangedmainStatBase=0;
        int ChangedsubStatBase=0;
        int ChangedatMgPowerBase=0;

        CompletableFuture<CharactersHatInfoDTO> future = characterService.getCharactersHatInfo(getCharactersInfo);
        CharactersHatInfoDTO hatInfo = future.join();

            // DTO에서 필요한 필드 값을 가져와서 변수로 사용합니다.
            int mainStat = hatInfo.getStr();
            int subStat = hatInfo.getDex();
            int atMgPower = hatInfo.getAttactPower();


        System.out.println(mainStat+"dsdsdsd");
        System.out.println(subStat+"dsdsdsd");
        System.out.println(atMgPower+"dsdsdsd");

        hatStatInfoDTO=characterService.getEquipSimulation(itemLevel, starForce, itemUpgrade);

        System.out.println(hatStatInfoDTO.getMainStat()+"ㅁㄴㅁㄴㄴ12222");
        System.out.println(hatStatInfoDTO.getSubStat()+"ㅁㄴ2ㅁㄴ22");
        System.out.println(hatStatInfoDTO.getAtMgPower()+"ㅁㄴㅁㄴ22");



        ChangedmainStatBase = mainStatBase + (hatStatInfoDTO.getMainStat() - mainStat);
        ChangedsubStatBase = subStatBase + (hatStatInfoDTO.getSubStat() - subStat);
        ChangedatMgPowerBase = atMgPowerBase + (hatStatInfoDTO.getAtMgPower() - atMgPower);

        System.out.println(ChangedmainStatBase+"dsdsdsd1");
        System.out.println(ChangedsubStatBase+"dsdsdsd1");
        System.out.println(ChangedatMgPowerBase+"dsdsdsd1");

        GetCharactersTotalChangedInfoDTO getCharactersTotalChangedInfoDTO = new GetCharactersTotalChangedInfoDTO(addAllStat, addBossDamage, addAtMgPower, petAtMgPower, ChangedmainStatBase, mainStatSkill, mainStatPerBase, mainStatPerSkill, mainStatNonPer, ChangedsubStatBase, subStatSkill, subStatPerBase, subStatPerSkill, subStatNonPer, ChangedatMgPowerBase, atMgPowerSkill, atMgPowerPerBase, atMgPowerPerSkill, criticalDamageBase, criticalDamageSkill, damageBase, damageSkill, BossDamageBase, BossDamageSkill, isFree);


        return characterService.getCharactersChangeCombat(getCharactersTotalChangedInfoDTO, getCharactersInfo, itemLevel, starForce, itemUpgrade);
    }


}
