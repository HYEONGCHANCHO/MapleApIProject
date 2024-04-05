package com.mapleApiTest.projectOne.controller.character;

import com.mapleApiTest.dropItemChoice.Service.DropItemChoiceService;
import com.mapleApiTest.projectOne.dto.character.request.*;
//import com.mapleApiTest.projectOne.dto.character.response.CharacterInfo;
import com.mapleApiTest.projectOne.dto.item.*;
import com.mapleApiTest.projectOne.service.character.CharacterService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/maplestory/v1/id") //캐릭터 고유 ocid 불러오기
    public CompletableFuture<String> getCharacterOcid(@RequestParam String charactersName) {
        GetCharactersInfo getCharactersInfo = new GetCharactersInfo(charactersName);
        return characterService.getCharacterOcid(getCharactersInfo);
    }

    @GetMapping("/maplestory/v1/character/basic") //캐릭터 기본정보 불러오기 이름 직업 레벨 서  CharactersInfoDTO
    public CompletableFuture<CharactersInfoDTO> getCharacterInfo(@RequestParam String charactersName) {
        GetCharactersInfo getCharactersInfo = new GetCharactersInfo(charactersName);
        CompletableFuture<String> CompletableFutureOcid = characterService.getCharacterOcid(getCharactersInfo);
        String ocid = CompletableFutureOcid.join();
        return characterService.getCharactersInfo(getCharactersInfo, apiKey, ocid);

    }

    @GetMapping("/maplestory/v1/character/stat") //캐릭터 스탯정보 불러오기 CharactersStatInfoDTO
    public CompletableFuture<CharactersStatInfoDTO> getCharacterStatInfo(@RequestParam String charactersName) {
        GetCharactersInfo getCharactersInfo = new GetCharactersInfo(charactersName);
        CompletableFuture<String> CompletableFutureOcid = characterService.getCharacterOcid(getCharactersInfo);
        String ocid = CompletableFutureOcid.join();
        return characterService.getCharactersStatInfo(getCharactersInfo, apiKey, ocid);

    }

    @GetMapping("/maplestory/v1/character/item-equipment") //캐릭터 착용 장비 정보 불러오기 CharactersItemEquipDTO
    public CompletableFuture<CharactersItemEquipDTO> getCharacterItemEquipInfo(@RequestParam String charactersName) {
        GetCharactersInfo getCharactersInfo = new GetCharactersInfo(charactersName);
        CompletableFuture<String> CompletableFutureOcid = characterService.getCharacterOcid(getCharactersInfo);
        String ocid = CompletableFutureOcid.join();
        return characterService.getCharactersItemEquip(getCharactersInfo, apiKey, ocid);

    }

    @GetMapping("/CharactersItemTotalStatInfo") //착용 아이템 스탯정보 종합 (세트효과 미포함) CharactersItemTotalStatInfoDTO

    public CompletableFuture<CharactersItemTotalStatInfoDTO> getCharactersItemTotalStatInfo(@RequestParam String charactersName) {
        GetCharactersInfo getCharactersInfo = new GetCharactersInfo(charactersName);
        CompletableFuture<String> CompletableFutureOcid = characterService.getCharacterOcid(getCharactersInfo);
        String ocid = CompletableFutureOcid.join();
        CharactersInfoDTO charactersInfoDTO = characterService.getCharactersInfo(getCharactersInfo, apiKey, ocid).join();

        CharactersStatInfoDTO charactersStatInfoDTO =
                characterService.getCharactersStatInfo(getCharactersInfo, apiKey, ocid).join();
        CharactersItemEquipDTO charactersItemEquipDTO = characterService.getCharactersItemEquip(getCharactersInfo, apiKey, ocid).join();

        return characterService.getCharactersItemTotalInfo(getCharactersInfo);
    }

    @GetMapping("/CharactersEquipInfo")
    public CompletableFuture<CharactersItemStatInfoDTO> getCharactersItemStatInfo(@RequestParam String charactersName, String equipmentType) {
        GetCharactersInfo getCharactersInfo = new GetCharactersInfo(charactersName);
        CompletableFuture<String> CompletableFutureOcid = characterService.getCharacterOcid(getCharactersInfo);
        String ocid = CompletableFutureOcid.join();
        CharactersInfoDTO charactersInfoDTO = characterService.getCharactersInfo(getCharactersInfo, apiKey, ocid).join();
        CharactersStatInfoDTO charactersStatInfoDTO =
                characterService.getCharactersStatInfo(getCharactersInfo, apiKey, ocid).join();
        CharactersItemEquipDTO charactersItemEquipDTO = characterService.getCharactersItemEquip(getCharactersInfo, apiKey, ocid).join();

        return characterService.getCharactersItemInfo(getCharactersInfo, equipmentType);
    }

    @GetMapping("/CharactersSetEffect")
    public void getCharactersSetEffect(@RequestParam String charactersName) {

        GetCharactersInfo getCharactersInfo = new GetCharactersInfo(charactersName);
        CompletableFuture<String> CompletableFutureOcid = characterService.getCharacterOcid(getCharactersInfo);
        String ocid = CompletableFutureOcid.join();

        CharactersSetEffectInfoDTO charactersSetEffectInfoDTO = characterService.getCharactersSetInfo(charactersName, ocid).join();

        characterService.getCharactersSetStatInfo(charactersSetEffectInfoDTO);

    }

    @GetMapping("/CharactersArtiInfo") //유니온 아티팩트
    public CompletableFuture<CharactersArtiInfoDTO> getCharactersArtiInfo(@RequestParam String charactersName) {

        GetCharactersInfo getCharactersInfo = new GetCharactersInfo(charactersName);
        CompletableFuture<String> CompletableFutureOcid = characterService.getCharacterOcid(getCharactersInfo);
        String ocid = CompletableFutureOcid.join();

        return characterService.getCharactersArtiInfo(charactersName, ocid);

    }

    @GetMapping("/CharactersUnionInfo")  //유니온 공격대, 유니온 점령
    public CompletableFuture<CharactersUnionInfoDTO> getCharactersUnionInfo(@RequestParam String charactersName) {

        GetCharactersInfo getCharactersInfo = new GetCharactersInfo(charactersName);
        CompletableFuture<String> CompletableFutureOcid = characterService.getCharacterOcid(getCharactersInfo);
        String ocid = CompletableFutureOcid.join();

        return characterService.getCharactersUnionInfo(charactersName, ocid);

    }


    @GetMapping("/CharactersHyperStatInfo")  //하이퍼 스탯
    public CompletableFuture<CharactersHyperStatInfoDTO> getCharactersHyperStatInfo(@RequestParam String charactersName) {

        GetCharactersInfo getCharactersInfo = new GetCharactersInfo(charactersName);
        CompletableFuture<String> CompletableFutureOcid = characterService.getCharacterOcid(getCharactersInfo);
        String ocid = CompletableFutureOcid.join();

        return characterService.getCharactersHyperStatInfo(charactersName, ocid);

    }
    @GetMapping("/CharactersAbilityStatInfo")  //어빌리티
    public CompletableFuture<CharactersAbilityInfoDTO> getCharactersAbilityInfo(@RequestParam String charactersName) {

        GetCharactersInfo getCharactersInfo = new GetCharactersInfo(charactersName);
        CompletableFuture<String> CompletableFutureOcid = characterService.getCharacterOcid(getCharactersInfo);
        String ocid = CompletableFutureOcid.join();
        CharactersInfoDTO charactersInfoDTO = characterService.getCharactersInfo(getCharactersInfo, apiKey, ocid).join();
        CharactersStatInfoDTO charactersStatInfoDTO =
                characterService.getCharactersStatInfo(getCharactersInfo, apiKey, ocid).join();



        return characterService.getCharactersAbilityInfo(charactersName, ocid,charactersInfoDTO,charactersStatInfoDTO);

    }

