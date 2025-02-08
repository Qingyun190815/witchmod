package witchmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.NlothsGift;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;

import witchmod.powers.AthamePower;  // 引入AthamePower自定义力量

import java.util.Iterator;

@SpirePatch(cls="com.megacrit.cardcrawl.rooms.AbstractRoom", method="alterCardRarityProbabilities")
public class MonsterRoomPatch {
    public static void Postfix(AbstractRoom __instance) {
        // PATCH FOR ATHAME RARE BONUS
        // 检查玩家是否拥有 AthamePower 并且该力量的数量大于0
        if (AbstractDungeon.player.hasPower(AthamePower.POWER_ID_FULL) && AbstractDungeon.player.getPower(AthamePower.POWER_ID_FULL).amount > 0){
            // 如果AthamePower处于满级，增加怪物房间中稀有卡片的出现概率
            __instance.rareCardChance += AbstractDungeon.player.getPower(AthamePower.POWER_ID_FULL).amount;
        }
    }
}
