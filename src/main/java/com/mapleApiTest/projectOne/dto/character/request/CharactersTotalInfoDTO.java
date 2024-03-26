package com.mapleApiTest.projectOne.dto.character.request;


public class CharactersTotalInfoDTO {

    private String charactersName;

//    private String date;

    private String world_name;

    private String character_class;

    private int character_level;

    private double damage;

    private double bossDamage;


    private double finalDamage;


    private double ignoreRate;


    private double criticalDamage;

    private int str;

    private int dex;

    private int intel;

    private int luk;
    private int hp;

    private int attackPower;
    private int magicPower;

    private int combatPower;

    private String hatInfo;
    private String topInfo;
    private String bottomInfo;
    private String capeInfo;
    private String shoesInfo;
    private String glovesInfo;
    private String shoulderInfo;
    private String faceInfo;
    private String eyeInfo;
    private String earInfo;
    private String pendantOneInfo;
    private String pendantTwoInfo;
    private String beltInfo;
    private String ringOneInfo;
    private String ringTwoInfo;
    private String ringThreeInfo;
    private String ringFourInfo;
    private String weaponInfo;
    private String subWeaponInfo;
    private String emblemInfo;
    private String badgeInfo;
    private String medalInfo;
    private String poketInfo;
    private String heartInfo;

    public CharactersTotalInfoDTO(String charactersName, String world_name, String character_class, int character_level, double damage, double bossDamage, double finalDamage, double ignoreRate, double criticalDamage, int str, int dex, int intel, int luk, int hp, int attackPower, int magicPower, int combatPower) {
        this.charactersName = charactersName;
//        this.date = date;
        this.world_name = world_name;
        this.character_class = character_class;
        this.character_level = character_level;
        this.damage = damage;
        this.bossDamage = bossDamage;
        this.finalDamage = finalDamage;
        this.ignoreRate = ignoreRate;
        this.criticalDamage = criticalDamage;
        this.str = str;
        this.dex = dex;
        this.intel = intel;
        this.luk = luk;
        this.hp = hp;
        this.attackPower = attackPower;
        this.magicPower = magicPower;
        this.combatPower = combatPower;
    }

    public CharactersTotalInfoDTO(String charactersName, String world_name, String character_class, int character_level, double damage, double bossDamage, double finalDamage, double ignoreRate, double criticalDamage, int str, int dex, int intel, int luk, int hp, int attackPower, int magicPower, int combatPower, String hatInfo, String topInfo, String bottomInfo, String capeInfo, String shoesInfo, String glovesInfo, String shoulderInfo, String faceInfo, String eyeInfo, String earInfo, String pendantOneInfo, String pendantTwoInfo, String beltInfo, String ringOneInfo, String ringTwoInfo, String ringThreeInfo, String ringFourInfo, String weaponInfo, String subWeaponInfo, String emblemInfo, String badgeInfo, String medalInfo, String poketInfo, String heartInfo) {
        this.charactersName = charactersName;
//        this.date = date;
        this.world_name = world_name;
        this.character_class = character_class;
        this.character_level = character_level;
        this.damage = damage;
        this.bossDamage = bossDamage;
        this.finalDamage = finalDamage;
        this.ignoreRate = ignoreRate;
        this.criticalDamage = criticalDamage;
        this.str = str;
        this.dex = dex;
        this.intel = intel;
        this.luk = luk;
        this.hp = hp;
        this.attackPower = attackPower;
        this.magicPower = magicPower;
        this.combatPower = combatPower;
        this.hatInfo = hatInfo;
        this.topInfo = topInfo;
        this.bottomInfo = bottomInfo;
        this.capeInfo = capeInfo;
        this.shoesInfo = shoesInfo;
        this.glovesInfo = glovesInfo;
        this.shoulderInfo = shoulderInfo;
        this.faceInfo = faceInfo;
        this.eyeInfo = eyeInfo;
        this.earInfo = earInfo;
        this.pendantOneInfo = pendantOneInfo;
        this.pendantTwoInfo = pendantTwoInfo;
        this.beltInfo = beltInfo;
        this.ringOneInfo = ringOneInfo;
        this.ringTwoInfo = ringTwoInfo;
        this.ringThreeInfo = ringThreeInfo;
        this.ringFourInfo = ringFourInfo;
        this.weaponInfo = weaponInfo;
        this.subWeaponInfo = subWeaponInfo;
        this.emblemInfo = emblemInfo;
        this.badgeInfo = badgeInfo;
        this.medalInfo = medalInfo;
        this.poketInfo = poketInfo;
        this.heartInfo = heartInfo;
    }