//////////////////////////////////////////////////////////////////////////////

    @GetMapping("/CharactersSimbolInfo")  //심볼
    public CompletableFuture<CharactersSimbolInfoDTO> getCharactersSimbolInfo(@RequestParam String charactersName) {

        GetCharactersInfo getCharactersInfo = new GetCharactersInfo(charactersName);
        CompletableFuture<String> CompletableFutureOcid = characterService.getCharacterOcid(getCharactersInfo);
        String ocid = CompletableFutureOcid.join();

        return characterService.getCharactersSimbolInfo(charactersName, ocid);

    }
    @GetMapping("/CharactersPetEquipInfo")  //심볼
    public CompletableFuture<CharactersPetEquipInfoDTO> getCharactersPetEquipInfo(@RequestParam String charactersName) {

        GetCharactersInfo getCharactersInfo = new GetCharactersInfo(charactersName);
        CompletableFuture<String> CompletableFutureOcid = characterService.getCharacterOcid(getCharactersInfo);
        String ocid = CompletableFutureOcid.join();

        return characterService.getCharactersPetEquipInfo(charactersName, ocid);

    }

    @GetMapping("/CharactersSkillStatInfo")  //SkillStat
    public CompletableFuture<CharactersSkillStatInfoDTO> getCharactersNthSkillStatInfo(@RequestParam String charactersName,@RequestParam int nthSkill) {

        GetCharactersInfo getCharactersInfo = new GetCharactersInfo(charactersName);
        CompletableFuture<String> CompletableFutureOcid = characterService.getCharacterOcid(getCharactersInfo);
        String ocid = CompletableFutureOcid.join();
        return characterService.getCharactersSkillStatInfo(charactersName, ocid,nthSkill);

    }

    @GetMapping("/CharactersHexaStatInfo")  //HexaStat
    public CompletableFuture<CharactersHexaStatInfoDTO> getCharactersHexaStatInfo(@RequestParam String charactersName) {

        GetCharactersInfo getCharactersInfo = new GetCharactersInfo(charactersName);
        CompletableFuture<String> CompletableFutureOcid = characterService.getCharacterOcid(getCharactersInfo);
        String ocid = CompletableFutureOcid.join();
        return characterService.getCharactersHexaStatInfo(charactersName, ocid);

    }






    //////////////////////////////////////////////////////////////////////////////
