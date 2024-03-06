package com.mapleApiTest.projectOne.dto.character.request;


public class CharactersTotalInfoDTO {

    private String charactersName;

    private String date;
//    private String item_equipment;

    private String world_name;

    private String character_class;

    private String character_level;

    private String hatInfo;

    public CharactersTotalInfoDTO(String charactersName, String date, String world_name, String character_class, String character_level, String hatInfo) {
        this.charactersName = charactersName;
        this.date = date;
//        this.item_equipment = item_equipment;
        this.world_name = world_name;
        this.character_class = character_class;
        this.character_level = character_level;
        this.hatInfo = hatInfo;
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
