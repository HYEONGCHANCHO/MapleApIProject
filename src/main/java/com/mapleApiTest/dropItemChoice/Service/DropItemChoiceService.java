package com.mapleApiTest.dropItemChoice.Service;

import com.mapleApiTest.dropItemChoice.DTO.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DropItemChoiceService {
    public void dropItemChoice(int limitPrice, int goalMesoNum, int goalDropNum) {
//        EyesItemDTO eyesItemDTO = new EyesItemDTO();
//        FaceItemDTO faceItemDTO = new FaceItemDTO();
//        EarRingItemDTO
//                earRingItemDTO = new EarRingItemDTO();
//        RingOneDTO ringOneDTO = new RingOneDTO();
//        RingTwoDTO ringTwoDTO = new RingTwoDTO();
//        RingThreeDTO ringThreeDTO = new RingThreeDTO();
//        RingFourDTO ringFourDTO = new RingFourDTO();
//        PendantOneDTO pendantOneDTO = new PendantOneDTO();
//        PendantTwoDTO pendantTwoDTO = new PendantTwoDTO();


//        int eyeField[] = {eyesItemDTO.getDropMeso(), eyesItemDTO.getDropDrop(), eyesItemDTO.getMesoMeso()};
//        int faceField[] = {faceItemDTO.getDropMeso(), faceItemDTO.getDropDrop(), faceItemDTO.getMesoMeso()};
//        int[] earRingField = {earRingItemDTO.getDropMeso(), earRingItemDTO.getDropDrop(), earRingItemDTO.getMesoMeso()};
//        int[] ringOneField = {ringOneDTO.getDropMeso(), ringOneDTO.getDropDrop(), ringOneDTO.getMesoMeso()};
//        int[] ringTwoField = {ringTwoDTO.getDropMeso(), ringTwoDTO.getDropDrop(), ringTwoDTO.getMesoMeso()};
//        int[] ringThreeField = {ringThreeDTO.getDropMeso(), ringThreeDTO.getDropDrop(), ringThreeDTO.getMesoMeso()};
//// int[] ringFourField = {ringFourDTO.getDropMeso(), ringFourDTO.getDropDrop(), ringFourDTO.getMesoMeso()};
//        int[] pendantOneField = {pendantOneDTO.getDropMeso(), pendantOneDTO.getDropDrop(), pendantOneDTO.getMesoMeso()};
//        int[] pendantTwoField = {pendantTwoDTO.getDropMeso(), pendantTwoDTO.getDropDrop(), pendantTwoDTO.getMesoMeso()};
//


        int faceField[] = {60, 88, 35, 16, 15, 0};
        int eyeField[] = {62, 92, 41, 21, 15, 0};
        int[] earRingField = {31, 84, 22, 4, 15, 0};
        int[] ringOneField = {59, 95, 36, 27, 15, 0};
        int[] ringTwoField = {63, 99, 37, 26, 15, 0};
        int[] ringThreeField = {65, 94, 42, 27, 15, 0};
        int[] ringFourField = {68, 99, 44, 30, 15, 0};
        int[] pendantOneField = {62, 95, 42, 24, 15, 0};
        int[] pendantTwoField = {62, 95, 42, 24, 15, 0};

        int eyesDropNum = 0;
        int eyesMesoNum = 0;
        int eyesPrice = 0;
        String eyepick = null;
        int faceDropNum = 0;
        int faceMesoNum = 0;
        int facePrice = 0;
        String facepick = null;
        int earRingDropNum = 0;
        int earRingMesoNum = 0;
        int earRingPrice = 0;
        String earRingpick = null;
        int ringOneDropNum = 0;
        int ringOneMesoNum = 0;
        int ringOnePrice = 0;
        String ringOnepick = null;
        int ringTwoDropNum = 0;
        int ringTwoMesoNum = 0;
        int ringTwoPrice = 0;
        String ringTwopick = null;
        int ringThreeDropNum = 0;
        int ringThreeMesoNum = 0;
        int ringThreePrice = 0;
        String ringThreepick = null;
        int ringFourDropNum = 0;
        int ringFourMesoNum = 0;
        int ringFourPrice = 0;
        String ringFourpick = null;
        int pendantOneDropNum = 0;
        int pendantOneMesoNum = 0;
        int pendantOnePrice = 0;
        String pendantOnepick = null;
        int pendantTwoDropNum = 0;
        int pendantTwoMesoNum = 0;
        int pendantTwoPrice = 0;
        String pendantTwopick = null;
        int totalPrice = 0;
        int totalDropNum = 0;
        int totalMesoNum = 0;
        List<Map<String, Object>> array = new ArrayList<>();
        List map2 = new ArrayList<>();


        for (int eyeFields : eyeField) {
            if (eyeFields == eyeField[0]) {
                eyesDropNum = 1;
                eyesMesoNum = 1;
                eyepick = "눈 : 드메" + eyeFields;
            } else if (eyeFields == eyeField[1]) {
                eyesDropNum = 2;
                eyesMesoNum = 0;
                eyepick = "눈 : 드드" + eyeFields;
            } else if (eyeFields == eyeField[2]) {
                eyesDropNum = 0;
                eyesMesoNum = 2;
                eyepick = "눈 : 메메" + eyeFields;
            } else if (eyeFields == eyeField[3]) {
                eyesDropNum = 1;
                eyesMesoNum = 0;
                eyepick = "눈 : 드" + eyeFields;
            } else if (eyeFields == eyeField[4]) {
                eyesDropNum = 0;
                eyesMesoNum = 1;
                eyepick = "눈 : 메" + eyeFields;
            } else if (eyeFields == eyeField[5]) {
                eyesDropNum = 0;
                eyesMesoNum = 0;
                eyepick = "눈 : x" + eyeFields;
            }


            for (int faceFields : faceField) {
                if (faceFields == faceField[0]) {
                    faceDropNum = 1;
                    faceMesoNum = 1;
                    facepick = "얼장 : 드메" + faceFields;
                } else if (faceFields == faceField[1]) {
                    faceDropNum = 2;
                    faceMesoNum = 0;

                    facepick = "얼장 : 드드" + faceFields;
                } else if (faceFields == faceField[2]) {
                    faceDropNum = 0;
                    faceMesoNum = 2;
                    facepick = "얼장 : 메메" + faceFields;
                } else if (faceFields == faceField[3]) {
                    faceDropNum = 1;
                    faceMesoNum = 0;

                    facepick = "얼장 : 드" + faceFields;
                } else if (faceFields == faceField[4]) {
                    faceDropNum = 0;

                    faceMesoNum = 1;
                    facepick = "얼장 : 메" + faceFields;
                } else if (faceFields == eyeField[5]) {
                    faceDropNum = 0;

                    faceMesoNum = 0;
                    facepick = "얼장 : x" + faceFields;
                }


                for (int earRingFields : earRingField) {

                    if (earRingFields == earRingField[0]) {
                        earRingDropNum = 1;
                        earRingMesoNum = 1;
                        earRingpick = "귀고리 : 드메" + earRingFields;

                    } else if (earRingFields == earRingField[1]) {
                        earRingDropNum = 2;
                        earRingMesoNum = 0;

                        earRingpick = "귀고리 : 드드" + earRingFields;
                    } else if (earRingFields == earRingField[2]) {
                        earRingDropNum = 0;

                        earRingMesoNum = 2;
                        earRingpick = "귀고리 : 메메" + earRingFields;
                    } else if (earRingFields == earRingField[3]) {
                        earRingDropNum = 1;
                        earRingMesoNum = 0;
                        earRingpick = "귀고리 : 드" + earRingFields;
                    } else if (earRingFields == earRingField[4]) {
                        earRingDropNum = 0;
                        earRingMesoNum = 1;
                        earRingpick = "귀고리 : 메" + earRingFields;
                    } else if (earRingFields == earRingField[5]) {
                        earRingDropNum = 0;
                        earRingMesoNum = 0;
                        earRingpick = "귀고리 : x" + earRingFields;
                    }


                    for (int ringOneFields : ringOneField) {

                        if (ringOneFields == ringOneField[0]) {
                            ringOneDropNum = 1;
                            ringOneMesoNum = 1;
                            ringOnepick = "반지1 : 드메" + ringOneFields;

                        } else if (ringOneFields == ringOneField[1]) {
                            ringOneDropNum = 2;
                            ringOneMesoNum = 0;
                            ringOnepick = "반지1 : 드드" + ringOneFields;

                        } else if (ringOneFields == ringOneField[2]) {
                            ringOneDropNum = 0;
                            ringOneMesoNum = 2;
                            ringOnepick = "반지1 : 메메" + ringOneFields;
                        } else if (ringOneFields == ringOneField[3]) {
                            ringOneDropNum = 1;
                            ringOneMesoNum = 0;
                            ringOnepick = "반지1 : 드" + ringOneFields;
                        } else if (ringOneFields == ringOneField[4]) {
                            ringOneDropNum = 0;
                            ringOneMesoNum = 1;
                            ringOnepick = "반지1 : 메" + ringOneFields;
                        } else if (ringOneFields == ringOneField[5]) {
                            ringOneDropNum = 0;
                            ringOneMesoNum = 0;
                            ringOnepick = "반지1 : x" + ringOneFields;
                        }

                        for (int ringTwoFields : ringTwoField) {

                            if (ringTwoFields == ringTwoField[0]) {
                                ringTwoDropNum = 1;
                                ringTwoMesoNum = 1;
                                ringTwopick = "반지2 : 드메" + ringTwoFields;
                            } else if (ringTwoFields == ringTwoField[1]) {
                                ringTwoDropNum = 2;
                                ringTwoMesoNum = 0;
                                ringTwopick = "반지2 : 드드" + ringTwoFields;

                            } else if (ringTwoFields == ringTwoField[2]) {
                                ringTwoDropNum = 0;
                                ringTwoMesoNum = 2;
                                ringTwopick = "반지2 : 메메" + ringTwoFields;
                            } else if (ringTwoFields == ringTwoField[3]) {
                                ringTwoDropNum = 1;
                                ringTwoMesoNum = 0;
                                ringTwopick = "반지2 : 드" + ringTwoFields;
                            } else if (ringTwoFields == ringTwoField[4]) {
                                ringTwoDropNum = 0;
                                ringTwoMesoNum = 1;
                                ringTwopick = "반지2 : 메" + ringTwoFields;
                            } else if (ringTwoFields == ringTwoField[5]) {
                                ringTwoDropNum = 0;
                                ringTwoMesoNum = 0;
                                ringTwopick = "반지2 : x" + ringTwoFields;
                            }


                            for (int ringThreeFields : ringThreeField) {


                                if (ringThreeFields == ringThreeField[0]) {
                                    ringThreeDropNum = 1;
                                    ringThreeMesoNum = 1;
                                    ringThreepick = "반지3 : 드메" + ringThreeFields;
                                } else if (ringThreeFields == ringThreeField[1]) {
                                    ringThreeDropNum = 2;
                                    ringThreeMesoNum = 0;
                                    ringThreepick = "반지3 : 드드" + ringThreeFields;

                                } else if (ringThreeFields == ringThreeField[2]) {
                                    ringThreeDropNum = 0;
                                    ringThreeMesoNum = 2;
                                    ringThreepick = "반지3 : 메메" + ringThreeFields;
                                } else if (ringThreeFields == ringThreeField[3]) {
                                    ringThreeDropNum = 1;
                                    ringThreeMesoNum = 0;
                                    ringThreepick = "반지3 : 드" + ringThreeFields;
                                } else if (ringThreeFields == ringThreeField[4]) {
                                    ringThreeDropNum = 0;
                                    ringThreeMesoNum = 1;
                                    ringThreepick = "반지3 : 메" + ringThreeFields;
                                } else if (ringThreeFields == ringThreeField[5]) {
                                    ringThreeDropNum = 0;
                                    ringThreeMesoNum = 0;
                                    ringThreepick = "반지3 : x" + ringThreeFields;
                                }

                                for (int ringFourFields : ringFourField) {

                                    if (ringFourFields == ringFourField[0]) {
                                        ringFourDropNum = 1;
                                        ringFourMesoNum = 1;
                                        ringFourpick = "반지4 : 드메" + ringFourFields;
                                    } else if (ringFourFields == ringFourField[1]) {
                                        ringFourDropNum = 2;
                                        ringFourMesoNum = 0;
                                        ringFourpick = "반지4 : 드드" + ringFourFields;
                                    } else if (ringFourFields == ringFourField[2]) {
                                        ringFourDropNum = 0;
                                        ringFourMesoNum = 2;
                                        ringFourpick = "반지4 : 메메" + ringFourFields;
                                    } else if (ringFourFields == ringFourField[3]) {
                                        ringFourDropNum = 1;
                                        ringFourMesoNum = 0;
                                        ringFourpick = "반지4 : 드" + ringFourFields;
                                    } else if (ringFourFields == ringFourField[4]) {
                                        ringFourDropNum = 0;
                                        ringFourMesoNum = 1;
                                        ringFourpick = "반지4 : 메" + ringFourFields;
                                    } else if (ringFourFields == ringFourField[5]) {
                                        ringFourDropNum = 0;
                                        ringFourMesoNum = 0;
                                        ringFourpick = "반지4 : x" + ringFourFields;
                                    }


// pendantOne
                                    for (int pendantOneFields : pendantOneField) {
                                        if (pendantOneFields == pendantOneField[0]) {
                                            pendantOneDropNum = 1;
                                            pendantOneMesoNum = 1;
                                            pendantOnepick = "펜던트1 : 드메 " + pendantOneFields;
                                        } else if (pendantOneFields == pendantOneField[1]) {
                                            pendantOneDropNum = 2;
                                            pendantOneMesoNum = 0;
                                            pendantOnepick = "펜던트1 : 드드" + pendantOneFields;
                                        } else if (pendantOneFields == pendantOneField[2]) {
                                            pendantOneDropNum = 0;
                                            pendantOneMesoNum = 2;
                                            pendantOnepick = "펜던트1 : 메메" + pendantOneFields;
                                        } else if (pendantOneFields == pendantOneField[3]) {
                                            pendantOneDropNum = 1;
                                            pendantOneMesoNum = 0;
                                            pendantOnepick = "펜던트1 : 드" + pendantOneFields;
                                        } else if (pendantOneFields == pendantOneField[4]) {
                                            pendantOneDropNum = 0;
                                            pendantOneMesoNum = 1;
                                            pendantOnepick = "펜던트1 : 메" + pendantOneFields;
                                        } else if (pendantOneFields == pendantOneField[5]) {
                                            pendantOneDropNum = 0;
                                            pendantOneMesoNum = 0;
                                            pendantOnepick = "펜던트1 : x" + pendantOneFields;
                                        }

                                        for (int pendantTwoFields : pendantTwoField) {
                                            if (pendantTwoFields == pendantTwoField[0]) {
                                                pendantTwoDropNum = 1;
                                                pendantTwoMesoNum = 1;
                                                pendantTwopick = "펜던트2 : 드메 " + pendantTwoFields;
                                            } else if (pendantTwoFields == pendantTwoField[1]) {
                                                pendantTwoDropNum = 2;
                                                pendantTwoMesoNum = 0;
                                                pendantTwopick = "펜던트2 : 드드" + pendantTwoFields;
                                            } else if (pendantTwoFields == pendantTwoField[2]) {
                                                pendantTwoDropNum = 0;
                                                pendantTwoMesoNum = 2;
                                                pendantTwopick = "펜던트2 : 메메" + pendantTwoFields;
                                            } else if (pendantTwoFields == pendantTwoField[3]) {
                                                pendantTwoDropNum = 1;
                                                pendantTwoMesoNum = 0;
                                                pendantTwopick = "펜던트2 : 드" + pendantTwoFields;
                                            } else if (pendantTwoFields == pendantTwoField[4]) {
                                                pendantTwoDropNum = 0;
                                                pendantTwoMesoNum = 1;
                                                pendantTwopick = "펜던트2 : 메" + pendantTwoFields;
                                            } else if (pendantTwoFields == pendantTwoField[5]) {
                                                pendantTwoDropNum = 0;
                                                pendantTwoMesoNum = 0;
                                                pendantTwopick = "펜던트2 : x" + pendantTwoFields;
                                            }
                                            totalPrice = eyeFields + faceFields + earRingFields + ringOneFields + ringTwoFields + ringThreeFields + ringFourFields + pendantOneFields + pendantTwoFields;
                                            totalDropNum = eyesDropNum + faceDropNum + earRingDropNum + ringOneDropNum + ringTwoDropNum + ringThreeDropNum + ringFourDropNum + pendantOneDropNum + pendantTwoDropNum;
                                            totalMesoNum = eyesMesoNum + faceMesoNum + earRingMesoNum + ringOneMesoNum + ringTwoMesoNum + ringThreeMesoNum + ringFourMesoNum + pendantOneMesoNum + pendantTwoMesoNum;

//                                            System.out.println("totalPrice11111111...."+totalPrice);
//                                            System.out.println("totalDropNum...."+totalDropNum);
//                                            System.out.println("totalMesoNum...."+totalMesoNum);
//
// 반복문을 통해 여러 개의 맵을 생성하고 배열에 추가합니다.
//                                            Map<String, Object> map = new HashMap<>();
//                                            map.put("총 가격", totalPrice);
//                                            map.put("눈장식", eyepick);
//                                            map.put("얼굴장식", facepick);
//                                            map.put("귀고리", earRingpick);
//                                            map.put("팬던트1", pendantOnepick);
//                                            map.put("팬던트2", pendantTwopick);
//                                            map.put("반지1", ringOnepick);
//                                            map.put("반지2", ringTwopick);
//                                            map.put("반지3", ringThreepick);
//                                            map.put("반지4", ringFourpick);
//                                            map.put("totalDropNum", totalDropNum);
//                                            map.put("totalMesoNum", totalMesoNum);
//                                            // 필요한 만큼 키-값 쌍을 추가할 수 있습니다.
//                                            array.add(map);

                                            map2.add(totalPrice);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

//            totalPrice = eyesPrice + facePrice + earRingPrice + ringOnePrice + ringTwoPrice + ringThreePrice + ringFourPrice + pendantOnePrice + pendantTwoPrice;
//            totalDropNum = eyesDropNum + faceDropNum + earRingDropNum + ringOneDropNum + ringTwoDropNum + ringThreeDropNum + ringFourDropNum + pendantOneDropNum + pendantTwoDropNum;
//            totalMesoNum = eyesMesoNum + faceMesoNum + earRingMesoNum + ringOneMesoNum + ringTwoMesoNum + ringThreeMesoNum + ringFourMesoNum + pendantOneMesoNum + pendantTwoMesoNum;
//
//            System.out.println("totalPrice11111111...."+totalPrice);
//            System.out.println("totalDropNum...."+totalDropNum);
//            System.out.println("totalMesoNum...."+totalMesoNum);


        }

//
//        Integer minKey1Value = null;
//        Object key2Value = null;
//        Object key3Value = null;
//        Object key4Value = null;
//        Object key5Value = null;
//        Object key6Value = null;
//        Object key7Value = null;
//        Object key8Value = null;
//        Object key9Value = null;
//        Object key10Value = null;
//        Map<String, Object> minMap = null;
//        Integer currentValue = 0;
//        for (Map<String, Object> map : array) {
//
//            System.out.println("dddddddddd1" + (int) map.get("totalDropNum"));
//            System.out.println("dddddddddd2" + (int) map.get("totalMesoNum"));
//            System.out.println("dddddddddd3" + goalMesoNum);
//            System.out.println("dddddddddd4" + goalDropNum);
//            System.out.println("dddddddddd" + map);
//
//            if ((int) map.get("totalDropNum") == goalDropNum && (int) map.get("totalMesoNum") == goalMesoNum) {
//                currentValue = (Integer) map.get("총 가격");
//                System.out.println("dddddddddd");
//                System.out.println("currentValue" + currentValue);
//                if (minKey1Value == null || currentValue < minKey1Value) {
//
//                    minKey1Value = currentValue;
//                    key2Value = map.get("눈장식");
//                    key3Value = map.get("얼굴장식");
//                    key4Value = map.get("귀고리");
//                    key5Value = map.get("팬던트1");
//                    key6Value = map.get("팬던트2");
//                    key7Value = map.get("반지1");
//                    key8Value = map.get("반지2");
//                    key9Value = map.get("반지3");
//                    key10Value = map.get("반지4");
//                    minMap = map;
//
//                }
//            }
//        }
//        System.out.println("minMap" + minMap + "\n");
//        System.out.println("minKey1Value: " + minKey1Value + "\n");
//        System.out.println("key2Value: " + key2Value + "\n");
//        System.out.println("key3Value: " + key3Value + "\n");
//        System.out.println("key4Value: " + key4Value + "\n");
//        System.out.println("key5Value: " + key5Value + "\n");
//        System.out.println("key6Value: " + key6Value + "\n");
//        System.out.println("key7Value: " + key7Value + "\n");
//        System.out.println("key8Value: " + key8Value + "\n");
//        System.out.println("key9Value: " + key9Value + "\n");
//        System.out.println("key10Value: " + key10Value + "\n");
//    }


    }
}