//아래는 잠시 보류//


    @PostMapping("/setCharactersBaseTotalInfo")
    public void setCharactersBaseTotalInfo(@RequestParam String charactersName, int addAllStat, Double addBossDamage, int addAtMgPower, int petAtMgPower, int mainStatBase, int mainStatSkill, int mainStatPerBase, int mainStatPerSkill, int mainStatNonPer, int subStatBase, int subStatSkill, int subStatPerBase, int subStatPerSkill, int subStatNonPer, int atMgPowerBase, int atMgPowerSkill, int atMgPowerPerBase, int atMgPowerPerSkill, Double criticalDamageBase, Double criticalDamageSkill, Double damageBase, Double damageSkill, Double BossDamageBase, Double BossDamageSkill, boolean isFree) {
        CharactersBaseTotalInfoDTO charactersBaseTotalInfoDTO = new CharactersBaseTotalInfoDTO(charactersName, addAllStat, addBossDamage, addAtMgPower, petAtMgPower, mainStatBase, mainStatSkill, mainStatPerBase, mainStatPerSkill, mainStatNonPer, subStatBase, subStatSkill, subStatPerBase, subStatPerSkill, subStatNonPer, atMgPowerBase, atMgPowerSkill, atMgPowerPerBase, atMgPowerPerSkill, criticalDamageBase, criticalDamageSkill, damageBase, damageSkill, BossDamageBase, BossDamageSkill, isFree);
        characterService.setCharactersBaseTotalInfo(charactersBaseTotalInfoDTO);

    }

    @GetMapping("/calCharactersCombat")
    public String getCharactersCombat(@RequestParam String charactersName) {

        CompletableFuture<CharactersBaseTotalInfoDTO> future = characterService.getCharactersBaseTotalInfoDTO(charactersName);
        CharactersBaseTotalInfoDTO charactersBaseTotalInfoDTO = future.join();
        return characterService.getCharactersCombat(charactersBaseTotalInfoDTO);

    }


