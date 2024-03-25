package com.mapleApiTest.dropItemChoice.Service;

import com.mapleApiTest.dropItemChoice.DTO.*;
import org.springframework.stereotype.Service;

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


        int faceField[] = {60, 88, 35, 16};
        int eyeField[] = {62, 92, 41, 21};
        int[] earRingField = {31, 84, 22, 4};
        int[] ringOneField = {59, 95, 36, 27};
        int[] ringTwoField = {63, 99, 37, 26};
        int[] ringThreeField = {65, 94, 42, 27};
        int[] ringFourField = {68, 99, 44, 30};
        int[] pendantOneField = {62, 95, 42, 24};
        int[] pendantTwoField = {};

        for (int eyeFields : eyeField) {
            int eyesDropNum = 0;
            int eyesMesoNum = 0;
            int eyesPrice = eyeFields;
            String eyepick = null;
            if (eyeFields == eyeField[0]) {
                eyesDropNum = 1;
                eyesMesoNum = 1;
                eyepick = "눈 : 드메" + eyeFields;
            } else if (eyeFields == eyeField[1]) {
                eyesDropNum = 2;
                eyepick = "눈 : 드드" + eyeFields;
            } else if (eyeFields == eyeField[2]) {
                eyesMesoNum = 2;
                eyepick = "눈 : 메메" + eyeFields;
            } else if (eyeFields == eyeField[3]) {
                eyesDropNum = 1;
                eyepick = "눈 : 드" + eyeFields;


        }
        for (int faceFields : faceField) {

            int faceDropNum = 0;
            int faceMesoNum = 0;
            int facePrice = faceFields;
            String facepick = null;
            if (faceFields == faceField[0]) {
                faceDropNum = 1;
                faceMesoNum = 1;
                facepick = "얼장 : 드메" + faceFields;

            } else if (faceFields == faceField[1]) {
                faceDropNum = 2;
                facepick = "얼장 : 드드" + faceFields;

            } else if (faceFields == faceField[2]) {
                faceMesoNum = 2;
                facepick = "얼장 : 메메" + faceFields;
            } else if (faceFields == faceField[3]) {
                faceDropNum = 1;
                facepick = "얼장 : 드" + faceFields;


        }
//        for (int earRingFields : earRingField) {
//
//            int earRingDropNum = 0;
//            int earRingMesoNum = 0;
//            int earRingPrice = earRingFields;
//            String earRingpick = null;
//
//            if (earRingFields == earRingField[0]) {
//                earRingDropNum = 1;
//                earRingMesoNum = 1;
//                earRingpick = "귀고리 : 드메" + earRingFields;
//
//            } else if (earRingFields == earRingField[1]) {
//                earRingDropNum = 2;
//                earRingpick = "귀고리 : 드드" + earRingFields;
//
//            } else if (earRingFields == earRingField[2]) {
//                earRingMesoNum = 2;
//                earRingpick = "귀고리 : 메메" + earRingFields;
//            } else if (earRingFields == earRingField[3]) {
//                earRingDropNum = 1;
//                earRingpick = "귀고리 : 드" + earRingFields;
//
//
//            }
            for (int ringOneFields : ringOneField) {

                int ringOneDropNum = 0;
                int ringOneMesoNum = 0;
                int ringOnePrice = ringOneFields;
                String ringOnepick = null;

                if (ringOneFields == ringOneField[0]) {
                    ringOneDropNum = 1;
                    ringOneMesoNum = 1;
                    ringOnepick = "반지1 : 드메" + ringOneFields;

                } else if (ringOneFields == ringOneField[1]) {
                    ringOneDropNum = 2;
                    ringOnepick = "반지1 : 드드" + ringOneFields;

                } else if (ringOneFields == ringOneField[2]) {
                    ringOneMesoNum = 2;
                    ringOnepick = "반지1 : 메메" + ringOneFields;
                } else if (ringOneFields == ringOneField[3]) {
                    ringOneDropNum = 1;
                    ringOnepick = "반지1 : 드" + ringOneFields;


                }

//                for (int ringTwoFields : ringTwoField) {
//
//                    int ringTwoDropNum = 0;
//                    int ringTwoMesoNum = 0;
//                    int ringTwoPrice = ringTwoFields;
//                    String ringTwopick = null;
//
//                    if (ringTwoFields == ringTwoField[0]) {
//                        ringTwoDropNum = 1;
//                        ringTwoMesoNum = 1;
//                        ringTwopick = "반지2 : 드메" + ringTwoFields;
//
//                    } else if (ringTwoFields == ringTwoField[1]) {
//                        ringTwoDropNum = 2;
//                        ringTwopick = "반지2 : 드드" + ringTwoFields;
//
//                    } else if (ringTwoFields == ringTwoField[2]) {
//                        ringTwoMesoNum = 2;
//                        ringTwopick = "반지2 : 메메" + ringTwoFields;
//                    } else if (ringTwoFields == ringTwoField[3]) {
//                        ringTwoDropNum = 1;
//                        ringTwopick = "반지2 : 드" + ringTwoFields;
//
//
//                    }

// pendantOne
                    for (int pendantOneFields : pendantOneField) {
                        int pendantOneDropNum = 0;
                        int pendantOneMesoNum = 0;
                        int pendantOnePrice = pendantOneFields;
                        String pendantOnepick = null;

                        if (pendantOneFields == pendantOneField[0]) {
                            pendantOneDropNum = 1;
                            pendantOneMesoNum = 1;
                            pendantOnepick = "펜던트1 : 드메 " + pendantOneFields;

                        } else if (pendantOneFields == pendantOneField[1]) {
                            pendantOneDropNum = 2;
                            pendantOnepick = "펜던트1 : 드드" + pendantOneFields;

                        } else if (pendantOneFields == pendantOneField[2]) {
                            pendantOneMesoNum = 2;
                            pendantOnepick = "펜던트1 : 메메" + pendantOneFields;
                        } else if (pendantOneFields == pendantOneField[3]) {
                            pendantOneDropNum = 1;
                            pendantOnepick = "펜던트1 : 드" + pendantOneFields;


                        }
//
//                                    for (int ringThreeFields : ringThreeField) {
//
//                                        int ringThreeDropNum = 0;
//                                        int ringThreeMesoNum = 0;
//                                        int ringThreePrice = ringThreeFields;
//                                        String ringThreepick = null;
//                                        if (ringThreeFields == ringThreeField[0]) {
//                                            ringThreeDropNum = 1;
//                                            ringThreeMesoNum = 1;
//                                            ringThreepick = "반지3 : 드메" + ringThreeFields;
//
//                                        } else if (ringThreeFields == ringThreeField[1]) {
//                                            ringThreeDropNum = 2;
//                                            ringThreepick = "반지3 : 드드" + ringThreeFields;
//
//                                        } else if (ringThreeFields == ringThreeField[2]) {
//                                            ringThreeMesoNum = 2;
//                                            ringThreepick = "반지3 : 메메" + ringThreeFields;
//                                        }
//
//                                        for (int pendantTwoFields : pendantTwoField) {
//                                            int pendantTwoDropNum = 0;
//                                            int pendantTwoMesoNum = 0;
//                                            int pendantTwoPrice = pendantTwoFields;
//                                            String pendantTwopick = null;
//
//                                            if (pendantTwoFields == pendantTwoField[0]) {
//                                                pendantTwoDropNum = 1;
//                                                pendantTwoMesoNum = 1;
//                                                pendantTwopick = "펜던트2 : 드메" + pendantTwoFields;
//
//                                            } else if (pendantTwoFields == pendantTwoField[1]) {
//                                                pendantTwoDropNum = 2;
//                                                pendantTwopick = "펜던트2 : 드드" + pendantTwoFields;
//
//                                            } else if (pendantTwoFields == pendantTwoField[2]) {
//                                                pendantTwoMesoNum = 2;
//                                                pendantTwopick = "펜던트2 : 메메" + pendantTwoFields;
//
//                                            }
//

//                        int totalPrice = eyesPrice + facePrice + earRingPrice + ringOnePrice + ringTwoPrice + pendantOnePrice;
//                        int totalDropNum = eyesDropNum + faceDropNum + earRingDropNum + ringOneDropNum + ringTwoDropNum + pendantOneDropNum;
//                        int totalMesoNum = eyesMesoNum + faceMesoNum + earRingMesoNum + ringOneMesoNum + ringTwoMesoNum + pendantOneMesoNum;
//                        if (totalMesoNum == goalMesoNum && totalDropNum == goalDropNum && totalPrice < limitPrice) {
//                            System.out.println("totalPrice" + totalPrice);
//                            System.out.println("eyesPrice" + eyesPrice);
//                            System.out.println("facePrice" + facePrice);
//                            System.out.println("earRingPrice" + earRingPrice);
//                            System.out.println("ringOnePrice" + ringOnePrice);
//                            System.out.println("ringTwoPrice" + ringTwoPrice);
////                                            System.out.println("ringThreePrice" + ringThreePrice);
//                            System.out.println("pendantOnePrice" + pendantOnePrice);
//                            System.out.println(eyepick + facepick + earRingpick + ringOnepick + ringTwopick + pendantOnepick);
//
                                              int totalPrice = eyesPrice + facePrice  + ringOnePrice +  pendantOnePrice;
                        int totalDropNum = eyesDropNum + faceDropNum  + ringOneDropNum +  pendantOneDropNum;
                        int totalMesoNum = eyesMesoNum + faceMesoNum  + ringOneMesoNum +  pendantOneMesoNum;
                        if (totalMesoNum == goalMesoNum && totalDropNum == goalDropNum && totalPrice < limitPrice) {
                            System.out.println("totalPrice" + totalPrice);
                            System.out.println("eyesPrice" + eyesPrice);
                            System.out.println("facePrice" + facePrice);
//                            System.out.println("earRingPrice" + earRingPrice);
                            System.out.println("ringOnePrice" + ringOnePrice);
//                            System.out.println("ringTwoPrice" + ringTwoPrice);
//                                            System.out.println("ringThreePrice" + ringThreePrice);
                            System.out.println("pendantOnePrice" + pendantOnePrice);
                            System.out.println(eyepick + facepick + ringOnepick+ pendantOnepick);




//                                        }

                        }
                    }
                }
            }
        }
    }
}
//}
//        }

