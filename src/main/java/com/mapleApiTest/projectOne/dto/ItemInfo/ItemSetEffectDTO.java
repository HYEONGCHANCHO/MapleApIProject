package com.mapleApiTest.projectOne.dto.ItemInfo;


import com.fasterxml.jackson.databind.JsonNode;

public class ItemSetEffectDTO {

    int allStat=0;
    int damage=0;
    int atMgPower=0;
    int criticalDamage=0;

    public void setItemSetEffect(int absolSetCount, int arcaneSetCount, int bossAcSetCount, int cvelSetCount, int lucidAcSetCount, int lomienSetCount, int eternalSetCount, int mystarSetCount) {

        if (absolSetCount >1) {
            damage += 10;
            atMgPower += 20;
            if (absolSetCount >2) {
                allStat += 30;
                damage += 10;
                atMgPower += 20;
                if (absolSetCount >3) {
                    atMgPower += 25;
                    if (absolSetCount >4) {
                        damage += 10;
                        atMgPower += 30;
                        if (absolSetCount >5) {
                            atMgPower += 20;
                            if (absolSetCount >6) {
                                atMgPower += 20;
                            }
                        }
                    }
                }
            }
        }
            if (arcaneSetCount >1) {
                damage += 10;
                atMgPower += 30;
                if (arcaneSetCount >2) {
                    atMgPower += 30;
                    if (arcaneSetCount >3) {
                        allStat += 50;
                        damage += 10;
                        atMgPower += 35;
                        if (arcaneSetCount >4) {
                            damage += 10;
                            atMgPower += 40;
                            if (arcaneSetCount >5) {
                                atMgPower += 30;
                                if (arcaneSetCount >6) {
                                    atMgPower += 30;
                                }
                            }
                        }
                    }
                }
            }

            if (bossAcSetCount >2) {
                allStat += 10;
                atMgPower += 5;
                if (bossAcSetCount >4) {
                    allStat += 10;
                    atMgPower += 5;
                    if (bossAcSetCount >6) {
                        allStat += 10;
                        atMgPower += 10;
                        if (bossAcSetCount >8) {
                            allStat += 15;
                            damage += 10;
                            atMgPower += 10;
                        }
                    }
                }
            }

            if (cvelSetCount >1) {
                allStat += 10;
                atMgPower += 10;
                damage += 10;
                if (cvelSetCount >2) {
                    allStat += 10;
                    atMgPower += 10;
                    if (cvelSetCount >3) {
                        allStat += 15;
                        atMgPower += 15;
                        criticalDamage+=5;
                        if (cvelSetCount >4) {
                            allStat += 15;
                            damage += 10;
                            atMgPower += 15;
                            if (cvelSetCount >5) {
                                allStat += 15;
                                atMgPower += 15;
                                if (cvelSetCount >6) {
                                    allStat += 15;
                                    criticalDamage+=5;
                                    atMgPower += 15;
                                    if (cvelSetCount >7) {
                                        allStat += 15;
                                        damage += 10;
                                        atMgPower += 15;
                                        if (cvelSetCount >8) {
                                            allStat += 15;
                                            criticalDamage+=5;
                                            atMgPower += 15;

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (lucidAcSetCount >1) {
                allStat += 10;
                atMgPower += 10;
                damage += 10;
                if (lucidAcSetCount >2) {
                    allStat += 10;
                    atMgPower += 10;
                    if (lucidAcSetCount >3) {
                        allStat += 10;
                        atMgPower += 10;
                    }
                }
            }

            if (lomienSetCount >1) {
                allStat += 20;
                if (lomienSetCount >2) {
                    atMgPower += 50;
                    if (lomienSetCount >3) {
                        damage += 30;
                    }
                }
            }

            if (eternalSetCount >1) {
                atMgPower += 40;
                damage += 10;
                if (eternalSetCount >2) {
                    allStat += 50;
                    atMgPower += 40;
                    damage += 10;
                    if (eternalSetCount >3) {
                        atMgPower += 40;
                        damage += 10;
                        if (eternalSetCount >4) {
                            atMgPower += 40;
                            if (eternalSetCount >5) {
                                atMgPower += 40;
                                damage += 30;
                                if (eternalSetCount >6) {
                                    allStat += 50;
                                    atMgPower += 40;
                                    damage += 10;
                                    if (eternalSetCount >7) {
                                        atMgPower += 40;
                                        damage += 10;
                                    }
                                }
                            }
                        }
                    }
                }
            }

                if (mystarSetCount >2) {
                    atMgPower += 40;
                    if (mystarSetCount >3) {
                        damage += 20;
                    }
                }

            }

    public int getAllStat() {
        return allStat;
    }

    public void setAllStat(int allStat) {
        this.allStat = allStat;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getAtMgPower() {
        return atMgPower;
    }

    public void setAtMgPower(int atMgPower) {
        this.atMgPower = atMgPower;
    }

    public int getCriticalDamage() {
        return criticalDamage;
    }

    public void setCriticalDamage(int criticalDamage) {
        this.criticalDamage = criticalDamage;
    }
}