//    @GetMapping("/charactersBaseInfo")
//    public String getCharactersBaseInfo(@RequestParam String charactersName, String date, int addAllStat, Double addBossDamage, int addAtMgPower, int petAtMgPower, int mainStatBase, int mainStatSkill, int mainStatPerBase, int mainStatPerSkill, int mainStatNonPer, int subStatBase, int subStatSkill, int subStatPerBase, int subStatPerSkill, int subStatNonPer, int atMgPowerBase, int atMgPowerSkill, int atMgPowerPerBase, int atMgPowerPerSkill, Double criticalDamageBase, Double criticalDamageSkill, Double damageBase, Double damageSkill, Double BossDamageBase, Double BossDamageSkill, boolean isFree) {
//        GetCharactersInfo getCharactersInfo = new GetCharactersInfo(charactersName, date);
//        GetCharactersTotalInfoDTO charactersTotalInfoDTO = new GetCharactersTotalInfoDTO(addAllStat, addBossDamage, addAtMgPower, petAtMgPower, mainStatBase, mainStatSkill, mainStatPerBase, mainStatPerSkill, mainStatNonPer, subStatBase, subStatSkill, subStatPerBase, subStatPerSkill, subStatNonPer, atMgPowerBase, atMgPowerSkill, atMgPowerPerBase, atMgPowerPerSkill, criticalDamageBase, criticalDamageSkill, damageBase, damageSkill, BossDamageBase, BossDamageSkill, isFree);
//
//        return characterService.getCharactersCombat(charactersTotalInfoDTO);
//    }


//    @GetMapping("/equipSimulation")
//    public void getEquipSimulation(@RequestParam int itemLevel, int starForce, int itemUpgrade,int addOptionStat, int potentialTotalMainStatPer, int potentialTotalSubStatPer, int potentialTotalAtMgPower) {
//
//        characterService.getEquipSimulation(itemLevel, starForce, itemUpgrade,addOptionStat,potentialTotalMainStatPer,potentialTotalSubStatPer,potentialTotalAtMgPower);
//
//    }