    public String getCharactersName() {
        return charactersName;
    }

//    public String getDate() {
//        return date;
//    }

    public String getWorld_name() {
        return world_name;
    }

    public String getCharacter_class() {
        return character_class;
    }

    public int getCharacter_level() {
        return character_level;
    }

    public double getDamage() {
        return damage;
    }

    public double getBossDamage() {
        return bossDamage;
    }

    public double getFinalDamage() {
        return finalDamage;
    }

    public double getIgnoreRate() {
        return ignoreRate;
    }

    public double getCriticalDamage() {
        return criticalDamage;
    }

    public int getStr() {
        return str;
    }

    public int getDex() {
        return dex;
    }

    public int getIntel() {
        return intel;
    }

    public int getLuk() {
        return luk;
    }

    public int getHp() {
        return hp;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public int getMagicPower() {
        return magicPower;
    }

    public int getCombatPower() {
        return combatPower;
    }

    public String getHatInfo() {
        return hatInfo;
    }

    public String getTopInfo() {
        return topInfo;
    }

    public String getBottomInfo() {
        return bottomInfo;
    }

    public String getCapeInfo() {
        return capeInfo;
    }

    public String getShoesInfo() {
        return shoesInfo;
    }

    public String getGlovesInfo() {
        return glovesInfo;
    }

    public String getShoulderInfo() {
        return shoulderInfo;
    }

    public String getFaceInfo() {
        return faceInfo;
    }

    public String getEyeInfo() {
        return eyeInfo;
    }

    public String getEarInfo() {
        return earInfo;
    }

    public String getPendantOneInfo() {
        return pendantOneInfo;
    }

    public String getPendantTwoInfo() {
        return pendantTwoInfo;
    }

    public String getBeltInfo() {
        return beltInfo;
    }

    public String getRingOneInfo() {
        return ringOneInfo;
    }

    public String getRingTwoInfo() {
        return ringTwoInfo;
    }

    public String getRingThreeInfo() {
        return ringThreeInfo;
    }

    public String getRingFourInfo() {
        return ringFourInfo;
    }

    public String getWeaponInfo() {
        return weaponInfo;
    }

    public String getSubWeaponInfo() {
        return subWeaponInfo;
    }

    public String getEmblemInfo() {
        return emblemInfo;
    }

    public String getBadgeInfo() {
        return badgeInfo;
    }

    public String getMedalInfo() {
        return medalInfo;
    }

    public String getPoketInfo() {
        return poketInfo;
    }

    public String getHeartInfo() {
        return heartInfo;
    }

    //    "["
//            "{\"item_equipment_part\":\"모자\"," +
//            "\"item_equipment_slot\":\"모자\"," +
//            "\"item_name\":\"앱솔랩스 시프캡\"," +
//            "\"item_icon\":\"https://open.api.nexon.com/static/maplestory/ItemIcon/KEPCPDME.png\"," +
//            "\"item_description\":null," +
//            "\"item_shape_name\":\"앱솔랩스 시프캡\"," +
//            "\"item_shape_icon\":\"https://open.api.nexon.com/static/maplestory/ItemIcon/KEPCPDME.png\"," +
//            "\"item_gender\":null," +
//
//            "\"item_total_option\":" +
//
//            "{\"str\":\"15\"," +
//            "\"dex\":\"124\"," +
//            "\"int\":\"0\"," +
//            "\"luk\":\"268\"," +
//            "\"max_hp\":\"3135\"," +
//            "\"max_mp\":\"0\"," +
//            "\"attack_power\":\"37\"," +
//            "\"magic_power\":\"33\"," +
//            "\"armor\":\"1267\"," +
//            "\"speed\":\"0\"," +
//            "\"jump\":\"0\"," +
//            "\"boss_damage\":\"0\"," +
//            "\"ignore_monster_armor\":\"10\"," +
//            "\"all_stat\":\"6\"," +
//            "\"damage\":\"0\"," +
//            "\"equipment_level_decrease\":0," +
//            "\"max_hp_rate\":\"0\"," +
//            "\"max_mp_rate\":\"0\"}," +
//
//            "\"item_base_option\":" +
//
//            "{\"str\":\"0\"," +
//            "\"dex\":\"45\"," +
//            "\"int\":\"0\"," +
//            "\"luk\":\"45\"," +
//            "\"max_hp\":\"0\"," +
//            "\"max_mp\":\"0\"," +
//            "\"attack_power\":\"3\"," +
//            "\"magic_power\":\"0\"," +
//            "\"armor\":\"400\"," +
//            "\"speed\":\"0\"," +
//            "\"jump\":\"0\"," +
//            "\"boss_damage\":\"0\"," +
//            "\"ignore_monster_armor\":\"10\"," +
//            "\"all_stat\":\"0\"," +
//            "\"max_hp_rate\":\"0\"," +
//            "\"max_mp_rate\":\"0\"," +
//            "\"base_equipment_level\":160}," +
//
//
//            "\"potential_option_grade\":\"레전드리\"," +
//            "\"additional_potential_option_grade\":\"에픽\"," +
//
//            "\"potential_option_1\":\"LUK : +12%\"," +
//            "\"potential_option_2\":\"LUK : +9%\"," +
//            "\"potential_option_3\":\"모든 스킬의 재사용 대기시간 : -2초(10초 이하는 10%감소, 5초 미만으로 감소 불가)\"," +
//            "\"additional_potential_option_1\":\"LUK : +4%\"," +
//            "\"additional_potential_option_2\":\"공격력 : +10\"," +
//            "\"additional_potential_option_3\":\"이동속도 : +6\"," +
//
//            "\"equipment_level_increase\":0," +
//
//            "\"item_exceptional_option\":" +
//
//            "{\"str\":\"0\",\"dex\":\"0\",\"int\":\"0\",\"luk\":\"0\",\"max_hp\":\"0\",\"max_mp\":\"0\",\"attack_power\":\"0\",\"magic_power\":\"0\"}," +
//            "\"item_add_option\":" +
//            "" +
//            "{\"str\":\"15\",\"dex\":\"0\",\"int\":\"0\",\"luk\":\"60\",\"max_hp\":\"1440\",\"max_mp\":\"0\",\"attack_power\":\"0\",\"magic_power\":\"0\",\"armor\":\"0\",\"speed\":\"0\",\"jump\":\"0\",\"boss_damage\":\"0\",\"damage\":\"0\",\"all_stat\":\"6\",\"equipment_level_decrease\":0}," +
//            "" +
//            "\"growth_exp\":0,\"growth_level\":0,\"scroll_upgrade\":\"12\",\"cuttable_count\":\"7\",\"golden_hammer_flag\":\"적용\",\"scroll_resilience_count\":\"0\",\"scroll_upgradeable_count\":\"0\"," +
//            "\"soul_name\":null,\"soul_option\":null,\"" +
//
//            "item_etc_option\":" +
//
//            "{\"str\":\"0\",\"dex\":\"0\",\"int\":\"0\",\"luk\":\"84\",\"max_hp\":\"1440\",\"max_mp\":\"0\",\"attack_power\":\"1\",\"magic_power\":\"0\",\"armor\":\"120\",\"speed\":\"0\",\"jump\":\"0\"}," +
//
//            "\"starforce\":\"18\",\"starforce_scroll_flag\":\"미사용\",\"" +
//
//            "item_starforce_option\":{\"str\":\"0\",\"dex\":\"79\",\"int\":\"0\",\"luk\":\"79\",\"max_hp\":\"255\",\"max_mp\":\"0\",\"attack_power\":\"33\",\"magic_power\":\"33\",\"armor\":\"747\",\"speed\":\"0\",\"jump\":\"0\"},\"special_ring_level\":0,\"date_expire\":null},{\"item_equipment_part\":

}