//    @GetMapping("/charactersChangeBaseInfo")
//    public String getCharactersChangeCombat(@RequestParam String charactersName, int addAllStat, Double addBossDamage, int addAtMgPower, int petAtMgPower, int mainStatBase, int mainStatSkill, int mainStatPerBase, int mainStatPerSkill, int mainStatNonPer, int subStatBase, int subStatSkill, int subStatPerBase, int subStatPerSkill, int subStatNonPer, int atMgPowerBase, int atMgPowerSkill, int atMgPowerPerBase, int atMgPowerPerSkill, Double criticalDamageBase, Double criticalDamageSkill, Double damageBase, Double damageSkill, Double BossDamageBase, Double BossDamageSkill, boolean isFree, int itemLevel, int starForce, int itemUpgrade,int addOptionStat, int potentialNewMainStatPer, int potentialNewSubStatPer, int potentialNewAtMgPowerPer,int potentialNewMainStat, int potentialNewSubStat, int potentialNewAtMgPowerStat) {
//
//        GetCharactersInfo getCharactersInfo = new GetCharactersInfo(charactersName);
//
//        int changedMainStatBase;
//        int changedSubStatBase;
//        int changedAtMgPowerBase;
//        int changedMainStatPerBase;
//        int changedSubStatPerBase;
//        int changedAtMgPowerPerBase;
//
//        //캐릭터 착용 모자 정
//        CompletableFuture<CharactersHatInfoDTO> future = characterService.getCharactersHatInfo(getCharactersInfo);
//        CharactersHatInfoDTO hatInfo = future.join();
//
//        // 캐릭터 착용 모자 정보
//        int charactersHatMainStat = hatInfo.getStr();
//        int charactersHatSubStat = hatInfo.getDex();
//        int charactersHatAtMgPower = hatInfo.getAttactPower();
//        int charactersHatPotentialMainStatPer = hatInfo.getStrPotentialPer();
//        int charactersHatPotentialMainStat = hatInfo.getStrPotentialStat();
//        int charactersHatPotentialSubStatPer = hatInfo.getDexPotentialPer();
//        int charactersHatPotentialSubStat = hatInfo.getDexPotentialStat();
//        int charactersHatPotentialAtMgPower = hatInfo.getAtMgPotentialStat();
//        int charactersHatPotentialAtMgPowerPer = hatInfo.getAtMgPotentialPer();
//
//        System.out.println(charactersHatMainStat + "dsdsdsd");
//        System.out.println(charactersHatSubStat + "dsdsdsd");
//        System.out.println(charactersHatAtMgPower + "dsdsdsd");
//
//        System.out.println(charactersHatPotentialMainStatPer + "dsdsdsd");
//        System.out.println(charactersHatPotentialMainStat + "dsdsdsd");
//        System.out.println(charactersHatPotentialSubStatPer + "dsdsdsd");
//
//        System.out.println(charactersHatPotentialSubStat + "dsdsdsd");
//        System.out.println(charactersHatPotentialAtMgPower + "dsdsdsd");
//        System.out.println(charactersHatPotentialAtMgPowerPer + "dsdsdsd");
//
//        CompletableFuture<HatStatInfoDTO> futureHat = characterService.getEquipSimulation(itemLevel, starForce, itemUpgrade,addOptionStat,potentialNewMainStatPer,potentialNewSubStatPer,potentialNewAtMgPowerPer,potentialNewMainStat,potentialNewSubStat,potentialNewAtMgPowerStat);
//
//        HatStatInfoDTO newHatInfo = futureHat.join();
//
//        int newHatMainStat = newHatInfo.getMainStat();
//        int newHatSubStat = newHatInfo.getSubStat();
//        int newHatAtMgPower = newHatInfo.getAtMgPower();
//        int newHatAllStatPer = newHatInfo.getAllStatPer();
//        int newHatPotentialMainStatPer = newHatInfo.getPotentialTotalMainStatPer();
//        int newHatPotentialSubStatPer = newHatInfo.getPotentialTotalSubStatPer();
//        int newHatPotentialAtMgPower = newHatInfo.getPotentialTotalAtMgPower();
//        int newHatPotentialMainStat = newHatInfo.getPotentialTotalMainStat();
//        int newHatPotentialSubStat = newHatInfo.getPotentialTotalSubStat();
//        int newHatPotentialAtMgPowerPer = newHatInfo.getPotentialTotalAtMgPowerPer();
//
//        changedMainStatBase = mainStatBase + (newHatMainStat +newHatPotentialMainStat - charactersHatMainStat);
//        changedSubStatBase = subStatBase + (newHatSubStat + newHatPotentialSubStat - charactersHatSubStat);
//        changedAtMgPowerBase = atMgPowerBase + (newHatAtMgPower + newHatPotentialAtMgPower - charactersHatAtMgPower);
//        changedMainStatPerBase = mainStatPerBase+newHatPotentialMainStatPer - charactersHatPotentialMainStatPer;
//        changedSubStatPerBase = subStatPerBase+newHatPotentialSubStatPer - charactersHatPotentialSubStatPer;
//        changedAtMgPowerPerBase = atMgPowerPerBase+ newHatPotentialAtMgPowerPer - charactersHatPotentialAtMgPowerPer;
//
//        System.out.println(changedMainStatBase + "dsdsdsd1");
//        System.out.println(changedSubStatBase + "dsdsdsd1");
//        System.out.println(changedAtMgPowerBase + "dsdsdsd1");
//        System.out.println(changedMainStatPerBase + "dsdsdsd1");
//        System.out.println(changedSubStatPerBase + "dsdsdsd1");
//        System.out.println(changedAtMgPowerPerBase + "dsdsdsd1");
//
//
//        GetCharactersTotalChangedInfoDTO getCharactersTotalChangedInfoDTO = new GetCharactersTotalChangedInfoDTO(addAllStat, addBossDamage, addAtMgPower, petAtMgPower, changedMainStatBase, mainStatSkill, changedMainStatPerBase, mainStatPerSkill, mainStatNonPer, changedSubStatBase, subStatSkill, changedSubStatPerBase, subStatPerSkill, subStatNonPer, changedAtMgPowerBase, atMgPowerSkill, changedAtMgPowerPerBase, atMgPowerPerSkill, criticalDamageBase, criticalDamageSkill, damageBase, damageSkill, BossDamageBase, BossDamageSkill, isFree);
//
//
//        return characterService.getCharactersChangeCombat(getCharactersTotalChangedInfoDTO, getCharactersInfo, itemLevel, starForce, itemUpgrade);
//    }

    @GetMapping("/choiceItem") //드메템 컨트롤러
    public void choiceItem(@RequestParam int limitPrice, int goalMesoNum, int goalDropNum) {

        DropItemChoiceService dropItemChoiceService = new DropItemChoiceService();
        dropItemChoiceService.dropItemChoice(limitPrice, goalMesoNum, goalDropNum);

    }


}
